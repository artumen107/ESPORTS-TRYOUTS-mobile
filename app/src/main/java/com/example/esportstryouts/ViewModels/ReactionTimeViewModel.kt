package com.example.esportstryouts.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.esportstryouts.Model.DataBaseRepository
import com.example.esportstryouts.Model.DelayAbilityDBRow
import com.example.esportstryouts.Model.ReactionTimeDBRow
import com.example.esportstryouts.Model.SwitchCostDBRow
import com.google.firebase.auth.FirebaseAuth

class ReactionTimeViewModel : ViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val repository = DataBaseRepository()
    val _reactionTimeResults = MutableLiveData<List<ReactionTimeDBRow>>()

    val reactionTimeResults: LiveData<List<ReactionTimeDBRow>> = _reactionTimeResults

//    fun addRTResult(date: String, time: String, best: Long, avg: Long) {
//        fetchDataBaseRT()
//
//        val email = firebaseAuth.currentUser?.email.toString()
//        val emailClear = email.replace("@", "").replace(".", "")
//        val hashCode = repository.databaseReferenceRT.push().key
//        val newRTResult = ReactionTimeDBRow(hashCode.toString(), date, time, best, email, avg)
//        repository.databaseReferenceRT.child(hashCode.toString()).setValue(newRTResult)
//        Log.i("TAKI HASHCODE: ", hashCode.toString())
//    }
//
//
//    fun fetchDataBaseRT() {
//        repository.fetchDataBaseRT(_reactionTimeResults)
//    }


}
