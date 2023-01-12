package com.example.esportstryouts.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.esportstryouts.Model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class LeaderboardViewModel : ViewModel() {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val repository = DataBaseRepository()
    var viewModelRT: ReactionTimeViewModel = ReactionTimeViewModel()
    var viewModelSC: SwitchCostViewModel = SwitchCostViewModel()
    var viewModelDA: DelayAbilityViewModel = DelayAbilityViewModel()

    val _allFriends = MutableLiveData<List<FriendsAllDB>>()
    val allFriends: LiveData<List<FriendsAllDB>> = _allFriends



    val friendsUpdating = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            val post = dataSnapshot.getValue<BestResultDBRow>()
            val postedAVG = post?.avg
            val postedBEST = post?.best
            val postedDATE = post?.date
            val postedTIME = post?.time

            Log.i("ZMIENIONO I POBRANO: ", post.toString())
            Log.i("AKTUALIZACJA: ", "ZMIENIONO I POBRANO: $postedAVG, $postedBEST, $postedDATE, $postedTIME")
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w("FIREBASE", "loadPost:onCancelled", databaseError.toException())
        }

        // jeśli jest

    }


    fun addFriend(emailFriend: String, customName: String) {
        repository.addFriend(emailFriend, customName)
    }

    fun removeFriend(email: String) {
        repository.removeFriend(email)
    }
    fun fetchDataBaseFriends() {
        repository.fetchDataBaseFriends(_allFriends)
    }


}
