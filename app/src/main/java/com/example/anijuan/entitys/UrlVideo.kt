package com.example.anijuan.entitys

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UrlVideo(
    @get:Exclude var id:String = "",
    var url:String = "",
    var provider:String = ""
)
