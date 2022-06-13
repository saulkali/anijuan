package com.example.anijuan.modules.moduleSeeLater.model

import com.example.anijuan.common.firebase.FirebaseReferenceHelper
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SeeLaterInteractor {

    fun getAnime(keyUser:String,callback:(DatabaseReference) -> Unit){
        val query = FirebaseDatabase.getInstance()
            .reference
            .child(FirebaseReferenceHelper.referenceSeeLater)
            .child(keyUser)
            .child(FirebaseReferenceHelper.referenceAnimes)
        callback(query)
    }

    fun getEpisode(keyUser:String,callback: (DatabaseReference) -> Unit){
        val query = FirebaseDatabase.getInstance().reference
            .child(FirebaseReferenceHelper.referenceSeeLater)
            .child(keyUser)
            .child(FirebaseReferenceHelper.referenceEpisodes)
        callback(query)
    }
}