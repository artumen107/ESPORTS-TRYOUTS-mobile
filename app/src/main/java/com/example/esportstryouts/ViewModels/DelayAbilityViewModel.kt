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

class DelayAbilityViewModel : ViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val repository = DataBaseRepository()
    val _delayAbilityResults = MutableLiveData<List<DelayAbilityDBRow>>()


    val delayAbilityResults: LiveData<List<DelayAbilityDBRow>> = _delayAbilityResults

//    fun addDAResult(date: String, time: String, best: Long, avg: Long) {
//
//        val email = firebaseAuth.currentUser?.email.toString()
//        val emailClear = email.replace("@", "").replace(".", "")
//        val hashCode = repository.databaseReferenceDA.push().key
//        val newDAResult = DelayAbilityDBRow(hashCode.toString(), date, time, best, email, avg)
//        repository.databaseReferenceDA.child(hashCode.toString()).setValue(newDAResult)
//        Log.i("TAKI HASHCODE: ", hashCode.toString())
//
//    }
//
//
//    fun fetchDataBaseDA() {
//        repository.fetchDataBaseDA(_delayAbilityResults)
//    }

}
