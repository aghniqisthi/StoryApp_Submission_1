package com.example.storyappdicoding.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyappdicoding.model.ApiConfig
import com.example.storyappdicoding.model.ResponseLogin
import retrofit2.Call
import retrofit2.Callback
import com.example.storyappdicoding.model.ResponseRegister
import retrofit2.Response

class AuthViewModel : ViewModel(){
    private val _register = MutableLiveData<ResponseRegister>()
    val register: LiveData<ResponseRegister> = _register

    private val _login = MutableLiveData<ResponseLogin>()
    val login: LiveData<ResponseLogin> = _login

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getRegisterResponse(name:String, email:String, pass:String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().register(name, email, pass)
        client.enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(call: Call<ResponseRegister>, response: Response<ResponseRegister>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _register.postValue(response.body())
                }
                else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getLoginResponse(email:String, pass:String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().login(email, pass)
        client.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _login.postValue(response.body())
                }
                else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}