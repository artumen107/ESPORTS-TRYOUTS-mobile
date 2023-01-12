package com.example.esportstryouts

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.esportstryouts.ViewModels.ReactionTimeViewModel
import com.example.esportstryouts.ViewModels.ResultsViewModel
import com.example.esportstryouts.ViewModels.SwitchCostViewModel
import com.example.esportstryouts.databinding.SwitchCostActivityBinding
import java.util.*

class SwitchCostActivity : AppCompatActivity() {

    // ViewBinding
    lateinit var binding: SwitchCostActivityBinding
    lateinit var viewModel: SwitchCostViewModel
    lateinit var viewModelResults: ResultsViewModel
    private lateinit var mainHandler : Handler
    private lateinit var trapTasksHandler : Handler
    // ### SETTINGS ###

    // min and max delay to start showing traps

    private val MIN_DELAY = 1500L; //milliseconds
    private val MAX_DELAY = 5000L; //milliseconds
    private val TOTAL_TRIES = 1 //total amount of rounds during the game
    private var nextPatternShowDelay = 0L


    // STATUS AND STATISTICS

    var CURRENT_TRY = 1;
    var GAME_STARTED = false;
    var DISPLAYED = false; // if red is displayed
    var DISPLAYED_RECTANGLE = false
    var DISPLAYED_SQUARE = false
    var TOTAL_TIME = 0L;
    var START_TIME = 0L;
    var BEST_TIME = 0L;
    var AVERAGE_TIME = 0L;
    var RANDOM_PATTERN = "pattern_rectangle_r1"
    var PATTERN_ID = 0
    var ALL_PATTERNS = arrayOf<String> ("rectangle_r1", "rectangle_r2", "rectangle_r3",
        "rectangle_r4", "rectangle_r5", "rectangle_s1", "rectangle_s2s3",
        "rectangle_s4", "rectangle_s5", "rectangle_s6", "rectangle_s7",
        "square_r1", "square_r2", "square_r3",  "square_r4", "square_s1", "square_s2s3")
    var ALL_ANSWERS = arrayOf<String> ("rectangle", "square")
    var ANSWER_LEFT = "rectangle"
    var ANSWER_RIGHT = "square"
    var DISPLAYED_PATTERN = "rectangle"

