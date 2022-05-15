package com.example.anijuan.entitys

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
data class Episode(
    @get:Exclude var id:String?= "",
    var photoUrl:String? ="",
    var name:String? = "",
    var description:String? = "",
    var date: String? = "",
    var episode: Double = 0.0,
    var urlVideo: String = "",
    var listLike:Map<String,Boolean> = mutableMapOf(),
    var listStars:Map<String,Int> = mutableMapOf(),
    var anime: Map<String,Anime> = mutableMapOf()
    ):Serializable
