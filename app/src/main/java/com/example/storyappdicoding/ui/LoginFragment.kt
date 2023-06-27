package com.example.storyappdicoding.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.storyappdicoding.databinding.FragmentLoginBinding
import com.example.storyappdicoding.viewmodel.AuthViewModel


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    companion object{
        lateinit var token : String
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authVM = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[AuthViewModel::class.java]

        val email = binding.edLoginEmail.text
        val pass = binding.edLoginPassword.text

        binding.btnLogin.isEnabled = !(email.isNullOrBlank() || pass.isNullOrBlank())

        binding.edLoginEmail.addTextChangedListener{
            binding.btnLogin.isEnabled = !(email.isNullOrBlank() || pass.isNullOrBlank())
        }

        binding.edLoginPassword.addTextChangedListener{
            binding.btnLogin.isEnabled = !(email.isNullOrBlank() || pass.isNullOrBlank())
        }

        binding.btnLogin.setOnClickListener {
            if(email.isNullOrBlank() || pass.isNullOrBlank()){
                Toast.makeText(requireContext(), "Fill in Email and Password", Toast.LENGTH_SHORT).show()
            }
            else {
                authVM.getLoginResponse(email.toString(), pass.toString())
                authVM.login.observe(viewLifecycleOwner) {
                    showLoading(true)
                    if (it.error == true) {
                        Toast.makeText(requireContext(), "Sorry, ${it.message}", Toast.LENGTH_SHORT)
                            .show()
                        showLoading(false)
                    } else if (it.error == false) {
                        showLoading(false)
                        Toast.makeText(requireContext(), "Login Success!", Toast.LENGTH_SHORT)
                            .show()

                        token = it.loginResult.token

                        val move = Intent(requireContext(), StoryActivity::class.java)
                        move.putExtra("data", it.loginResult)
                        move.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        requireContext().startActivity(move)
                        AuthenticationActivity().finish()
                    }
                }
            }
        }
    }

    private fun showLoading(load:Boolean) {
        binding.progressBar.visibility = if (load) View.VISIBLE else View.GONE
    }

}