package com.rcordoba.m6p2rcordoba.application

import android.app.Application
import com.rcordoba.m6p2rcordoba.data.MateriaRepository
import com.rcordoba.m6p2rcordoba.data.remote.RetrofitHelper
import retrofit2.Retrofit

class MateriaApp: Application() {
    private val retrofit by lazy {
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy {
        MateriaRepository(retrofit)
    }
}