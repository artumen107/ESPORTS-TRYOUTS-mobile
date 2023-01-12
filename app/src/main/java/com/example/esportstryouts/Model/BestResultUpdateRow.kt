package com.example.esportstryouts.Model

class BestResultDBFullRow (

    val avg: Long,
    val best: Long,
    val date: String,
    val email: String,
    val game: String,
    val hash: String,
    val time: String
)

class BestResultUpdateRow (
    val allResults: ArrayList<BestResultDBFullRow>
)