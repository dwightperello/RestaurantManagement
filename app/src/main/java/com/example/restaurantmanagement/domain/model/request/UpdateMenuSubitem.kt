package com.example.restaurantmanagement.domain.model.request

data class UpdateMenuSubitem(
    val isAvailable: Boolean,
    val isRecommended: Boolean
)