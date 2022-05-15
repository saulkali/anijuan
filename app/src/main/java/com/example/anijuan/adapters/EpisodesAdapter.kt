package com.example.anijuan.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.anijuan.R
import com.example.anijuan.databinding.ItemCardEpisodeBinding
import com.example.anijuan.entitys.Anime
import com.example.anijuan.entitys.Episode
import com.example.anijuan.interfaces.AnimeDetailsAux
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EpisodesAdapter(
    var episodes:MutableList<Episode>,
    var listener:AnimeDetailsAux
    ):RecyclerView.Adapter<EpisodesAdapter.ViewHolder>() {

    private lateinit var mContext:Context
    private val mUrlAnimes = "animes"
    private val mUrlEpisodes = "episodes"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_card_episode,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val episode:Episode = episodes[position]
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

            val stars:Int? = episode.listStars.get(
                FirebaseAuth.getInstance().currentUser!!.uid
            )
            stars?.let {
                getStarsEpisode(it)
            }

            binding.cbEpisodeLike.isChecked = episode.listLike.containsKey(
                FirebaseAuth.getInstance().currentUser!!.uid
            )

            binding.root.setOnClickListener {
                listener.openActivityDetails(episode)
            }
            binding.tvCounterLikes.text = episode.listLike.keys.size.toString()
            binding.cbEpisodeLike.setOnCheckedChangeListener { _ , checked ->
                setLikeEpisode(episode,checked)
            }

            binding.cbStart1.setOnClickListener {
                val checked = binding.cbStart1.isChecked
                if (checked) setStarsEpisode(episode,1)
                else setStarsEpisode(episode,6)
            }
            binding.cbStart2.setOnClickListener {
                val checked = binding.cbStart2.isChecked
                if (checked) setStarsEpisode(episode,2)
                else setStarsEpisode(episode,6)
            }
            binding.cbStart3.setOnClickListener {
                val checked = binding.cbStart3.isChecked
                if (checked) setStarsEpisode(episode,3)
                else setStarsEpisode(episode,6)
            }
            binding.cbStart4.setOnClickListener {
                val checked = binding.cbStart4.isChecked
                if (checked) setStarsEpisode(episode,4)
                else setStarsEpisode(episode,6)
            }
            binding.cbStart5.setOnClickListener {
                val checked = binding.cbStart5.isChecked
                if (checked) setStarsEpisode(episode,5)
                else setStarsEpisode(episode,6)
            }

        }

    }

    override fun getItemCount(): Int = episodes.size


    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val binding = ItemCardEpisodeBinding.bind(view)

        fun setLikeEpisode(episode: Episode,checked:Boolean){
            var anime:Anime? = null
            for(ani in episode.anime){
                anime = ani.value
                anime.id = ani.key
            }
            anime?.let{
                if(checked){
                    //anime references
                    val databaseReference = FirebaseDatabase.getInstance().reference
                        .child(mUrlAnimes)
                        .child(it.id.toString())
                        .child(mUrlEpisodes).child(episode.id!!)
                        .child("listLike")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(checked)
                    //episodes references
                    val databaseReferenceEpisodes = FirebaseDatabase.getInstance().reference
                        .child(mUrlEpisodes)
                        .child(episode.id.toString())
                        .child("listLike")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(checked)
                }else {
                    //anime reference
                    val databaseReference = FirebaseDatabase.getInstance().reference
                        .child(mUrlAnimes)
                        .child(it.id.toString()).child(mUrlEpisodes)
                        .child(episode.id!!).child("listLike")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(null)
                    //episodes references
                    val databaseReferenceEpisodes = FirebaseDatabase.getInstance().reference
                        .child(mUrlEpisodes)
                        .child(episode.id.toString())
                        .child("listLike")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(null)
                }
            }
        }

        fun setStarsEpisode(episode: Episode,stars:Int){
            var anime:Anime? = null
            for(ani in episode.anime){
                anime = ani.value
                anime.id = ani.key
            }
            anime?.let {
                //episodes references
                val databaseReferenceEpisodes = FirebaseDatabase.getInstance().reference.child(mUrlEpisodes)
                    .child(episode.id.toString())
                    .child("listStars")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(stars)

                //anime references
                val databaseReference = FirebaseDatabase.getInstance().reference.child(mUrlAnimes)
                    .child(anime.id.toString())
                    .child(mUrlEpisodes).child(episode.id!!)
                    .child("listStars")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(stars)
            }
            getStarsEpisode(stars)
        }

        fun getStarsEpisode(stars: Int){
            when(stars){
                1 -> {
                    binding.cbStart1.isChecked = true
                    binding.cbStart2.isChecked = false
                    binding.cbStart3.isChecked = false
                    binding.cbStart4.isChecked = false
                    binding.cbStart5.isChecked = false
                }
                2 -> {
                    binding.cbStart1.isChecked = true
                    binding.cbStart2.isChecked = true
                    binding.cbStart3.isChecked = false
                    binding.cbStart4.isChecked = false
                    binding.cbStart5.isChecked = false
                }
                3 -> {
                    binding.cbStart1.isChecked = true
                    binding.cbStart2.isChecked = true
                    binding.cbStart3.isChecked = true
                    binding.cbStart4.isChecked = false
                    binding.cbStart5.isChecked = false
                }
                4 -> {
                    binding.cbStart1.isChecked = true
                    binding.cbStart2.isChecked = true
                    binding.cbStart3.isChecked = true
                    binding.cbStart4.isChecked = true
                    binding.cbStart5.isChecked = false
                }
                5 -> {
                    binding.cbStart1.isChecked = true
                    binding.cbStart2.isChecked = true
                    binding.cbStart3.isChecked = true
                    binding.cbStart4.isChecked = true
                    binding.cbStart5.isChecked = true
                }
                6 -> {
                    binding.cbStart1.isChecked = false
                    binding.cbStart2.isChecked = false
                    binding.cbStart3.isChecked = false
                    binding.cbStart4.isChecked = false
                    binding.cbStart5.isChecked = false
                }
            }
        }
    }
}