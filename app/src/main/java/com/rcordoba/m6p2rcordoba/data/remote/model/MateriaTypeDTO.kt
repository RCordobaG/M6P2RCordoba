package com.rcordoba.m6p2rcordoba.data.remote.model

import com.google.gson.annotations.SerializedName

class MateriaTypeDTO {
    @SerializedName("type")
    var type: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("image")
    var image: String? = null

    @SerializedName("orbs")
    var orbs: List<materiaOrbs>? = null

    @SerializedName("lat")
    var latitude: Double? = null

    @SerializedName("lon")
    var longitude: Double? = null
}