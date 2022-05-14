package com.example.anijuan.entitys

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Providers(
    @get:Exclude var id:String = "",
    var name:String = ""
)
