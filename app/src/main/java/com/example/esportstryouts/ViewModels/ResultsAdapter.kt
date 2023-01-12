package com.example.esportstryouts.ViewModels

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.setPadding
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.esportstryouts.Model.ReactionTimeDBRow
import com.example.esportstryouts.Model.ResultsDBRow
import com.example.esportstryouts.R


class ResultsAdapter(private val view: View,
                     private val viewModel: ResultsViewModel,
                     private val allResults: LiveData<List<ResultsDBRow>>,
                     private val context: Context?)
    : RecyclerView.Adapter<ResultsAdapter.ResultsHolder>() {

    inner class ResultsHolder(view: View) : RecyclerView.ViewHolder(view) {
        val resultFullDate: TextView = view.findViewById(R.id.resultDateTv)
        val resultBest: TextView = view.findViewById(R.id.resultBestTv)
        val resultLogo: ImageView = view.findViewById(R.id.resultGameIv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_result, parent, false)
        return ResultsHolder(view)
    }

    override fun onBindViewHolder(holder: ResultsHolder, position: Int) {

        holder.resultFullDate.text =
            viewModel.allResults.value?.get(position)?.date.toString()
        holder.resultBest.text = StringBuilder(viewModel.allResults.value?.get(position)?.best.toString() + " ms")


        if (viewModel.allResults.value?.get(position)?.game.toString() == "RT")
        {
            holder.resultLogo.setImageResource(R.drawable.reaction_time_icon)
        }
        else if (viewModel.allResults.value?.get(position)?.game.toString() == "SC")
        {
            holder.resultLogo.setImageResource(R.drawable.switch_cost_icon)
        }
        else if (viewModel.allResults.value?.get(position)?.game.toString() == "DA")
        {
            holder.resultLogo.setPadding(2)
            holder.resultLogo.setImageResource(R.drawable.delay_ability_icon)

        }


    }

    override fun getItemCount(): Int {
        return allResults.value?.size ?: 0
    }

}