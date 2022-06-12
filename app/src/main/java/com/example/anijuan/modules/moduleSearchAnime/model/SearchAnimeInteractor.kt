package com.example.anijuan.modules.moduleSearchAnime.model

import com.example.anijuan.common.entities.Anime
import com.example.anijuan.common.settingsCustom.SettingsCustom
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class SearchAnimeInteractor {

    fun getAnimes(callback:(MutableList<Anime>)->Unit){
        doAsync {
            val query = FirebaseDatabase.getInstance()
                .reference
                .child(SettingsCustom.referenceAnimes)
            val listAnime = mutableListOf<Anime>()

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){

                        for (anime in snapshot.children){
                            val encodeAnime:Anime? = anime.getValue(Anime::class.java)

                            if (encodeAnime != null)
                                listAnime.add(encodeAnime)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
            uiThread {
                callback(listAnime)
            }
        }
    }
}