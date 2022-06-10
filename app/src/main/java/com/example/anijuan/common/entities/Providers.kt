package com.example.anijuan.common.entities

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Providers(
    @get:Exclude var id:String = "",
    var name:String = ""
)
