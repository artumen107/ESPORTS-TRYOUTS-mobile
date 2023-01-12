package com.example.esportstryouts

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.esportstryouts.ViewModels.DelayAbilityViewModel
import com.example.esportstryouts.ViewModels.ResultsViewModel
import com.example.esportstryouts.databinding.DelayAbilityActivityBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import java.util.*

class DelayAbilityActivity : AppCompatActivity() {

    // ViewBinding
    lateinit var binding: DelayAbilityActivityBinding

    private lateinit var viewModel: DelayAbilityViewModel
    private lateinit var viewModelResults: ResultsViewModel
    private lateinit var mainHandler : Handler
    private lateinit var trapTasksHandler : Handler
    // ### SETTINGS ###

    // min and max delay to start showing traps
    val MIN_DELAY_START = 1000L; //miliseconds
    val MAX_DELAY_START = 2000L; //miliseconds
    var startTimeTraps = 0L // random delay between MIN and MAX for traps to be started showing
    val NB_TRIES = 1 //steps during the game

    // TRAPS SETTINGS

    val TRAPS_TOTAL_MIN =  1 // minimum amount of traps that will be displayed
    val TRAPS_TOTAL_MAX =  5 // maximum amount of traps that will be displayed
    var TRAPS_TOTAL = 0 // random total amount of traps between MIN and MAX
    var CURRENT_TRAP = 0 // current trap
    val MIN_DELAY_TRAP = 1200L; // min delay before another trap/red is displayed
    val MAX_DELAY_TRAP = 2500L; // max delay before another trap/red is displayed
    var nextTrapDelay = 0L // random delay between MIN and MAX for trap to be showed
    var TRAP_ONE_COLOR = "#FFA500"
    var TRAP_TWO_COLOR = "#C71585"
    var TRAP_COLORS =  arrayOf<String> (TRAP_ONE_COLOR, TRAP_TWO_COLOR)

    // STATUS AND STATISTICS

