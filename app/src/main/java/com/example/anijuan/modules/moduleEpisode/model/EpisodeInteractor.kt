package com.example.anijuan.modules.moduleEpisode.model

import com.example.anijuan.common.entities.Anime
import com.example.anijuan.common.entities.Episode
import com.example.anijuan.common.firebase.FirebaseReferenceHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class EpisodeInteractor {

    fun getEpisodes(callback: (DatabaseReference) -> Unit){
        val query = FirebaseDatabase.getInstance().reference
            .child(FirebaseReferenceHelper.referenceEpisodes)
        callback(query)
    }

    fun saveEpisodeSeeLater(episode: Episode){
        val keyUser = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().reference
            .child(FirebaseReferenceHelper.referenceSeeLater)
            .child(keyUser)
            .child(FirebaseReferenceHelper.referenceEpisodes)
            .child(episode.id.toString())
            .setValue(episode)
    }

    fun setStartEpisode(stars:Int,anime: Anime,episode: Episode){
        //episodes references
        FirebaseDatabase.getInstance().reference
            .child(FirebaseReferenceHelper.referenceEpisodes)
            .child(episode.id.toString())
            .child(FirebaseReferenceHelper.referenceEpisodeStars)
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(stars)

        //anime references
        FirebaseDatabase.getInstance().reference
            .child(FirebaseReferenceHelper.referenceAnimes)
            .child(anime.id.toString())
            .child(FirebaseReferenceHelper.referenceEpisodes)
            .child(episode.id!!)
            .child(FirebaseReferenceHelper.referenceEpisodeStars)
            .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(stars)

    }
    fun setLikeEpisode(checked:Boolean,anime: Anime,episode:Episode){
        if(checked){
            //episodes references
            FirebaseDatabase.getInstance().reference
                .child(FirebaseReferenceHelper.referenceEpisodes)
                .child(episode.id.toString())
                .child(FirebaseReferenceHelper.referenceEpisodeLike)
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .setValue(checked)

            //anime references
            FirebaseDatabase.getInstance().reference
                .child(FirebaseReferenceHelper.referenceAnimes)
                .child(anime.id.toString())
                .child(FirebaseReferenceHelper.referenceEpisodes)
                .child(episode.id!!)
                .child(FirebaseReferenceHelper.referenceEpisodeLike)
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .setValue(checked)

        }else {

            //episodes references
            FirebaseDatabase.getInstance().reference
                .child(FirebaseReferenceHelper.referenceEpisodes)
                .child(episode.id.toString())
                .child(FirebaseReferenceHelper.referenceEpisodeLike)
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .setValue(null)

            //anime reference
            FirebaseDatabase.getInstance().reference
                .child(FirebaseReferenceHelper.referenceAnimes)
                .child(anime.id.toString())
                .child(FirebaseReferenceHelper.referenceEpisodes)
                .child(episode.id!!)
                .child(FirebaseReferenceHelper.referenceEpisodeLike)
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .setValue(null)
        }
    }
}