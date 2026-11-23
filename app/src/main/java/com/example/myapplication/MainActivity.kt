package com.example.myapplication

import android.graphics.Color

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log



class MainActivity : AppCompatActivity() {
    companion object {
        const val APP = "GymnasticsApp"
        const val maxScore = 20
        const val lastElement = 10
        const val deductionPoints = 2

        const val KEY_SCORE = "score"
        const val KEY_ELEMENT = "element"
        const val KEY_PERFORMCLOSED = "performClosed"
        const val KEY_ROUTINECOMPLETE = "routineComplete"
        const val KEY_HIGHSCORE = "highscore"
        const val KEY_PREVIOUSSCORE = "previousScore"
    }
    var score = 0
    var element = 1
    var difficultZone = "basic"
    var textColour = "blue"
    var performClosed = false
    var routineComplete = false
    var previousScore = 0
    var highscore = 0


    lateinit var scoreView: TextView
    lateinit var elementView: TextView
    lateinit var highscoreView: TextView
    lateinit var previousScoreView: TextView
    lateinit var completeView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        scoreView = findViewById(R.id.scoreView)
        elementView = findViewById(R.id.elementView)
        highscoreView = findViewById(R.id.highscoreView)
        previousScoreView = findViewById(R.id.previousScoreView)
        completeView = findViewById(R.id.completeView)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        updateUi()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SCORE, score)
        outState.putInt(KEY_ELEMENT, element)
        outState.putBoolean(KEY_PERFORMCLOSED, performClosed)
        outState.putBoolean(KEY_ROUTINECOMPLETE, routineComplete)
        outState.putInt(KEY_HIGHSCORE, highscore)
        outState.putInt(KEY_PREVIOUSSCORE, previousScore)
        Log.d(APP, "State Saved: score = $score, element = $element")
    }
    override fun onRestoreInstanceState(onSavedInstanceState: Bundle){
        super.onRestoreInstanceState(onSavedInstanceState)
        score = onSavedInstanceState.getInt(KEY_SCORE)
        element = onSavedInstanceState.getInt(KEY_ELEMENT)
        performClosed = onSavedInstanceState.getBoolean(KEY_PERFORMCLOSED)
        routineComplete = onSavedInstanceState.getBoolean(KEY_ROUTINECOMPLETE)
        highscore = onSavedInstanceState.getInt(KEY_HIGHSCORE)
        previousScore = onSavedInstanceState.getInt(KEY_PREVIOUSSCORE)
        updateElement()
        updateUi()
        Log.d(APP,"State restores: score = $score, element = $element")
    }
    private fun updateUi() {
        scoreView.text = getString(R.string.scoreView, score)
        highscoreView.text = getString(R.string.highscoreView, highscore)
        previousScoreView.text = getString(R.string.previousScoreView, previousScore)
        elementView.setTextColor(
            when (textColour) {
                "green" -> Color.rgb(0, 160, 0)
                "orange" -> Color.rgb(255, 165, 0)
                else -> Color.BLUE
            }
        )
        completeView.visibility = if (routineComplete) View.VISIBLE else View.GONE
        completeView.text = getString(R.string.completeView, score)
        elementView.text = getString(R.string.elementView, element)
    }

    fun addScore(){
        if (score >= maxScore) {
            return
        }
        var addedScore = 0
        when (difficultZone) {
            "basic" -> addedScore = 1
            "intermediate" -> addedScore = 2
            "advance" -> addedScore = 3
        }
        score += addedScore
        Log.d(APP, "Element $element performed: +$addedScore, score=$score")

        if (element == lastElement){
            routineComplete = true

            Log.d(APP, "Routine complete, final score=$score")
        } else {
            element++
        }

        updateElement()
    }
    fun updateElement(){
        difficultZone = when (element) {
            in 4..7 -> "intermediate"
            in 8..10 -> "advance"
            else -> "basic"
        }
        textColour = when (difficultZone) {
            "intermediate" -> "green"
            "advance" -> "orange"
            else -> "blue"
        }
    }
    fun deduction(){
        Log.d(APP, "Deduction pressed (element=$element, score=$score)")
        if (element <= 1 || routineComplete) {
            Log.d(APP, "Deduction was blocked: ")
            return
        }
        score -= deductionPoints
        score = maxOf(0 , score)
        Log.d(APP, "Deduction was taken, score=$score")
    }
    fun deductionButtonPress(view: View?) {
        deduction()
        performClosed = true
        updateUi()
    }
    fun performButtonPress(view: View?) {
        Log.d(APP, "Performed pressed. Element = $element, score = $score")
        if (performClosed){
            Log.d(APP, "Performed been blocked: complete=$routineComplete, dedution=$performClosed")
            return
        }
        addScore()
        updateUi()
    }
    fun resetButtonPress(view: View?) {
        reset()
        updateUi()
    }
    fun reset(){
        Log.d(APP, "Reset pressed (score=$score)")
        previousScore = score
        highscore = maxOf(highscore, score)
        score = 0
        element = 1
        difficultZone = "basic"
        textColour = "blue"
        routineComplete = false
        performClosed = false
        updateElement()
    }
}