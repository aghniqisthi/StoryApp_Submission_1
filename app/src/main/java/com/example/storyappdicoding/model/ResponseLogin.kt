package com.example.storyappdicoding.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseLogin(
    val error: Boolean,
    val loginResult: LoginResult,
    val message: String
) : Parcelable