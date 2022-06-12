package com.example.anijuan.modules.moduleSettings.model

import com.example.anijuan.common.entities.Providers
import com.example.anijuan.common.firebase.FirebaseReferenceHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


class SettingsInteractor {

    fun getProviders(callback:(ArrayList<String>) -> Unit){
        doAsync {
            val providersReference = FirebaseDatabase.getInstance().reference
                .child(FirebaseReferenceHelper.referenceProviders)
            val listProviders = arrayListOf<String>()

            val eventListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(provider in snapshot.children){
                        provider.getValue(Providers::class.java)?.let {
                            listProviders.add(it.name)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            }
            providersReference.addListenerForSingleValueEvent(eventListener)
            uiThread {
                callback(listProviders)
            }
        }
    }
}