package com.example.esportstryouts

import android.graphics.Color
import android.icu.text.DateFormat.DAY
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.esportstryouts.Model.BestResultDBRow
import com.example.esportstryouts.ViewModels.ReactionTimeViewModel
import com.example.esportstryouts.ViewModels.ResultsViewModel
import com.example.esportstryouts.databinding.ReactionTimeActivityBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.text.DateFormat.getDateInstance
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ReactionTimeActivity : AppCompatActivity() {

    // ViewBinding
    lateinit var binding: ReactionTimeActivityBinding
    lateinit var viewModel: ReactionTimeViewModel
    lateinit var viewModelResults: ResultsViewModel
    private lateinit var mainHandler : Handler

    // SETTINGS


    val MIN_DELAY = 1500L; //miliseconds
    val MAX_DELAY = 5000L; //miliseconds
    val TOTAL_TRIES = 1//steps during the game
    var nextRedTime = 0L

    // STATUS AND STATISTICS

    var CURRENT_TRY = 1;
    var GAME_STARTED = false;
    var DISPLAYED = false;
    var TOTAL_TIME = 0L;
    var START_TIME = 0L;
    var BEST_TIME = 0L;
    var AVERAGE_TIME = 0L;


    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reaction_time_activity)

       mainHandler = Handler(Looper.getMainLooper())
        viewModel = ReactionTimeViewModel()
        viewModelResults = ResultsViewModel()
       binding = ReactionTimeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.reactionTimeStartBtn.setOnClickListener {
            if (!GAME_STARTED) {
                startGame()
            }
            else {
                stopGame()
            }
            manageGame()
        }

        binding.reactionTimeBackBtn.setOnClickListener {
            stopGame()
          finish()
        }

    }

    override fun onPause() {
        super.onPause();

        mainHandler.removeCallbacksAndMessages(null)

    }


    fun startGame () {
        binding.reactionTimeResult3Tv.text = ""
        binding.reactionTimeResult2Tv.text = ""
        binding.reactionTimeResultTv.text = ""
        binding.reactionTimeStartBtn.text = "STOP"
        GAME_STARTED = true
        DISPLAYED = false
        BEST_TIME = Long.MAX_VALUE;
        TOTAL_TIME = 0
        CURRENT_TRY = 1
        binding.reactionTimeCurrentTry.text = "$CURRENT_TRY"
        // change the background color in a random time between MIN_DELAY and MAX_DELAY
        nextRedTime = randomLongBetween(MIN_DELAY, MAX_DELAY)
        println("#######################")
        println("#######################")
        println("#######################")
        println("Next read appears in: $nextRedTime")
        println("#######################")
        println("#######################")
        println("#######################")
        mainHandler.postDelayed(bgColorTask, nextRedTime)
    }

    fun stopGame () {
        mainHandler.removeCallbacks(bgColorTask)
        GAME_STARTED = false
        DISPLAYED = false
        binding.reactionTimeTouchArea.setBackgroundColor(Color.parseColor("#008000"))
        binding.reactionTimeStartBtn.text = "START"
        binding.reactionTimeResultTv.text = ""
        CURRENT_TRY = 0
        binding.reactionTimeCurrentTry.text = "0"
        AVERAGE_TIME = TOTAL_TIME / TOTAL_TRIES // calculation of average time

    }

    fun finishGame () {

        // stop game
        mainHandler.removeCallbacks(bgColorTask)
        GAME_STARTED = false
        binding.reactionTimeStartBtn.text = "START"
        binding.reactionTimeResultTv.text = ""
        CURRENT_TRY = 0
        binding.reactionTimeCurrentTry.text = "0"
        AVERAGE_TIME = TOTAL_TIME / TOTAL_TRIES // calculation of average time

        // show results
        var showAverage = StringBuilder("Average: $AVERAGE_TIME ms")
        var showBest = StringBuilder("Best time: $BEST_TIME ms")
        var showMsg = StringBuilder("Congrats! Try again!")
        binding.reactionTimeResultTv.text = showAverage
        binding.reactionTimeResult2Tv.text = showBest
        binding.reactionTimeResult3Tv.text = showMsg

        //send data to database
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)+1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val fullDate = StringBuilder("$day/$month/$year")
        val fullTime = StringBuilder("$hour:$minute")


        viewModelResults.addResult("RT",fullDate.toString(), fullTime.toString(), BEST_TIME, AVERAGE_TIME)

        //viewModel.addRTResult("DATA2", "08.12.2022", BEST_TIME, AVERAGE_TIME)

    }
    fun manageGame() {

        binding.reactionTimeTouchArea.setOnClickListener {
            if (GAME_STARTED) {

                // game started, we need to manage touch on the screen

                if (DISPLAYED) {

                    // delay before user touched the area

                    var delay = (System.currentTimeMillis()) - START_TIME

                    if (delay < BEST_TIME) {
                        // new best time
                        BEST_TIME = delay
                    }

                    TOTAL_TIME += delay // we add to total time
                    binding.reactionTimeTouchArea.setBackgroundColor(Color.parseColor("#008000"))
                    CURRENT_TRY++

                    val result = StringBuilder("$delay").append(" ms")
                    binding.reactionTimeResultTv.text = result
                    if (CURRENT_TRY > TOTAL_TRIES ) {
                        // game ended
                        finishGame()


                    } else {
                        binding.reactionTimeCurrentTry.text = "$CURRENT_TRY"
                        // we plan next red box randomly appears
                        nextRedTime = randomLongBetween(MIN_DELAY, MAX_DELAY)
                        println("#######################")
                        println("#######################")
                        println("#######################")
                        println("Next read appears in: $nextRedTime")
                        println("#######################")
                        println("#######################")
                        println("#######################")
                        DISPLAYED = false
                        mainHandler.postDelayed(bgColorTask, nextRedTime);
                    }


                } else {

                   stopGame()
                    binding.reactionTimeResultTv.text = "You failed. Try Again."
                }
            }
        }
    }


    val bgColorTask = object : Runnable {
        override fun run () {
            binding.reactionTimeTouchArea.setBackgroundColor(Color.RED)
            START_TIME = System.currentTimeMillis()
            DISPLAYED = true // RED is displayed, touch it!
            //mainHandler.removeCallbacksAndMessages(null)

        }
    }

    fun randomLongBetween(origin : Long, bound : Long): Long {
        return origin + (Math.random() * (bound - origin)).toLong()
    }



    }
