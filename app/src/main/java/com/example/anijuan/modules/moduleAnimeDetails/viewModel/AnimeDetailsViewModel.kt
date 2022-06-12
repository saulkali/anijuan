package com.example.anijuan.modules.moduleAnimeDetails.viewModel

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.anijuan.R
import com.example.anijuan.common.entities.Anime
import com.example.anijuan.common.entities.Episode
import com.example.anijuan.modules.moduleAnimeDetails.AnimeDetailsActivity
import com.example.anijuan.modules.moduleAnimeDetails.model.AnimeDetailsInteractor
import com.example.anijuan.modules.modulePlayer.PlayerActivity
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth

class AnimeDetailsViewModel:ViewModel() {

    private val interactor = AnimeDetailsInteractor()

    var animeSelected: MutableLiveData<Anime> = MutableLiveData<Anime>()
    var fireBaseAdapter:MutableLiveData<FirebaseRecyclerAdapter<Episode,AnimeDetailsActivity.AnimeDetailsHolder>> = MutableLiveData<FirebaseRecyclerAdapter<Episode,AnimeDetailsActivity.AnimeDetailsHolder>>()

    fun setAnimeSelected(anime: Anime){
        animeSelected.value = anime
        loadFirebaseAdapter()
    }

    fun getAnimeSelected():LiveData<Anime> = animeSelected

    fun getFirebaseAdapter():LiveData<FirebaseRecyclerAdapter<Episode,AnimeDetailsActivity.AnimeDetailsHolder>> = fireBaseAdapter

    private fun loadFirebaseAdapter(){
        interactor.getEpisodesAnime(animeSelected.value!!){ query ->
            val options = FirebaseRecyclerOptions.Builder<Episode>().setQuery(query){ value ->
                val episode = value.getValue(Episode::class.java)
                episode!!.id = value.key.toString()
                episode
            }.build()
            fireBaseAdapter.value = object: FirebaseRecyclerAdapter<Episode, AnimeDetailsActivity.AnimeDetailsHolder>(options){
                private lateinit var mContext: Context

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeDetailsActivity.AnimeDetailsHolder {
                    mContext = parent.context
                    val view = LayoutInflater.from(mContext).inflate(R.layout.item_card_episode,parent,false)
                    return AnimeDetailsActivity().AnimeDetailsHolder(view)
                }

                override fun onBindViewHolder(
                    holder: AnimeDetailsActivity.AnimeDetailsHolder,
                    position: Int,
                    model: Episode
                ) {
                    val episode = getItem(position)

                    with(holder){
                        Glide.with(mContext)
                            .load(episode.photoUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .circleCrop()
                            .into(binding.ivPhotoEpisode)

                        binding.tvNameEpisode.text = episode.name
                        binding.tvDescriptionEpisode.text = episode.description
                        binding.tvNumberEpisode.text = episode.episode.toString()

                        val stars:Int? = episode.listStars[
                                FirebaseAuth.getInstance().currentUser!!.uid
                        ]
                        stars?.let {
                            getStarsEpisode(it)
                        }

                        binding.cbEpisodeLike.isChecked = episode.listLike.containsKey(
                            FirebaseAuth.getInstance().currentUser!!.uid
                        )

                        binding.root.setOnClickListener {
                            //open player activity
                            val intent = Intent(mContext,PlayerActivity::class.java)
                            intent.putExtra("url", episode.urlVideo)
                            mContext.startActivity(intent)
                        }

                        binding.tvCounterLikes.text = episode.listLike.keys.size.toString()
                        binding.cbEpisodeLike.setOnCheckedChangeListener { _ , checked ->
                            interactor.setLikeEpisode(checked,animeSelected.value!!,episode){}
                        }

                        binding.cbStart1.setOnClickListener {
                            val checked = binding.cbStart1.isChecked
                            if (checked) interactor.setStartEpisode(1,animeSelected.value!!,episode){
                                getStarsEpisode(it)
                            }
                            else interactor.setStartEpisode(6,animeSelected.value!!,episode){
                                getStarsEpisode(it)
                            }
                        }
                        binding.cbStart2.setOnClickListener {
                            val checked = binding.cbStart2.isChecked
                            if (checked) interactor.setStartEpisode(2,animeSelected.value!!,episode){
                                getStarsEpisode(it)
                            }
                            else interactor.setStartEpisode(6,animeSelected.value!!,episode){
                                getStarsEpisode(it)
                            }
                        }
                        binding.cbStart3.setOnClickListener {
                            val checked = binding.cbStart3.isChecked
                            if (checked) interactor.setStartEpisode(3,animeSelected.value!!,episode){
                                getStarsEpisode(it)
                            }
                            else interactor.setStartEpisode(1,animeSelected.value!!,episode){
                                getStarsEpisode(it)
                            }
                        }
                        binding.cbStart4.setOnClickListener {
                            val checked = binding.cbStart4.isChecked
                            if (checked) interactor.setStartEpisode(4,animeSelected.value!!,episode){
                                getStarsEpisode(it)
                            }
                            else interactor.setStartEpisode(6,animeSelected.value!!,episode){
                                getStarsEpisode(it)
                            }
                        }
                        binding.cbStart5.setOnClickListener {
                            val checked = binding.cbStart5.isChecked
                            if (checked) interactor.setStartEpisode(5,animeSelected.value!!,episode){
                                getStarsEpisode(it)
                            }
                            else interactor.setStartEpisode(6,animeSelected.value!!,episode){
                                getStarsEpisode(it)
                            }
                        }
                    }
                }
            }

        }
    }

}