package com.example.practica2_pdm.data


import com.squareup.moshi.Json

data class Question(
    val correctAnswerIndex: Int,
    val options: List<String>,
    val text: String
)