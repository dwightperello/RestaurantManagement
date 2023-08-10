package com.example.restaurantmanagement.domain.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class response_login(
    val email: String,
    val firstname: String,
    val token: String,
    val userId: String):Parcelable
