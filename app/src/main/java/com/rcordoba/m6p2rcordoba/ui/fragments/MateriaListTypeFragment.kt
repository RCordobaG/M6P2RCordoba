package com.rcordoba.m6p2rcordoba.ui.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.telecom.CallEndpoint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.rcordoba.m6p2rcordoba.Constants
import com.rcordoba.m6p2rcordoba.R
import com.rcordoba.m6p2rcordoba.application.MateriaApp
import com.rcordoba.m6p2rcordoba.data.MateriaRepository
import com.rcordoba.m6p2rcordoba.data.remote.model.MateriaDTO
import com.rcordoba.m6p2rcordoba.data.remote.model.MateriaTypeDTO
import com.rcordoba.m6p2rcordoba.data.remote.model.materiaOrbs
import com.rcordoba.m6p2rcordoba.databinding.FragmentMateriaListBinding
import com.rcordoba.m6p2rcordoba.databinding.FragmentMateriaTypeBinding
import com.rcordoba.m6p2rcordoba.ui.adapters.OrbAdapter
import com.rcordoba.m6p2rcordoba.ui.fragments.login.FragmentLogin
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 * Use the [MateriaListTypeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val TYPE_ID = "endpoint"

class MateriaListTypeFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMateriaTypeBinding? = null
    private val binding get() = _binding!!

    private lateinit var map : GoogleMap

    private var lat : Double = 0.0
    private var lon : Double = 0.0


    private var type_id: String? = null

    private lateinit var repository: MateriaRepository
    private lateinit var fragmentMediaController: MediaController

    private lateinit var auth: FirebaseAuth

    private lateinit var icon:BitmapDescriptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            MapsInitializer.initialize(requireContext())

        }
        catch (e : GooglePlayServicesNotAvailableException){
            e.printStackTrace()
        }
        arguments?.let { args ->
            type_id = args.getString(TYPE_ID)
            Log.d(Constants.LOGTAG, "Received endpoint: ${type_id}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMateriaTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        //val mapFragment : SupportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        //mapFragment.getMapAsync(this)

        binding.mapView3.onCreate(savedInstanceState)
        binding.mapView3.getMapAsync(this)

        binding.typeLogoutButton.setOnClickListener{
            auth.signOut()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView,FragmentLogin()).commit()
        }

        repository = (requireActivity().application as MateriaApp).repository

        lifecycleScope.launch {
            type_id?.let { type ->
                val call: Call<MateriaTypeDTO> = repository.getMateriaType(type)
                var orbs: List<materiaOrbs>?

                fragmentMediaController = MediaController(requireContext())

                call.enqueue(object : Callback<MateriaTypeDTO>{
                    override fun onResponse(
                        p0: Call<MateriaTypeDTO>,
                        response: Response<MateriaTypeDTO>
                    ) {
                        binding.errorTextViewDetail.visibility = View.INVISIBLE
                        binding.apply {
                            materiaTypeHeader.text = response.body()?.type
                            typeDescriptionText.text = response.body()?.description

                            orbs = response.body()?.orbs!!
                            Log.d(Constants.LOGTAG,"${orbs}")
                            orbRecyclerView.apply {
                                layoutManager = LinearLayoutManager(requireContext())
                                adapter = OrbAdapter(orbs!!,requireContext())
                            }
                            Glide.with(requireActivity())
                                .load(response.body()?.image)
                                .into(typeImage)

                            lat = response.body()?.latitude!!
                            lon = response.body()?.longitude!!
                            Log.d("Debug","${lat},${lon}")

                            try {
                                createMarker(lat,lon)
                            }
                            catch (error : Exception){
                                Log.e("Exception", "${error}")
                                error.printStackTrace();
                            }
                        }
                    }

                    override fun onFailure(p0: Call<MateriaTypeDTO>, p1: Throwable) {
                        binding.errorTextViewDetail.visibility = View.VISIBLE
                        Log.d(Constants.LOGTAG,"Error occurred: ${p1.message}")
                        Toast.makeText(requireContext(),
                            getString(R.string.toast_error_message, p1.message),
                            Toast.LENGTH_LONG).show()
                    }

                })
            }
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // createMarker(0.0,0.0)
    }

    private fun createMarker(lat: Double, lon: Double){
        val coordinates = LatLng(lat, lon)



        val marker = MarkerOptions()
            .position(coordinates)
            .title("Location")
            .snippet("Not actual place")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mapicon))

        map.addMarker(marker)

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates,18f),
            1000,
            null)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MateriaType.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(endpoint: String) =
            MateriaListTypeFragment().apply {
                arguments = Bundle().apply {
                    putString(TYPE_ID, endpoint)
                }
            }
    }


}