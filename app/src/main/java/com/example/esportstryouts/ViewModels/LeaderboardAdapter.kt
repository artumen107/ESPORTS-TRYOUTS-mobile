package com.example.esportstryouts.ViewModels

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.esportstryouts.Model.FriendsAllDB
import com.example.esportstryouts.Model.ReactionTimeDBRow
import com.example.esportstryouts.Model.ResultsDBRow
import com.example.esportstryouts.R


class LeaderboardAdapter(private val view: View,
                         private val viewModel: LeaderboardViewModel,
                         private val allFriends: LiveData<List<FriendsAllDB>>,
                         private val context: Context?)
    : RecyclerView.Adapter<LeaderboardAdapter.FriendsHolder>() {

    inner class FriendsHolder(view: View) : RecyclerView.ViewHolder(view) {
        val friendCustomName: TextView = view.findViewById(R.id.leaderboardCustomName)
        val friendRemove: ImageButton = view.findViewById(R.id.removeFriend)

        val friendAvgResultRT: TextView = view.findViewById(R.id.leaderboardRTAvg)
        val friendAvgResultDA: TextView = view.findViewById(R.id.leaderboardDAAvg)
        val friendAvgResultSC: TextView = view.findViewById(R.id.leaderboardSCAvg)

        val friendBestResultRT: TextView = view.findViewById(R.id.leaderboardRTBest)
        val friendBestResultDA: TextView = view.findViewById(R.id.leaderboardDABest)
        val friendBestResultSC: TextView = view.findViewById(R.id.leaderboardSCBest)

        val friendPosition: TextView = view.findViewById(R.id.leaderboardPosition)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_friend_new, parent, false)
        return FriendsHolder(view)
    }

    override fun onBindViewHolder(holder: FriendsHolder, position: Int) {

        holder.friendCustomName.text =
            viewModel.allFriends.value?.get(position)?.custom_name.toString()
        // IF game == RT... IF game == SC...

        var lbReactionTimeAvg = viewModel.allFriends.value?.get(position)?.Best_Results?.Reaction_Time?.avg.toString()
        var lbDelayAbilityAvg = viewModel.allFriends.value?.get(position)?.Best_Results?.Delay_Ability?.avg.toString()
        var lbSwitchCostAvg = viewModel.allFriends.value?.get(position)?.Best_Results?.Switch_Cost?.avg.toString()

        var lbReactionTimeBest = viewModel.allFriends.value?.get(position)?.Best_Results?.Reaction_Time?.best.toString()
        var lbDelayAbilityBest = viewModel.allFriends.value?.get(position)?.Best_Results?.Delay_Ability?.best.toString()
        var lbSwitchCostBest = viewModel.allFriends.value?.get(position)?.Best_Results?.Switch_Cost?.best.toString()

        holder.friendAvgResultRT.text = lbReactionTimeAvg
        holder.friendAvgResultDA.text = lbDelayAbilityAvg
        holder.friendAvgResultSC.text = lbSwitchCostAvg

        holder.friendBestResultRT.text = lbReactionTimeBest
        holder.friendBestResultDA.text = lbDelayAbilityBest
        holder.friendBestResultSC.text = lbSwitchCostBest

        val leaderboardPosition = position +1
        holder.friendPosition.text =
            leaderboardPosition.toString()

        holder.friendRemove.setOnClickListener {
            val emailClear = viewModel.allFriends.value?.get(position)?.email.toString().replace("@", "").replace(".", "")
           viewModel.removeFriend(emailClear)
            Log.i("USUNIETO:" , emailClear)
        }

        // funkcje mutablelist: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/

//        if (viewModel.allResults.value?.get(position)?.game.toString() == "SC")
//        {
//            holder.resultLogo.setImageResource(R.drawable.switch_cost_icon)
//        }



    }

    override fun getItemCount(): Int {
        return allFriends.value?.size ?: 0
    }

}