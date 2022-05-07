package com.example.anijuan.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.anijuan.R
import com.example.anijuan.databinding.ItemCardEpisodeBinding
import com.example.anijuan.entitys.Episode
import com.example.anijuan.interfaces.AnimeDetailsAux

class EpisodesAdapter(
    var episodes:MutableList<Episode>,
    var listener:AnimeDetailsAux
    ):RecyclerView.Adapter<EpisodesAdapter.ViewHolder>() {

    private lateinit var mContext:Context

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

            binding.root.setOnClickListener {
                listener.openActivityDetails(episode)
            }
        }

    }

    override fun getItemCount(): Int = episodes.size


    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val binding = ItemCardEpisodeBinding.bind(view)
    }
}