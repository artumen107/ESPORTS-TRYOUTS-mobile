package com.example.esportstryouts.Model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
data class FriendsDBRow (
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

data class FriendsAllDB(
    var friends_all_db: ArrayList<FriendMailDB>? = null,
    var custom_name : String = "cntest",
    var email: String = "emailtest",
    var Best_Results : FriendBestResultsRoot? = null,
    var Reaction_Time: BestResultDBRow? = null,
    var Switch_Cost: BestResultDBRow? = null,
    var Delay_Ability: BestResultDBRow? = null,
        )
data class FriendMailDB (
    var custom_name: String = "",
    var email: String = "",
    var best_results_root: FriendBestResultsRoot? = null
        )

data class FriendBestResultsRoot (
    var best_results_objects: ArrayList<FriendsDBRow>? = null,
    var Best_Results : FriendBestResultsRoot? = null,
    var Reaction_Time: BestResultDBRow? = null,
    var Switch_Cost: BestResultDBRow? = null,
    var Delay_Ability: BestResultDBRow? = null,
        )