package com.example.slaughterhousescreen.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

data class FileURL(
    @SerialName("fileName" ) var fileName : String? = null

    )
