package com.example.esportstryouts.ViewModels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.esportstryouts.Model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ResultsViewModel : ViewModel() {
    private val fireBase = FirebaseDatabase.getInstance()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val repository = DataBaseRepository()
    var viewModelRT: ReactionTimeViewModel = ReactionTimeViewModel()
    var viewModelSC: SwitchCostViewModel = SwitchCostViewModel()
    var viewModelDA: DelayAbilityViewModel = DelayAbilityViewModel()

    val _allResults = MutableLiveData<List<ResultsDBRow>>()
    val allResults: LiveData<List<ResultsDBRow>> = _allResults
    var databaseReferenceBestResults = fireBase.getReference("Users/${firebaseAuth.currentUser?.email.toString()
        .replace("@", "").replace(".", "")}/Best_Results")
    var databaseReferenceBestResultsRT = fireBase.getReference("Users/${firebaseAuth.currentUser?.email.toString()
        .replace("@", "").replace(".", "")}/Best_Results/Reaction_Time")
    var databaseReferenceBestResultsSC = fireBase.getReference("Users/${firebaseAuth.currentUser?.email.toString()
        .replace("@", "").replace(".", "")}/Best_Results/Switch_Cost")
    var databaseReferenceBestResultsDA = fireBase.getReference("Users/${firebaseAuth.currentUser?.email.toString()
        .replace("@", "").replace(".", "")}/Best_Results/Delay_Ability")


    fun addResult(game: String, date: String, time: String, best: Long, avg: Long) {

        val email = firebaseAuth.currentUser?.email.toString()
        val emailClear = email.replace("@", "").replace(".", "")
        val hashCode = repository.databaseReferenceResults.push().key
        val newResult = ResultsDBRow(game, hashCode.toString(), date, time, best, email, avg)
        repository.databaseReferenceResults.child(hashCode.toString()).setValue(newResult)
        Log.i("HASHCODE OF GAME: ", hashCode.toString())
        repository.updateBestResults(game, best, avg, date, time)
    }


    fun fetchDataBaseAll(sort: String) {
        repository.fetchDataBaseResults(_allResults, sort)
    }


    fun getResponseUsingLiveData() : LiveData<Response> {
        return repository.getResponseFromRealtimeDatabaseUsingLiveData()
    }


}
