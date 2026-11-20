package com.example.myapplication

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

var score = 0
var element = 1
var difficultZone = "basic"
var backgroundColour = "blue"
var performClosed = false
var previousScore = 0
var highscore = 0
lateinit var rootView: View
lateinit var scoreView: TextView
lateinit var elementView: TextView
lateinit var highscoreView: TextView
lateinit var previousScoreView: TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        rootView = findViewById(R.id.main)
        scoreView = findViewById(R.id.scoreView)
        elementView = findViewById(R.id.elementView)
        highscoreView = findViewById(R.id.highscoreView)
        previousScoreView = findViewById(R.id.previousScoreView)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        updateUi()
    }

    private fun updateUi() {
        scoreView.text = getString(R.string.scoreView, score)
        elementView.text = getString(R.string.elementView, element)
        highscoreView.text = getString(R.string.highscoreView, highscore)
        previousScoreView.text = getString(R.string.previousScoreView, previousScore)

        rootView.setBackgroundColor(
            when (backgroundColour) {
                "green" -> Color.GREEN
                "orange" -> Color.rgb(255, 165, 0)
                else -> Color.CYAN
            }
        )
    }

    fun addScore(){
        if (score >= 20) {
            return
        }
        score = when (difficultZone) {
            "basic" -> score + 1
            "intermediate" -> score + 2
            "advance" -> score + 3
            else -> 0
        }
        element += 1
        updateElement()
    }
    fun updateElement(){
        difficultZone = when (element) {
            in 4..7 -> "intermediate"
            in 8..10 -> "advance"
            else -> "basic"
        }
        backgroundColour = when (difficultZone) {
            "intermediate" -> "green"
            "advance" -> "orange"
            else -> "blue"
        }
    }
    fun deduction(){
        if (element <= 1) {
            return
        }
        score -= 2
    }
    fun deductionButtonPress(view: View?) {
        if (element <= 1) {
            return
        }
        deduction()
        performClosed = true
        updateUi()
    }
    fun performButtonPress(view: View?) {
        if (element == 11 || performClosed){
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
        previousScore = score
        highscore = maxOf(highscore, score)
        score = 0
        element = 1
        difficultZone = "basic"
        backgroundColour = "blue"
        performClosed = false
        updateElement()
    }
}