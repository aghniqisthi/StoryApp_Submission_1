package com.example.storyappdicoding.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.*
import com.example.storyappdicoding.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel : ViewModel() {
    private val _liststories = MutableLiveData<List<Story>>()
    val listStories: LiveData<List<Story>> = _liststories

    private val _detailstory = MutableLiveData<Story>()
    val detailstory: LiveData<Story> = _detailstory

    private val _addstory = MutableLiveData<ResponseAddStory>()
    val addstory: LiveData<ResponseAddStory> = _addstory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getAllStories(page:Int?, size:Int?, location:Int?, token:String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getAllStories(page, size, location, "bearer $token")
        client.enqueue(object : Callback<ResponseListStories> {
            override fun onResponse(call: Call<ResponseListStories>, response: Response<ResponseListStories>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _liststories.postValue(response.body()?.listStory)
                }
                else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseListStories>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getDetailStory(id:String, token:String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getDetailStory(id, "bearer $token")
        client.enqueue(object : Callback<ResponseDetailStory> {
            override fun onResponse(call: Call<ResponseDetailStory>, response: Response<ResponseDetailStory>) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _detailstory.postValue(response.body()!!.story)
                }
                else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseDetailStory>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun addStory(imageMultipart:MultipartBody.Part, description:RequestBody, token:String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().addStory(imageMultipart, description, "bearer $token")

        client.enqueue(object : Callback<ResponseAddStory> {
            override fun onResponse(
                call: Call<ResponseAddStory>,
                response: Response<ResponseAddStory>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _addstory.postValue(response.body())
                    }
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ResponseAddStory>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}