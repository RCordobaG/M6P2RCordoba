package com.rcordoba.m6p2rcordoba.data

import com.rcordoba.m6p2rcordoba.data.remote.MateriaAPI
import com.rcordoba.m6p2rcordoba.data.remote.model.MateriaDTO
import com.rcordoba.m6p2rcordoba.data.remote.model.MateriaTypeDTO
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.create

class MateriaRepository(private val retrofit: Retrofit) {

    private val materiaAPI: MateriaAPI = retrofit.create(MateriaAPI::class.java)

    fun listAllTypes(url: String?): Call<List<MateriaDTO>> = materiaAPI.listAllTypes(url)

    fun getMateriaType(endpoint: String?): Call<MateriaTypeDTO> = materiaAPI.getMateriaType(endpoint)
}