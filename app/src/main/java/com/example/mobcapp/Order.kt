package com.example.mobcapp

data class Order(
    val id: Int,
    val mealName: String,
    val mealPrice: Double,
    val orderAmount: Int,
    val isValueMeal: Boolean,
    val friesSize: String?,
    val drinkSize: String?,
    val totalPrice: Double,
    val orderTime: String
)