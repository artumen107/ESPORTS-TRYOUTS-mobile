package com.example.esportstryouts.Model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.esportstryouts.ViewModels.ResultsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataBaseRepository {


    private val fireBase = FirebaseDatabase.getInstance()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
   // val viewModel = ResultsViewModel()
    val databaseReferenceResults = fireBase.getReference("All_Results/${firebaseAuth.currentUser?.email.toString()
        .replace("@", "").replace(".", "")}")
    val databaseReferenceUsers = fireBase.getReference("Users/")
    val databaseReferenceBestResults = fireBase.getReference("Users/${firebaseAuth.currentUser?.email.toString()
        .replace("@", "").replace(".", "")}/Best_Results")
    val databaseReferenceFriends = fireBase.getReference("Users/${firebaseAuth.currentUser?.email.toString()
        .replace("@", "").replace(".", "")}/Friends/")

//lista przyjaciol, foreach
    lateinit var databaseReferenceBestResultsGame : DatabaseReference
    var length : Int = 0;


    fun updateBestResults (
        game: String, best: Long,
        avg: Long, date: String, time: String
    ) {

        val email = firebaseAuth.currentUser?.email.toString().trim()
        val emailClear = email.replace("@", "").replace(".", "").trim()
        var total_games = 0

        if (game == "RT") {
            databaseReferenceBestResultsGame = fireBase.getReference("Users/${firebaseAuth.currentUser?.email.toString()
                .replace("@", "").replace(".", "")}/Best_Results/Reaction_Time")
            ResultsDBRow.counterRT +=1
            total_games = ResultsDBRow.counterRT
        }
        else if (game == "SC") {
            databaseReferenceBestResultsGame = fireBase.getReference("Users/${firebaseAuth.currentUser?.email.toString()
                .replace("@", "").replace(".", "")}/Best_Results/Switch_Cost")
            ResultsDBRow.counterSC +=1
            total_games = ResultsDBRow.counterSC
        }
        else if (game == "DA") {
            databaseReferenceBestResultsGame = fireBase.getReference("Users/${firebaseAuth.currentUser?.email.toString()
                .replace("@", "").replace(".", "")}/Best_Results/Delay_Ability")
            ResultsDBRow.counterDA +=1
            total_games = ResultsDBRow.counterDA
        }

        databaseReferenceBestResultsGame.
        addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("SNAPSHOT JEST TAKI", snapshot.value.toString())


                var currentBestResult = snapshot.getValue(BestResultDBRow::class.java)
                if (currentBestResult != null) {
                    Log.i("Current best result: ", currentBestResult.best.toString())
                    if (best < currentBestResult.best) {
                        databaseReferenceBestResultsGame.setValue(BestResultDBRow(avg, best, date, time, total_games))
                        Log.i("New record found!", "Saved: $best")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            } })

        val update = mapOf("total_games" to total_games)
        databaseReferenceBestResultsGame.updateChildren(update)
            .addOnSuccessListener {
                Log.i("update success!", "total games updated successfully")
            }
            .addOnFailureListener {
                Log.i("unlucky!", "total games updated successfully")
            }
    }



    fun fetchDataBaseResults (liveData: MutableLiveData<List<ResultsDBRow>>, sortFilter : String) {
        databaseReferenceResults.
        orderByChild(sortFilter).
        addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("SNAPSHOT", snapshot.value.toString())

                val allResults: List<ResultsDBRow> = snapshot.children.map {
                        dataSnapshot ->
                    dataSnapshot.getValue(ResultsDBRow::class.java)!!
                }

                liveData.postValue(allResults)
                length = allResults.size
                Log.i("ALL ITEMS", allResults.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            } })
    }

    fun fetchDataBaseFriends (liveData: MutableLiveData<List<FriendsAllDB>>) {
        databaseReferenceFriends.
        addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("SNAPSHOT", snapshot.value.toString())

                val allFriends: List<FriendsAllDB> = snapshot.children.map {
                        dataSnapshot ->
                    dataSnapshot.getValue(FriendsAllDB::class.java)!!
                }

                liveData.postValue(allFriends)
                length = allFriends.size
                Log.i("ALL ITEMS", allFriends.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            } })
    }

    fun addFriend (emailFriend: String, customNameFriend: String) {

        val email = firebaseAuth.currentUser?.email.toString()
        val emailClear = email.replace("@", "").replace(".", "")
        //val hashCode = repository.databaseReferenceUsers.push().key
        val newFriend = NewUserDBRow(emailFriend, customNameFriend)
        val newDefaultResult = DefaultGamesResults(9999, 9999, "00.00.00", "00:00", 0)
        val emailFriendClear = emailFriend.replace("@", "").replace(".", "")
        Log.i("TEST FRIEND USER: ", databaseReferenceUsers.child(emailFriendClear).toString())
        databaseReferenceUsers.child(emailClear).child("Friends").child(emailFriendClear).setValue(newFriend)


        databaseReferenceUsers.child(emailFriendClear).child("Best_Results").
        addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("TEST IF USER EXISTS", snapshot.value.toString())

                if (snapshot.exists()) {
                    Log.i("USER EXISTS!", "I ADD NEW SCORE")
                    val friendsBestResults: List<BestResultDBRow> = snapshot.children.map {
                            dataSnapshot ->
                        dataSnapshot.getValue(BestResultDBRow::class.java)!!
                    }
                    databaseReferenceUsers.child(emailClear).child("Friends").child(emailFriendClear).child("Best_Results").child("Delay_Ability").setValue(friendsBestResults[0])
                    databaseReferenceUsers.child(emailClear).child("Friends").child(emailFriendClear).child("Best_Results").child("Reaction_Time").setValue(friendsBestResults[1])
                    databaseReferenceUsers.child(emailClear).child("Friends").child(emailFriendClear).child("Best_Results").child("Switch_Cost").setValue(friendsBestResults[2])
                }
                else {
                    Log.i("USER DOESN'T EXIST", "I ADD DEFAULT SCORE")
                    databaseReferenceUsers.child(emailClear).child("Friends").child(emailFriendClear).child("Best_Results").child("Delay_Ability").setValue(newDefaultResult)
                    databaseReferenceUsers.child(emailClear).child("Friends").child(emailFriendClear).child("Best_Results").child("Reaction_Time").setValue(newDefaultResult)
                    databaseReferenceUsers.child(emailClear).child("Friends").child(emailFriendClear).child("Best_Results").child("Switch_Cost").setValue(newDefaultResult)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            } })

    }


    fun removeFriend (email: String) {
        databaseReferenceFriends.child(email).removeValue()
    }

    fun getResponseFromRealtimeDatabaseUsingLiveData() : MutableLiveData<Response> {
        val mutableLiveData = MutableLiveData<Response>()

        databaseReferenceBestResults.get().addOnCompleteListener { task ->
            val response = Response()
            if (task.isSuccessful) {
                val result = task.result
                result?.let {
                    response.best_results_objects = result.children.map { snapShot ->
                        snapShot.getValue(BestResultDBRow::class.java)!!
                    }
                }
                if (response.best_results_objects?.size!! >0)
                {
                    ResultsDBRow.counterDA = response.best_results_objects?.get(0)?.total_games!!
                    ResultsDBRow.counterRT = response.best_results_objects?.get(1)?.total_games!!
                    ResultsDBRow.counterSC = response.best_results_objects?.get(2)?.total_games!!
                }

            } else {
                response.exception = task.exception
            }
            mutableLiveData.value = response

        }
        return mutableLiveData
    }

}