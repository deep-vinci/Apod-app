package com.example.apod_kt.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable@Parcelize
data class Apod(
    val copyright: String = "",
    val date: String = "",
    val explanation: String = "",
    val hdurl: String = "",
    @SerialName("media_type")
    val mediaType: String = "",
    @SerialName("service_version")
    val serviceType: String = "",
    val title: String = "",
    val url: String = ""
) : Parcelable
