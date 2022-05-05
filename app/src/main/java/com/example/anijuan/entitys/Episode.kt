package com.example.anijuan.entitys

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Episode(
    var id:Long?= null,
    var photoUrl:String? ="",
    var name:String? = "",
    var description:String? = "",
    var date: Date? = null,
    var episode: Double = 0.0,
    var urlVideo:String = "",
    var anime: Anime? = null
    )
