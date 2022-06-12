package com.example.anijuan.modules.moduleAnime.model

import com.example.anijuan.common.entities.Anime
import com.example.anijuan.common.firebase.FirebaseReferenceHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AnimeInteractor {
    fun getAnimes(callback:(DatabaseReference) -> Unit){
        val query = FirebaseDatabase.getInstance().reference
            .child(FirebaseReferenceHelper.referenceAnimes)
        callback(query)
    }

    fun saveAnimeSeeLater(anime: Anime,callback:(Anime)->Unit){
        // save anime see later
        val key = FirebaseDatabase.getInstance().reference.child(
            FirebaseReferenceHelper.referenceSeeLater)
            .push().key!!

        val keyUser = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().reference
            .child(FirebaseReferenceHelper.referenceSeeLater)
            .child(keyUser).child(FirebaseReferenceHelper.referenceAnimes).child(key).setValue(anime)

        callback(anime)
    }
}