package com.rcordoba.m6p2rcordoba.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.rcordoba.m6p2rcordoba.R
import com.rcordoba.m6p2rcordoba.databinding.FragmentSignUpBinding
import com.rcordoba.m6p2rcordoba.ui.fragments.login.FragmentLogin

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var auth: FirebaseAuth


/**
 * A simple [Fragment] subclass.
 * Use the [FragmentSignUp.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentSignUp : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentSignUpBinding? = null
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.signupButton.setOnClickListener{
            var email = binding.signUpTextEmail.text.toString()
            var password = binding.signUpTextPassword.text.toString()
            if (email.isNotBlank()){
                if(password.isNotBlank()){
                    auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener{ task ->
                            if(task.isSuccessful){
                                var user = auth.currentUser
                                if(user != null){
                                    requireActivity().supportFragmentManager.beginTransaction()
                                        .replace(
                                            R.id.fragmentContainerView,
                                            MateriaListFragment()
                                        )
                                        .commit()
                                }
                            }

                            else{
                                Toast.makeText(requireContext(),
                                    getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
                                handleErrors(task)
                            }
                        }
                }
                else{
                    Toast.makeText(requireContext(),
                        getString(R.string.password_can_not_empty), Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(requireContext(),
                    getString(R.string.email_can_not_empty), Toast.LENGTH_SHORT).show()
            }
        }

        binding.signupTextBtn.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView,FragmentLogin())
                .commit()
        }
    }

    private fun handleErrors(task: Task<AuthResult>){
        var errorCode = ""

        try{
            errorCode = (task.exception as FirebaseAuthException).errorCode
        }catch(e: Exception){
            e.printStackTrace()
        }

        when(errorCode){
            "ERROR_INVALID_EMAIL" -> {
                Toast.makeText(requireContext(), getString(R.string.email_error), Toast.LENGTH_SHORT).show()
                binding.signUpTextEmail.error = getString(R.string.email_error)
                binding.signUpTextEmail.requestFocus()
            }
            "ERROR_WRONG_PASSWORD" -> {
                Toast.makeText(requireContext(),
                    getString(R.string.password_invalid), Toast.LENGTH_SHORT).show()
                binding.signUpTextPassword.error = getString(R.string.password_invalid)
                binding.signUpTextPassword.requestFocus()
                binding.signUpTextPassword.setText("")

            }
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                Toast.makeText(requireContext(),
                    getString(R.string.account_exists), Toast.LENGTH_SHORT).show()
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                Toast.makeText(requireContext(), getString(R.string.account_exists), Toast.LENGTH_LONG).show()
                binding.signUpTextEmail.error = getString(R.string.account_exists)
                binding.signUpTextEmail.requestFocus()
            }
            "ERROR_USER_TOKEN_EXPIRED" -> {
                Toast.makeText(requireContext(),
                    getString(R.string.session_expired), Toast.LENGTH_LONG).show()
            }
            "ERROR_USER_NOT_FOUND" -> {
                Toast.makeText(requireContext(),
                    getString(R.string.not_registered), Toast.LENGTH_LONG).show()
            }
            "ERROR_WEAK_PASSWORD" -> {
                Toast.makeText(requireContext(), getString(R.string.password_invalid), Toast.LENGTH_LONG).show()
                binding.signUpTextPassword.error = getString(R.string.password_length_invalid)
                binding.signUpTextPassword.requestFocus()
            }
            "NO_NETWORK" -> {
                Toast.makeText(requireContext(), getString(R.string.no_network), Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(requireContext(), getString(R.string.auth_error), Toast.LENGTH_SHORT).show()
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentSignUp.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentSignUp().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}