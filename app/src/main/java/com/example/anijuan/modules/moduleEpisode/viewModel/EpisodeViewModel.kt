package com.example.anijuan.modules.moduleEpisode.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.example.anijuan.R
import com.example.anijuan.common.entities.Anime
import com.example.anijuan.common.entities.Episode
import com.example.anijuan.modules.moduleEpisode.EpisodeFragment
import com.example.anijuan.modules.moduleEpisode.model.EpisodeInteractor
import com.example.anijuan.modules.modulePlayer.PlayerActivity
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth

class EpisodeViewModel: ViewModel() {

    private var progressBarVisibility:MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    private val interactor = EpisodeInteractor()

    private var firebaseAdapter:MutableLiveData<FirebaseRecyclerAdapter<Episode,EpisodeFragment.EpisodeHolder>>
        = MutableLiveData<FirebaseRecyclerAdapter<Episode,EpisodeFragment.EpisodeHolder>>()

    init {
        loadFirebaseAdapter()
    }

    fun getProgressBarVisibility():LiveData<Boolean> = progressBarVisibility
    fun setProgressBarVisibility(isVisibility:Boolean){
        progressBarVisibility.value = isVisibility
    }
    fun getFirebaseAdapter() = firebaseAdapter

    private fun loadFirebaseAdapter(){
        interactor.getEpisodes { query ->
            val options = FirebaseRecyclerOptions.Builder<Episode>()
                .setQuery(query){
                    val episode = it.getValue(Episode::class.java)
                    episode!!.id = it.key
                    episode
                }.build()

            firebaseAdapter.value = object: FirebaseRecyclerAdapter<Episode, EpisodeFragment.EpisodeHolder>(options){
                private lateinit var mContext: Context

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeFragment.EpisodeHolder {
                    mContext = parent.context

                    val view = LayoutInflater.from(mContext).inflate(R.layout.item_card_episode,parent,false)
                    return EpisodeFragment().EpisodeHolder(view)
                }

                @SuppressLint("NotifyDataSetChanged") // bug interno en firebase 8.0.0
                override fun onDataChanged() {
                    super.onDataChanged()
                    notifyDataSetChanged()
                    setProgressBarVisibility(false)
                }

                override fun onBindViewHolder(holder: EpisodeFragment.EpisodeHolder, position: Int, model: Episode) {
                    val episode = getItem(position)
                    var anime: Anime? = null
                    for (ani in episode.anime){
                        anime = ani.value
                    }
                    with(holder){
                        binding.tvNameEpisode.text = episode.name
                        binding.tvDescriptionEpisode.text = episode.description
                        binding.tvNumberEpisode.text = episode.episode.toString()
                        binding.tvCounterLikes.text = episode.listLike.keys.size.toString()

                        anime?.let {
                            binding.tvSubNameAnime.text = it.name
                        }

                        binding.cbEpisodeLike.isChecked = episode.listLike.containsKey(
                            FirebaseAuth.getInstance().currentUser!!.uid
                        )

                        val stars:Int? = episode.listStars.get(
                            FirebaseAuth.getInstance().currentUser!!.uid
                        )
                        stars?.let {
                            getStarsEpisode(it)
                        }

                        Glide.with(mContext)
                            .load(episode.photoUrl)
                            .centerCrop()
                            .circleCrop()
                            .into(binding.ivPhotoEpisode)

                        binding.root.setOnClickListener {
                            val intent = Intent(mContext, PlayerActivity::class.java)
                            intent.putExtra("url",episode.urlVideo)
                            mContext.startActivity(intent)
                        }
                        binding.root.setOnLongClickListener {

                            MaterialAlertDialogBuilder(mContext)
                                .setTitle(R.string.add_later_title)
                                .setPositiveButton(R.string.btn_confirm_dialog){ _, _ ->
                                    interactor.saveEpisodeSeeLater(episode)
                                }
                                .setNegativeButton(R.string.btn_cancel_dialog,null)
                                .show()
                            true
                        }
                        binding.cbEpisodeLike.setOnCheckedChangeListener { _, checked ->
                            interactor.setLikeEpisode(checked,anime!!,episode)
                        }

                        binding.cbStart1.setOnClickListener {
                            val checked = binding.cbStart1.isChecked
                            if (checked) interactor.setStartEpisode(1,anime!!,episode)
                            else interactor.setStartEpisode(6,anime!!,episode)
                        }
                        binding.cbStart2.setOnClickListener {
                            val checked = binding.cbStart2.isChecked
                            if (checked) interactor.setStartEpisode(2,anime!!,episode)
                            else interactor.setStartEpisode(6,anime!!,episode)
                        }
                        binding.cbStart3.setOnClickListener {
                            val checked = binding.cbStart3.isChecked
                            if (checked) interactor.setStartEpisode(3,anime!!,episode)
                            else interactor.setStartEpisode(6,anime!!,episode)
                        }
                        binding.cbStart4.setOnClickListener {
                            val checked = binding.cbStart4.isChecked
                            if (checked) interactor.setStartEpisode(4,anime!!,episode)
                            else interactor.setStartEpisode(6,anime!!,episode)
                        }
                        binding.cbStart5.setOnClickListener {
                            val checked = binding.cbStart5.isChecked
                            if (checked) interactor.setStartEpisode(5,anime!!,episode)
                            else interactor.setStartEpisode(6,anime!!,episode)
                        }


                    }//end with holder
                }//end function
            }//end mFirebaseAdapter
        }

    }
}