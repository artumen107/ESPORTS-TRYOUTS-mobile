package com.example.esportstryouts.Model

data class ReactionTimeDBRow (
    val hash: String = "",
    val date: String = "",
    val time: String = "",
    val best: Long = 0L,
    val email: String = "",
    val avg: Long = 0L
)