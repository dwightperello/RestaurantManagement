package com.example.restaurantmanagement.domain.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class SalesReportItem(
    val dateTimeStamp: String,
    val discountAmount: Double,
    val discountType: String,
    val id: Int,
    val paymentType: String,
    val refnumber: Int,
    val salesorderdetails: List<Salesorderdetail>,
    val serviceCharge: Double,
    val tableNumber: Int,
    val total: Double,
    val vat: Double,
    val vatAmount: Double
):Parcelable

@Parcelize
data class Salesorderdetail(
    val id: Int,
    val imageURL: String,
    val orderTableTag: Int,
    val orderTitle: String,
    val originalPrice: Double,
    val price: Double,
    val qty: Int,
    val salesId: Int
):Parcelable