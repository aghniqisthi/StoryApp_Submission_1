package com.example.storyappdicoding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.storyappdicoding.databinding.FragmentRegisterBinding
import com.example.storyappdicoding.viewmodel.AuthViewModel


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authVM = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())[AuthViewModel::class.java]

        val name = binding.edRegisterName.text
        val email = binding.edRegisterEmail.text
        val pass = binding.edRegisterPassword.text

        binding.btnRegister.isEnabled =
            !(name.isNullOrBlank() || email.isNullOrBlank() || pass.isNullOrBlank())

        binding.edRegisterName.addTextChangedListener{
            binding.btnRegister.isEnabled =
                !(name.isNullOrBlank() || email.isNullOrBlank() || pass.isNullOrBlank())
        }

        binding.edRegisterEmail.addTextChangedListener{
            binding.btnRegister.isEnabled =
                !(name.isNullOrBlank() || email.isNullOrBlank() || pass.isNullOrBlank())
        }

        binding.edRegisterPassword.addTextChangedListener{
            binding.btnRegister.isEnabled =
                !(name.isNullOrBlank() || email.isNullOrBlank() || pass.isNullOrBlank())
        }

        binding.btnRegister.setOnClickListener {
            if(name.isNullOrBlank() || email.isNullOrBlank() || pass.isNullOrBlank()){
                Toast.makeText(requireContext(), "Fill in Name, Email, and Password", Toast.LENGTH_SHORT).show()
            }
            else {
                authVM.getRegisterResponse(name.toString(), email.toString(), pass.toString())
                authVM.register.observe(viewLifecycleOwner) {
                    showLoading(true)
                    if (it.error) {
                        Toast.makeText(requireContext(), "Sorry, ${it.message}", Toast.LENGTH_SHORT)
                            .show()
                        showLoading(false)
                    } else if (!it.error) {
                        showLoading(false)
                        Toast.makeText(requireContext(), "Register Success!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun showLoading(load:Boolean) {
        binding.progressBar.visibility = if (load) View.VISIBLE else View.GONE
    }
}