package com.example.mobcapp

data class CustomerDataClass(
    val id: Int,
    val ordering: Int,
    val name: String,
    val image: List<String>,
    val dialogIntro: List<String>,
    val dialogOutro: List<String>,
    val dialogFiller: List<String>,
)
