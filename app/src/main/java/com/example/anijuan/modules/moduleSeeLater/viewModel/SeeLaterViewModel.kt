package com.example.anijuan.modules.moduleSeeLater.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.anijuan.R
import com.example.anijuan.common.entities.Anime
import com.example.anijuan.common.entities.Episode
import com.example.anijuan.modules.moduleSeeLater.SeeLaterFragment
import com.example.anijuan.modules.moduleSeeLater.model.SeeLaterInteractor
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SeeLaterViewModel: ViewModel() {
    private val interactor = SeeLaterInteractor()
    private var firebaseAdapterAnime: MutableLiveData<FirebaseRecyclerAdapter<Anime, SeeLaterFragment.AnimeHolder>> =
        MutableLiveData<FirebaseRecyclerAdapter<Anime, SeeLaterFragment.AnimeHolder>>()
    private var firebaseAdapterEpisode: MutableLiveData<FirebaseRecyclerAdapter<Episode, SeeLaterFragment.EpisodeHolder>> =
        MutableLiveData<FirebaseRecyclerAdapter<Episode, SeeLaterFragment.EpisodeHolder>>()

    init {
        loadFirebaseAdapterAnime()
        loadFirebaseAdapterEpisode()
    }

    fun getFirebaseAdapterAnime() = firebaseAdapterAnime

    fun getFirebaseAdapterEpisode() = firebaseAdapterEpisode
    fun loadFirebaseAdapterEpisode(){
        val keyUser = FirebaseAuth.getInstance().currentUser!!.uid
        interactor.getEpisode(keyUser){ query ->
            val options = FirebaseRecyclerOptions.Builder<Episode>().setQuery(query){
                val anime = it.getValue(Episode::class.java)
                anime!!.id = it.key
                anime
            }.build()


            firebaseAdapterEpisode.value = object : FirebaseRecyclerAdapter<Episode, SeeLaterFragment.EpisodeHolder>(options){

                private lateinit var mContext: Context

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeeLaterFragment.EpisodeHolder {
                    mContext = parent.context

                    val view = LayoutInflater.from(mContext).inflate(R.layout.item_card_episode,parent,false)

                    return SeeLaterFragment().EpisodeHolder(view)
                }

                override fun onBindViewHolder(holder: SeeLaterFragment.EpisodeHolder, position: Int, model: Episode) {
                    val episode = getItem(position)
                    with(holder){

                        binding.tvNameEpisode.text = episode.name
                        binding.tvDescriptionEpisode.text = episode.description
                        binding.tvNumberEpisode.text = episode.episode.toString()

                        Glide.with(mContext)
                            .load(episode.photoUrl)
                            .centerCrop()
                            .into(binding.ivPhotoEpisode)

                        binding.root.setOnClickListener {
                            openPlayer(episode)
                        }

                        binding.root.setOnLongClickListener {
                            MaterialAlertDialogBuilder(mContext)
                                .setTitle(R.string.delete_later_title)
                                .setPositiveButton(R.string.btn_confirm_dialog){ _, _ ->
                                    deleteEpisodeSee(episode)
                                }.setNegativeButton(R.string.btn_cancel_dialog,null)
                                .show()
                            true
                        }

                    }
                }

                @SuppressLint("NotifyDataSetChanged") // bug interno firebase 8.0.0
                override fun onDataChanged() {
                    super.onDataChanged()
                    notifyDataSetChanged()
                }

            }
        }

    }
    fun loadFirebaseAdapterAnime(){
        val keyUser = FirebaseAuth.getInstance().currentUser!!.uid
        interactor.getAnime(keyUser){ query ->
            val options = FirebaseRecyclerOptions.Builder<Anime>().setQuery(query) {
                val anime = it.getValue(Anime::class.java)
                anime!!.id = it.key
                anime
            }.build()

            firebaseAdapterAnime.value = object : FirebaseRecyclerAdapter<Anime, SeeLaterFragment.AnimeHolder>(options){
                private lateinit var mContext: Context
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeeLaterFragment.AnimeHolder {
                    mContext = parent.context
                    val view = LayoutInflater.from(mContext).inflate(R.layout.item_card_anime,parent,false)
                    return SeeLaterFragment().AnimeHolder(view)
                }

                override fun onBindViewHolder(holder: SeeLaterFragment.AnimeHolder, position: Int, model: Anime) {
                    val anime = getItem(position)
                    with(holder){
                        binding.tvNameAnime.text = anime.name
                        binding.tvDescriptionAnime.text = anime.description
                        binding.tvSeasonAnime.text = anime.season.toString()

                        Glide.with(mContext)
                            .load(anime.photoUrl)
                            .centerCrop()
                            .into(binding.ivPhotoAnime)

                        binding.root.setOnClickListener {
                            // FIXME: open details anime activity
                        //openDetailsAnime(anime)
                        }
                        binding.root.setOnLongClickListener {
                            mContext?.let { it1 ->
                                MaterialAlertDialogBuilder(it1)
                                    .setTitle(mContext.getString(R.string.delete_later_title))
                                    .setPositiveButton(R.string.btn_confirm_dialog){ _, _ ->
                                        // FIXME: delete anime see later
                                    //deleteAnimeSee(anime)
                                    }
                                    .setNegativeButton(R.string.btn_cancel_dialog,null)
                                    .show()
                            }
                            true
                        }
                    }

                }

                @SuppressLint("NotifyDataSetChanged") // ugs
                override fun onDataChanged() {
                    super.onDataChanged()
                    notifyDataSetChanged()
                }

            }
        }
    }
}