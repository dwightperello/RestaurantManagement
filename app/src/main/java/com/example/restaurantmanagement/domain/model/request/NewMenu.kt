package com.example.restaurantmanagement.domain.model.request

data class NewMenu(
    val description: String,
    val imageURL: String,
    val name: String,
    val tag: Int,
    val id:Int
)