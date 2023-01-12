package com.example.esportstryouts.Model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ResultsDBRow (
    val game: String = "",
    val hash: String = "",
    val date: String = "",
    val time: String = "",
    val best: Long = 0L,
    val email: String = "",
    val avg: Long = 0L,
) {


    companion object {

        var counterRT: Int = 0
        var counterDA: Int = 0
        var counterSC: Int = 0

        fun counterRT(): Int {
            return counterRT
        }
        fun counterDA(): Int {
            return counterDA
        }
        fun counterSC(): Int {
            return counterSC
        }

}}


