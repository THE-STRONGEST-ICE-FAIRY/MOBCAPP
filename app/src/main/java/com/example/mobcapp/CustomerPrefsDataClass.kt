package com.example.mobcapp

data class CustomerPrefsDataClass(
    val id: Int,
    val name: String,
    val easy: Map<String, Double>,
    val medium: Map<String, List<EqualizerDataClass>>,
    val hard: Map<String, List<EqualizerDataClass>>
)
