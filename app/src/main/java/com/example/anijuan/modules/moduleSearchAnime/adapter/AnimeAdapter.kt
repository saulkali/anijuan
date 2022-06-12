package com.example.anijuan.modules.moduleSearchAnime.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.anijuan.R
import com.example.anijuan.databinding.ItemCardAnimeBinding
import com.example.anijuan.common.entities.Anime
import com.example.anijuan.modules.moduleSearchAnime.interfaces.SearchAnimeAux

class AnimeAdapter(val animes:MutableList<Anime>, val listener: SearchAnimeAux):RecyclerView.Adapter<AnimeAdapter.AnimeHolder> () {

    private lateinit var mContext:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_card_anime,parent,false)

        return AnimeHolder(view)

    }

    override fun onBindViewHolder(holder: AnimeHolder, position: Int) {
        val anime = animes[position]
        with(holder){
            binding.tvNameAnime.text = anime.name
            binding.tvDescriptionAnime.text = anime.description
            binding.tvSeasonAnime.text = anime.season.toString()

            Glide.with(mContext)
                .load(anime.photoUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivPhotoAnime)

            binding.root.setOnClickListener {
                openAnimeDetails(anime)
            }

        }
    }

    override fun getItemCount(): Int = animes.size


    inner class AnimeHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemCardAnimeBinding.bind(view)

        fun openAnimeDetails(anime:Anime){
            listener.openAnimeDetails(anime)
        }
    }

}