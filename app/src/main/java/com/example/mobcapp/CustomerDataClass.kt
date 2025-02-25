package com.example.mobcapp

data class CustomerDataClass(
    val id: Int,
    val ordering: Int,
    val name: String,
    val image: List<String>,
    val dialogIntro: String,
    val dialogOutro: String,
    val dialogFiller: String,
    val dialogGood: String,
    val dialogBad: String,
)
