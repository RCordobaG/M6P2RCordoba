package com.rcordoba.m6p2rcordoba.ui.fragments.login

import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth
import com.rcordoba.m6p2rcordoba.R
import com.rcordoba.m6p2rcordoba.databinding.FragmentLoginBinding
import com.rcordoba.m6p2rcordoba.ui.fragments.FragmentSignUp
import com.rcordoba.m6p2rcordoba.ui.fragments.MateriaListFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var auth: FirebaseAuth

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentLogin.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentLogin : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        if(auth.currentUser != null){
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainerView,
                    MateriaListFragment()
                )
                .commit()
        }



        binding.loginButton.setOnClickListener{
            var email = binding.loginTextEmail.text.toString()
            var password = binding.loginTextPassword.text.toString()
            if (email.isNotBlank()){
                if(password.isNotBlank() || password.length < 6){
                    auth.signInWithEmailAndPassword(email,password)
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
                                    getString(R.string.login_failed),Toast.LENGTH_SHORT).show()
                                    Log.w("FRBSERROR", "createUserWithEmail:failure", task.exception)
                            }
                            handleErrors(task)
                        }
                }
                else{
                    Toast.makeText(requireContext(),
                        getString(R.string.password_can_not_empty),Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(requireContext(),
                    getString(R.string.email_can_not_empty),Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginTextBtn.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView,FragmentSignUp())
                .commit()
        }

        binding.resetPasswordText.setOnClickListener{
            val resetMail = EditText(it.context)
            resetMail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

            val passwordResetDialog = AlertDialog.Builder(it.context)
                .setTitle(getString(R.string.recoverPassStr))
                .setMessage(getString(R.string.insertEmailRecStr))
                .setView(resetMail)
                .setPositiveButton(getString(R.string.sendStr)) { _, _ ->
                    val mail = resetMail.text.toString()
                    if (mail.isNotEmpty()) {
                        auth.sendPasswordResetEmail(mail).addOnSuccessListener {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.emailLinkStr),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }.addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.LinkEmailStrFail),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.InsertEmailStr),
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }
                }.setNegativeButton(getString(R.string.cancelStr)) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
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
                binding.loginTextEmail.error = getString(R.string.email_error)
                binding.loginTextEmail.requestFocus()
            }
            "ERROR_WRONG_PASSWORD" -> {
                Toast.makeText(requireContext(),
                    getString(R.string.password_invalid), Toast.LENGTH_SHORT).show()
                binding.loginTextPassword.error = getString(R.string.password_invalid)
                binding.loginTextPassword.requestFocus()
                binding.loginTextPassword.setText("")

            }
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> {
                //An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.
                Toast.makeText(requireContext(),
                    getString(R.string.account_exists), Toast.LENGTH_SHORT).show()
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                Toast.makeText(requireContext(), getString(R.string.account_exists), Toast.LENGTH_LONG).show()
                binding.loginTextEmail.error = getString(R.string.account_exists)
                binding.loginTextEmail.requestFocus()
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
                binding.loginTextPassword.error = getString(R.string.password_length_invalid)
                binding.loginTextPassword.requestFocus()
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
         * @return A new instance of fragment FragmentLogin.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentLogin().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}