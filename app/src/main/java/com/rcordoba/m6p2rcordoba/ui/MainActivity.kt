package com.rcordoba.m6p2rcordoba.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.rcordoba.m6p2rcordoba.Constants
import com.rcordoba.m6p2rcordoba.R
import com.rcordoba.m6p2rcordoba.data.MateriaRepository
import com.rcordoba.m6p2rcordoba.data.remote.RetrofitHelper
import com.rcordoba.m6p2rcordoba.data.remote.model.MateriaDTO
import com.rcordoba.m6p2rcordoba.databinding.ActivityMainBinding
import com.rcordoba.m6p2rcordoba.ui.fragments.MateriaListFragment
import com.rcordoba.m6p2rcordoba.ui.fragments.login.FragmentLogin
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private var keepSplash = true
    private val delay = 3600L

    private lateinit var binding: ActivityMainBinding

    private lateinit var repository: MateriaRepository
    private lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        //setupSplashScreen(splashScreen = splashScreen)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, FragmentLogin())
                .commit()
        }

        retrofit = RetrofitHelper().getRetrofit()
        repository = MateriaRepository(retrofit)

        lifecycleScope.launch {

            val call: Call<List<MateriaDTO>> = repository.listAllTypes("types")

            call.enqueue(object : Callback<List<MateriaDTO>> {
                override fun onResponse(p0: Call<List<MateriaDTO>>, response: Response<List<MateriaDTO>>) {
                    //Respuesta del server
                    Log.d(Constants.LOGTAG, "Respuesta recibida: ${response.body()}")
                }

                override fun onFailure(p0: Call<List<MateriaDTO>>, error: Throwable) {
                    //Manejo del error
                    Toast.makeText(
                        this@MainActivity,
                        "Error en la conexión: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })

        }

    }

    private fun setupSplashScreen(splashScreen: SplashScreen) {
        splashScreen.setKeepOnScreenCondition{keepSplash}
        Handler(Looper.getMainLooper()).postDelayed({
            keepSplash = false
        },delay)
    }
}