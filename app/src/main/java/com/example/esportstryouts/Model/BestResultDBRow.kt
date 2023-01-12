package com.example.esportstryouts.Model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@IgnoreExtraProperties
data class BestResultDBRow (
    var avg: Long = 0L,
    var best: Long = 0L,
    var date: String = "",
    var time: String = "",
    var total_games: Int = 0
) {
    constructor() : this(0, 0, "", "") {}
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "avg" to avg,
            "best" to best,
            "date" to date,
            "time" to time,
           "total_games" to total_games,
        )
    }
}

data class Response (
    var best_results_objects: List<BestResultDBRow>? = null,
    var exception: Exception? = null
        )

//data class Response (
//    var best_results_objects: List<BestResultDBRow>? = null,
//    var exception: Exception? = null
//)