    //

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reaction_time_activity)

        mainHandler = Handler(Looper.getMainLooper())
        viewModel = SwitchCostViewModel()
        viewModelResults = ResultsViewModel()
        trapTasksHandler = Handler(Looper.getMainLooper())
        binding = SwitchCostActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.switchCostStartBtn.setOnClickListener {
            if (!GAME_STARTED) {
                startGame()
            }
            else {
                stopGame()
            }
            manageGame()
        }

        binding.switchCostBackBtn.setOnClickListener {
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
        binding.switchCostResult3Tv.text = ""
        binding.switchCostResult2Tv.text = ""
        binding.switchCostResultTv.text = ""
        binding.switchCostStartBtn.text = getString(R.string.stopmsg)
        binding.switchCostAnswerLeftImg.setImageResource(R.color.white)
        binding.switchCostAnswerRightImg.setImageResource(R.color.white)
        binding.switchCostPatternArea.setImageResource(R.color.white)
        GAME_STARTED = true
        DISPLAYED = false
        DISPLAYED_SQUARE = false
        DISPLAYED_RECTANGLE = false
        BEST_TIME = Long.MAX_VALUE;
        TOTAL_TIME = 0
        CURRENT_TRY = 1
        binding.switchCostCurrentTry.text = "$CURRENT_TRY"

        // show pattern in a random time between MIN_DELAY and MAX_DELAY

        nextPatternShowDelay = randomLongBetween(MIN_DELAY, MAX_DELAY)
        println("#######################")
        println("#######################")
        println("#######################")
        println("Next pattern appears in: $nextPatternShowDelay")
        println("#######################")
        println("#######################")
        println("#######################")
        mainHandler.postDelayed(patternChangeTask, nextPatternShowDelay)

    }

    fun stopGame () {
        mainHandler.removeCallbacksAndMessages(null);
        trapTasksHandler.removeCallbacksAndMessages(null);
        GAME_STARTED = false
        DISPLAYED = false
        DISPLAYED_SQUARE = false
        DISPLAYED_RECTANGLE = false
        binding.switchCostStartBtn.text = getString(R.string.startmsg)
        binding.switchCostResultTv.text = ""
        CURRENT_TRY = 0
        binding.switchCostCurrentTry.text = "0"
        binding.switchCostAnswerLeftImg.setImageResource(R.color.White)
        binding.switchCostAnswerRightImg.setImageResource(R.color.White)
        binding.switchCostPatternArea.setImageResource(R.color.White)
        AVERAGE_TIME = TOTAL_TIME / TOTAL_TRIES // calculation of average time

    }

    fun finishGame () {

        // stop game
        stopGame()

        // show results
        var showAverage = StringBuilder("AVG: $AVERAGE_TIME ms")
        var showBest = StringBuilder("BEST: $BEST_TIME ms")
        var showMsg = StringBuilder("Good job! Try again?")
        binding.switchCostResultTv.text = showMsg
        binding.switchCostResult2Tv.text = showBest
        binding.switchCostResult3Tv.text = showAverage

        //send data to database

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)+1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val fullDate = StringBuilder("$day/$month/$year")
        val fullTime = StringBuilder("$hour:$minute")

        viewModelResults.addResult("SC",fullDate.toString(), fullTime.toString(), BEST_TIME, AVERAGE_TIME)
    }
    fun manageGame() {

        binding.switchCostAnswerLeft.setOnClickListener{
            if (GAME_STARTED) {

                // game started, we need to manage touch on the screen

                if (DISPLAYED) {

                    if ((ANSWER_LEFT == "rectangle" && DISPLAYED_PATTERN == "rectangle") || (ANSWER_LEFT == "square" && DISPLAYED_PATTERN == "square")) {
                        // delay before user touched the area

                        val delay = (System.currentTimeMillis()) - START_TIME

                        if (delay < BEST_TIME) {
                            // new best time
                            BEST_TIME = delay
                        }

                        TOTAL_TIME += delay // we add to total time
                        CURRENT_TRY++

                        val result = StringBuilder("$delay").append(" ms")
                        binding.switchCostResultTv.text = result
                        binding.switchCostAnswerLeftImg.setImageResource(R.color.white)
                        binding.switchCostAnswerRightImg.setImageResource(R.color.white)
                        binding.switchCostPatternArea.setImageResource(R.color.white)
                        if (CURRENT_TRY > TOTAL_TRIES ) {
                            // game ended
                            finishGame()
                    } else {

                            binding.switchCostCurrentTry.text = "$CURRENT_TRY"
                            // we plan next pattern randomly appears
                            nextPatternShowDelay = randomLongBetween(MIN_DELAY, MAX_DELAY)
                            println("#######################")
                            println("#######################")
                            println("#######################")
                            println("Next pattern appears in: $nextPatternShowDelay")
                            println("#######################")
                            println("#######################")
                            println("#######################")
                            DISPLAYED = false
                            mainHandler.postDelayed(patternChangeTask, nextPatternShowDelay);
                        }


                    } else {

                        stopGame()
                        binding.switchCostResultTv.text = "You failed. Try Again."
                    }


                }
            }
        }

        binding.switchCostAnswerRight.setOnClickListener{
            if (GAME_STARTED) {

                // game started, we need to manage touch on the screen

                if (DISPLAYED) {

                    if ((ANSWER_RIGHT == "rectangle" && DISPLAYED_PATTERN == "rectangle") || (ANSWER_RIGHT == "square" && DISPLAYED_PATTERN == "square")) {
                        // delay before user touched the area

                        val delay = (System.currentTimeMillis()) - START_TIME

                        if (delay < BEST_TIME) {
                            // new best time
                            BEST_TIME = delay
                        }

                        TOTAL_TIME += delay // we add to total time
                        CURRENT_TRY++

                        val result = StringBuilder("$delay").append(" ms")
                        binding.switchCostResultTv.text = result
                        binding.switchCostAnswerLeftImg.setImageResource(R.color.white)
                        binding.switchCostAnswerRightImg.setImageResource(R.color.white)
                        binding.switchCostPatternArea.setImageResource(R.color.white)
                        if (CURRENT_TRY > TOTAL_TRIES ) {
                            // game ended
                            finishGame()
                        } else {
                            binding.switchCostCurrentTry.text = "$CURRENT_TRY"
                            // we plan next pattern randomly appears
                            nextPatternShowDelay = randomLongBetween(MIN_DELAY, MAX_DELAY)
                            println("#######################")
                            println("#######################")
                            println("#######################")
                            println("Next pattern appears in: $nextPatternShowDelay")
                            println("#######################")
                            println("#######################")
                            println("#######################")
                            DISPLAYED = false
                            mainHandler.postDelayed(patternChangeTask, nextPatternShowDelay);
                        }


                    } else {

                        stopGame()
                        binding.switchCostResultTv.text = "You failed. Try Again."
                    }


                }
            }
        }
    }

    val patternChangeTask = object : Runnable {
        override fun run () {
            RANDOM_PATTERN = "pattern_" + ALL_PATTERNS.random()
            PATTERN_ID = resources.getIdentifier("com.example.esportstryouts:drawable/$RANDOM_PATTERN", null, null)
            ANSWER_LEFT = ALL_ANSWERS.random()

            if (ANSWER_LEFT == "rectangle") {
                ANSWER_RIGHT = "square"
                binding.switchCostAnswerLeftImg.setImageResource(R.drawable.pattern_rectangle1)
                binding.switchCostAnswerRightImg.setImageResource(R.drawable.pattern_square1)}
            else {ANSWER_RIGHT = "rectangle"
                binding.switchCostAnswerLeftImg.setImageResource(R.drawable.pattern_square1)
                binding.switchCostAnswerRightImg.setImageResource(R.drawable.pattern_rectangle1)}

            binding.switchCostPatternArea.setImageResource(PATTERN_ID)

            START_TIME = System.currentTimeMillis()
            DISPLAYED = true // random pattern is displayed



            if (RANDOM_PATTERN.contains("/*rectangle/*".toRegex()))
            {
                DISPLAYED_RECTANGLE = true
                DISPLAYED_SQUARE = false
                DISPLAYED_PATTERN = "rectangle"
                println("************")
                println("RECTANGLE IS SHOWN")
                println("************")
            }
            else {
                DISPLAYED_RECTANGLE = false
                DISPLAYED_SQUARE = true
                DISPLAYED_PATTERN = "square"
                println("************")
                println("SQUARE IS SHOWN")
                println("************")
            }


        }
    }

    fun randomLongBetween(origin : Long, bound : Long): Long {
        return origin + (Math.random() * (bound - origin)).toLong()
    }

}