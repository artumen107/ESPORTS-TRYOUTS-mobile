package com.example.esportstryouts.Fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.esportstryouts.MainActivity
import com.example.esportstryouts.Model.Response
import com.example.esportstryouts.R
import com.example.esportstryouts.ViewModels.*
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior.ScrollState

import kotlinx.coroutines.Delay

class ResultsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    lateinit var viewModelRT: ReactionTimeViewModel
    lateinit var viewModelDA: DelayAbilityViewModel
    lateinit var viewModelSC: SwitchCostViewModel
    lateinit var viewModelResults: ResultsViewModel
    lateinit var spinner: Spinner
    lateinit var layoutManager: LinearLayoutManager
    lateinit var ResultsAdapter : ResultsAdapter
    lateinit var progressBar : ProgressBar
    lateinit var recyclerView : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as MainActivity).supportActionBar?.title = "Your results"
        return inflater.inflate(R.layout.results_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = LinearLayoutManager(view.context)

        //tutaj mamy inaczej niz w MoviesViewModel
        viewModelRT = ReactionTimeViewModel()
        viewModelDA = DelayAbilityViewModel()
        viewModelSC = SwitchCostViewModel()
        viewModelResults = ResultsViewModel()
        spinner = view.findViewById(R.id.resultsFilter)
        progressBar = view.findViewById(R.id.resultsProgressBar)
        recyclerView = view.findViewById(R.id.resultsList)

//        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            override fun onScrollStateChanged(recyclerView : RecyclerView, scrollState: Int) {
//                super.onScrollStateChanged(recyclerView, scrollState)
//
//                progressBar.apply {
//                    progress = 100
//                }
//
//            }
//        })

        ResultsAdapter = ResultsAdapter(view, viewModelResults, viewModelResults._allResults, this.context)
        spinner.onItemSelectedListener = this


        viewModelResults._allResults.observe(viewLifecycleOwner) {
            ResultsAdapter.notifyDataSetChanged()
        }

        //print what we retrieved from database
        getResponseUsingLiveData()

            view.findViewById<RecyclerView>(R.id.resultsList).let {
                it.layoutManager = layoutManager
                it.adapter = ResultsAdapter
                if (spinner.selectedItem.toString().trim() == "Game")
                {
                    viewModelResults.fetchDataBaseAll("game")
                }
                else if (spinner.selectedItem.toString().trim() == "Best score")
                {
                    viewModelResults.fetchDataBaseAll("best")
                }
                else if (spinner.selectedItem.toString().trim() == "Date")
                {
                    viewModelResults.fetchDataBaseAll("date")
                }


            }

        view.findViewById<ImageButton>(R.id.resultsBackBtn).apply {
            setOnClickListener {

                val action =
                    ResultsFragmentDirections.actionResultsFragmentToMainMenuFragment()
                view.findNavController().navigate(action)
            }
        }


    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {

        //val resultsList = view.findViewById<RecyclerView>(R.id.resultsList)
        Log.i("WYBRANO TAKI ITEMEK: ", parent?.getItemAtPosition(position).toString())

        if (spinner.selectedItem.toString().trim() == "Game")
        {
            viewModelResults.fetchDataBaseAll("game")
        }
        else if (spinner.selectedItem.toString().trim() == "Best score")
        {
            viewModelResults.fetchDataBaseAll("best")
        }
        else if (spinner.selectedItem.toString().trim() == "Date")
        {
            viewModelResults.fetchDataBaseAll("date")
        }

    }
    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }

    private fun print (response: Response) {
        response.best_results_objects?.let { bestResultDBRows ->
        bestResultDBRows.forEach { bestResultDBRow ->
            bestResultDBRow.total_games.let {
                Log.i("TOTAL GAMES: ", "$it")
            }

        }
        }

        response.exception?.let { exception ->
            exception.message?.let {
                Log.e("WE GOT ERROR: ", it)
            }
        }
    }

    private fun getResponseUsingLiveData() {
        viewModelResults.getResponseUsingLiveData().observe(viewLifecycleOwner) {
            Log.i("NA POBRANEJ LISCIE?: ","$it")
        }
    }

}




