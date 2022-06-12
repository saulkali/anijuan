package com.example.anijuan.modules.moduleAnimeDetails.model

import com.example.anijuan.common.entities.Anime
import com.example.anijuan.common.entities.Episode
import com.example.anijuan.common.firebase.FirebaseReferenceHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AnimeDetailsInteractor {
    fun getEpisodesAnime(anime: Anime,callback: (DatabaseReference) -> Unit){
        val query:DatabaseReference = FirebaseDatabase.getInstance().reference
            .child(FirebaseReferenceHelper.referenceAnimes)
            .child(anime.id.toString())
            .child(FirebaseReferenceHelper.referenceEpisodes)
        callback(query)
    }

    fun setLikeEpisode(checked:Boolean,anime: Anime,episode: Episode, callback: (Boolean) -> Unit){
        if(checked){
            //anime references
            FirebaseDatabase.getInstance().reference
                .child( FirebaseReferenceHelper.referenceAnimes)
                .child(anime.id.toString())
                .child(FirebaseReferenceHelper.referenceEpisodes)
                .child(episode.id.toString())
                .child(FirebaseReferenceHelper.referenceEpisodeLike)
                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(checked)

            //episodes references
            FirebaseDatabase.getInstance().reference
                .child(FirebaseReferenceHelper.referenceEpisodes)
                .child(episode.id.toString())
                .child(FirebaseReferenceHelper.referenceEpisodeLike)
                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(checked)
        }else {
            //anime reference
            FirebaseDatabase.getInstance().reference
                .child(FirebaseReferenceHelper.referenceAnimes)
                .child(anime.id.toString()).child(FirebaseReferenceHelper.referenceEpisodes)
                .child(episode.id.toString()).child(FirebaseReferenceHelper.referenceEpisodeLike)
                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(null)
            //episodes references
            FirebaseDatabase.getInstance().reference
                .child(FirebaseReferenceHelper.referenceEpisodes)
                .child(episode.id.toString())
                .child(FirebaseReferenceHelper.referenceEpisodeLike)
                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(null)
        }
        callback(true)
    }

    fun setStartEpisode(stars:Int,anime: Anime,episode: Episode,callback:(Int)->Unit){
        FirebaseDatabase.getInstance().reference
            .child(FirebaseReferenceHelper.referenceEpisodes)
            .child(episode.id.toString())
            .child(FirebaseReferenceHelper.referenceEpisodeStars)
            .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(stars)

        //anime references
        FirebaseDatabase.getInstance().reference
            .child(FirebaseReferenceHelper.referenceAnimes)
            .child(anime.id.toString())
            .child(FirebaseReferenceHelper.referenceEpisodes)
            .child(episode.id!!)
            .child(FirebaseReferenceHelper.referenceEpisodeStars)
            .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(stars)

        callback(stars)

    }
}