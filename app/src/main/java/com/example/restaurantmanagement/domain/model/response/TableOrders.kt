package com.example.restaurantmanagement.domain.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class TableOrdersItem(
    val dateTimeStamp: String,
    val id: Int,
    val isPaid: Boolean,
    val orderPrice: Double,
    val orderStatus: String,
    val orderTagId: Int,
    val orderdetails: List<Orderdetail>,
    val tableNumber: Int
):Parcelable

@Parcelize
data class Orderdetail(
    val id: Int,
    val imageURL: String,
    val orderTableTag: Int,
    val orderTitle: String,
    val originalPrice: Double,
    val price: Double,
    val qty: Int,
    val tableOrderId: Int
):Parcelable