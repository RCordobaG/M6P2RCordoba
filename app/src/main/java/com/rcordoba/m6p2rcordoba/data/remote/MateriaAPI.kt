package com.rcordoba.m6p2rcordoba.data.remote

import com.rcordoba.m6p2rcordoba.data.remote.model.MateriaDTO
import com.rcordoba.m6p2rcordoba.data.remote.model.MateriaTypeDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface MateriaAPI {
    @GET()
    fun listAllTypes(
        @Url url: String?
    ): Call<List<MateriaDTO>>

    @GET("types/{type}")
    fun getMateriaType(
        @Path("type") type: String?
    ): Call<MateriaTypeDTO>
}