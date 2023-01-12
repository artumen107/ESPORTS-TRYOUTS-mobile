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

class SwitchCostViewModel : ViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val repository = DataBaseRepository()

    val _switchCostResults = MutableLiveData<List<SwitchCostDBRow>>()


    val switchCostResults: LiveData<List<SwitchCostDBRow>> = _switchCostResults

//    fun addSCResult(date: String, time: String, best: Long, avg: Long) {
//        fetchDataBaseSC()
//
//        val email = firebaseAuth.currentUser?.email.toString()
//        val emailClear = email.replace("@", "").replace(".", "")
//        val hashCode = repository.databaseReferenceSC.push().key
//        val newSCResult = SwitchCostDBRow(hashCode.toString(), date, time, best, email, avg)
//        repository.databaseReferenceSC.child(hashCode.toString()).setValue(newSCResult)
//        Log.i("TAKI HASHCODE: ", hashCode.toString())
//    }
//
//    fun fetchDataBaseSC() {
//        repository.fetchDataBaseSC(_switchCostResults)
//    }

}
