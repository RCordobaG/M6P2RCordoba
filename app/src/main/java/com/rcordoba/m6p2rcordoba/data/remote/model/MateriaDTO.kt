package com.rcordoba.m6p2rcordoba.data.remote.model

import com.google.gson.annotations.SerializedName

class MateriaDTO {
    @SerializedName("type")
    var type: String? = null

    @SerializedName("endpoint")
    var endpoint: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("image")
    var thumbnail: String? = null
}