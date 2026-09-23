package com.ivan.hotseat

data class Question(
    val text: String,
    val answers: List<String>,
    val correctIndex: Int
)