    var CURRENT_TRY = 1;
    var GAME_STARTED = false;
    var DISPLAYED = false; // if red is displayed
    var TOTAL_TIME = 0L;
    var START_TIME = 0L;
    var BEST_TIME = 0L;
    var AVERAGE_TIME = 0L;

    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reaction_time_activity)

        viewModel = DelayAbilityViewModel()
        viewModelResults = ResultsViewModel()
        mainHandler = Handler(Looper.getMainLooper())
        trapTasksHandler = Handler(Looper.getMainLooper())
        binding = DelayAbilityActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.delayAbilityStartBtn.setOnClickListener {
            if (!GAME_STARTED) {
                startGame()
            }
            else {
                stopGame()
            }
            manageGame()
        }

        binding.delayAbilityBackBtn.setOnClickListener {
            stopGame()
            finish()
        }

    }

    override fun onPause() {
        super.onPause();

        mainHandler.removeCallbacksAndMessages(null)
        trapTasksHandler.removeCallbacksAndMessages(null)
    }


    fun startGame () {

        binding.delayAbilityResult3Tv.text = ""
        binding.delayAbilityResult2Tv.text = ""
        binding.delayAbilityResultTv.text = ""
        binding.delayAbilityStartBtn.text = "STOP"

        GAME_STARTED = true
        DISPLAYED = false
        BEST_TIME = Long.MAX_VALUE;
        TOTAL_TIME = 0
        CURRENT_TRY = 1
        binding.delayAbilityCurrentTry.text = "$CURRENT_TRY"
        // start showing traps

        startTimeTraps = randomLongBetween(MIN_DELAY_START, MAX_DELAY_START)
        println("#######################")
        println("#######################")
        println("#######################")
        println("Traps will start showing in: $startTimeTraps")
        println("#######################")
        println("#######################")
        println("#######################")


        // how many traps will be shown

        TRAPS_TOTAL = (TRAPS_TOTAL_MIN..TRAPS_TOTAL_MAX).random()

        println("#######################")
        println("How many traps will be shown?: $TRAPS_TOTAL")
        println("#######################")

        // showing TRAPS_TOTAL traps

            mainHandler.postDelayed(trapsTasks, startTimeTraps);

    }

    fun stopGame () {
        mainHandler.removeCallbacksAndMessages(null);
        trapTasksHandler.removeCallbacksAndMessages(null);

        GAME_STARTED = false
        DISPLAYED = false
        CURRENT_TRAP = 0
        binding.delayAbilityTouchArea.setBackgroundColor(Color.parseColor("#008000"))
        binding.delayAbilityStartBtn.text = "START"
        binding.delayAbilityResultTv.text = ""
        CURRENT_TRY = 0
        binding.delayAbilityCurrentTry.text = "0"
        AVERAGE_TIME = TOTAL_TIME / NB_TRIES // calculation of average time

        }

    fun finishGame () {

        // stop game
        stopGame()

        // show results
        var showAverage = StringBuilder("Average: $AVERAGE_TIME ms")
        var showBest = StringBuilder("Best time: $BEST_TIME ms")
        var showMsg = StringBuilder("Congrats! Try again!")
        binding.delayAbilityResultTv.text = showAverage
        binding.delayAbilityResult2Tv.text = showBest
        binding.delayAbilityResult3Tv.text = showMsg

        //send data to database

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)+1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val fullDate = StringBuilder("$day/$month/$year")
        val fullTime = StringBuilder("$hour:$minute")

        viewModelResults.addResult("DA",fullDate.toString(), fullTime.toString(), BEST_TIME, AVERAGE_TIME)

    }
    fun manageGame() {

        binding.delayAbilityTouchArea.setOnClickListener {
            if (GAME_STARTED) {

                // game started, we need to manage touch on the screen

                if (DISPLAYED) {

                    // delay before user touched the area

                    val delay = (System.currentTimeMillis()) - START_TIME

                    if (delay < BEST_TIME) {
                        // new best time
                        BEST_TIME = delay
                    }

                    TOTAL_TIME += delay // we add to total time
                    binding.delayAbilityTouchArea.setBackgroundColor(Color.parseColor("#008000"))
                    CURRENT_TRY++

                    val result = StringBuilder("$delay").append(" ms")
                    binding.delayAbilityResultTv.text = result
                    if (CURRENT_TRY > NB_TRIES ) {
                        // game ended
                        finishGame()


                    } else {
                        binding.delayAbilityCurrentTry.text = "$CURRENT_TRY"
                        // we plan next traps randomly appears

                        // showing TRAPS_TOTAL traps

                        TRAPS_TOTAL = (TRAPS_TOTAL_MIN..TRAPS_TOTAL_MAX).random()

                        println("#######################")
                        println("How many traps will be shown?: $TRAPS_TOTAL")
                        println("#######################")

                        startTimeTraps = randomLongBetween(MIN_DELAY_START, MAX_DELAY_START)
                        println("#######################")
                        println("#######################")
                        println("#######################")
                        println("Traps will start showing in: $startTimeTraps")
                        println("#######################")
                        println("#######################")
                        println("#######################")
                        DISPLAYED = false

                            mainHandler.postDelayed(trapsTasks, startTimeTraps);

                    }


                } else {

                    stopGame()
                    binding.delayAbilityResultTv.text = "You failed. Try Again."
                }
            }
        }
    }

    val trapsTasks = object : Runnable {
        override fun run() {
            nextTrapDelay = randomLongBetween(MIN_DELAY_TRAP, MAX_DELAY_TRAP)

            trapTasksHandler.postDelayed(bgColorTaskTraps, nextTrapDelay)

            CURRENT_TRAP+=1
            if (CURRENT_TRAP <= TRAPS_TOTAL) {
                println("NASTEPNY TRAP POJAWI SIE ZA: $nextTrapDelay")
                mainHandler.postDelayed(this, nextTrapDelay)
                println("ROBI SIE ROBI SIE TRAP")
            }
            else {
                mainHandler.removeCallbacksAndMessages(null)
                println("CZERWONE POJAWI SIE ZA: $nextTrapDelay")
                mainHandler.postDelayed(bgColorTaskRed, nextTrapDelay)
                CURRENT_TRAP = 0
            }
        }
    }


        val bgColorTaskTraps = Runnable {
                val randomColor = TRAP_COLORS.random()
                binding.delayAbilityTouchArea.setBackgroundColor(Color.parseColor(randomColor))
            }


    val bgColorTaskRed = Runnable {
        println("CZERWONE UWAGA")
        binding.delayAbilityTouchArea.setBackgroundColor(Color.RED)
        START_TIME = System.currentTimeMillis()
        DISPLAYED = true // RED is displayed, touch it!
    }

    fun randomLongBetween(origin : Long, bound : Long): Long {
        return origin + (Math.random() * (bound - origin)).toLong()
    }

}