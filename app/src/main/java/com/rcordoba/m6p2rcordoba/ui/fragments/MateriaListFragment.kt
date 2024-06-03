package com.rcordoba.m6p2rcordoba.ui.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rcordoba.m6p2rcordoba.Constants
import com.rcordoba.m6p2rcordoba.R
import com.rcordoba.m6p2rcordoba.application.MateriaApp
import com.rcordoba.m6p2rcordoba.data.MateriaRepository
import com.rcordoba.m6p2rcordoba.data.remote.model.MateriaDTO
import com.rcordoba.m6p2rcordoba.databinding.FragmentMateriaListBinding
import com.rcordoba.m6p2rcordoba.ui.adapters.MateriaListAdapter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 * Use the [MateriaListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val TYPE_ID = "type_id"

class MateriaListFragment : Fragment() {
    private var _binding: FragmentMateriaListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: MateriaRepository
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaPlayer = MediaPlayer.create(requireContext(),R.raw.sound_effect)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMateriaListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = (requireActivity().application as MateriaApp).repository

        lifecycleScope.launch {
            val call: Call<List<MateriaDTO>> = repository.listAllTypes("types")

            call.enqueue(object : Callback<List<MateriaDTO>> {
                override fun onResponse(
                    p0: Call<List<MateriaDTO>>,
                    response: Response<List<MateriaDTO>>
                ) {
                    binding.errorTextViewMain.visibility = View.INVISIBLE
                    Log.d(Constants.LOGTAG, "Response: ${response.body()}")

                    response.body()?.let { types ->

                        binding.materiaListRecycler.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = MateriaListAdapter(types) { type ->
                                Log.d(Constants.LOGTAG, "Endpoint: ${type.endpoint.toString()}")
                                mediaPlayer.start()
                                requireActivity().supportFragmentManager.beginTransaction()
                                    .replace(
                                        R.id.fragmentContainerView,
                                        MateriaListTypeFragment.newInstance(type.endpoint.toString())
                                    )
                                    .addToBackStack(null)
                                    .commit()
                            }
                        }
                    }
                }

                override fun onFailure(p0: Call<List<MateriaDTO>>, p1: Throwable) {
                    binding.errorTextViewMain.visibility = View.VISIBLE
                    Log.d(Constants.LOGTAG,"Error occurred: ${p1.message}")
                    Toast.makeText(requireContext(),
                        getString(R.string.toast_error_message, p1.message),
                        Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MateriaList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(typeID: String) =
            MateriaListFragment().apply {
                arguments = Bundle().apply {
                    putString(TYPE_ID, typeID)
                }
            }
    }
}