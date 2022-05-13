package com.example.anijuan.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.anijuan.adapters.EpisodesAdapter
import com.example.anijuan.databinding.ActivityAnimeDetailsBinding
import com.example.anijuan.entitys.Anime
import com.example.anijuan.entitys.Episode
import com.example.anijuan.interfaces.AnimeDetailsAux
import com.google.gson.Gson

class AnimeDetailsActivity : AppCompatActivity(),AnimeDetailsAux {

    private lateinit var mBinding:ActivityAnimeDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAnimeDetailsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        getAnimeDetails()

    }

    private fun getAnimeDetails() {
        val anime: Anime? = intent.getSerializableExtra("anime") as? Anime
        if(anime == null)
            finish()

        Glide.with(this)
            .load(anime!!.photoUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(mBinding.ivDetailsAnimePhoto)

        mBinding.tvNameDetailsAnime.text = anime.name
        mBinding.tvDescriptionDetailsAnime.text = anime.description
        addEpisodes(anime.episodes)
    }

    private fun addEpisodes(episodes: Map<String, Episode>){
        val layoutManage = LinearLayoutManager(this)
        val listEpisodes = mutableListOf<Episode>()
        for( episode in episodes){
            episode.value.id = episode.key
            listEpisodes.add(episode.value)
        }
        listEpisodes.sortBy {
            it.episode
        }
        val adapterEpisode = EpisodesAdapter(listEpisodes,this)

        mBinding.rvDetailsAnimeEpisodes.apply {
            layoutManager = layoutManage
            adapter = adapterEpisode
        }
    }

    override fun openActivityDetails(episode: Episode) {
        val intent = Intent(this,PlayerActivity::class.java)
        intent.putExtra("url",episode.urlVideo)
        startActivity(intent)
    }


}