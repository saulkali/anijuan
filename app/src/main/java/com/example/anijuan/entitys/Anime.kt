package com.example.anijuan.entitys

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Anime(
    var id:Long? = null,
    var photoUrl:String? = "",
    var name:String? = "",
    var description:String? = "",
    var season:Double? = 0.0,
    )
