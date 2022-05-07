package com.example.anijuan.entitys

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Anime(
    @get:Exclude var id:String? = null,
    var photoUrl:String? = "",
    var name:String? = "",
    var description:String? = "",
    var season:Double? = 0.0,
    var listLike:Map<String,Boolean>? = null,
    var episodes:MutableList<Episode> = arrayListOf()
    ):Serializable
