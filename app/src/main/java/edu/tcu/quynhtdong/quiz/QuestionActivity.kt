package edu.tcu.quynhtdong.quiz

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.Serializable

class QuestionActivity: AppCompatActivity(), View.OnClickListener{
    private var questions = Constants.getQuestions().shuffled()
    private var questionIdx = 0
    private val optionTvs = mutableListOf<TextView>()
    private var selectedOptionIdx = -1
    private var answerRevealed = false
    private var correctAnswers = 0
    private var correctOpt = -1
    private var username = "username"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        username = intent.getStringExtra("username").toString()
//        correctAnswers = intent.getIntExtra("correct", 0)
//        questionIdx = intent.getIntExtra("questionIdx", 0)

        findViewById<ProgressBar>(R.id.progress_bar).max = 10
        findViewById<ProgressBar>(R.id.progress_bar).progress = questionIdx + 1
        findViewById<TextView>(R.id.progress_tv).text = getString(R.string.progress, questionIdx + 1, questions.size)
        findViewById<TextView>(R.id.question_tv).text = questions[questionIdx].question
        findViewById<ImageView>(R.id.flag_iv).setImageResource(questions[questionIdx].image)

        val optionLl = findViewById<LinearLayout>(R.id.option_ll)

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.topMargin = dpToPx(16)

        for(i in questions[questionIdx].options.indices){
            val optionTv = TextView(this)
            optionTv.layoutParams = layoutParams
            optionTv.gravity = Gravity.CENTER
            optionTv.setPadding(dpToPx(16),dpToPx(16),dpToPx(16),dpToPx(16))
            optionTv.textSize = 16F
            optionTv.text = questions[questionIdx].options[i]
            optionTv.setBackgroundResource(R.drawable.default_option_bg)

            optionLl.addView(optionTv)
            optionTvs.add(optionTv)
            if (questions[questionIdx].options[i].equals(questions[questionIdx].correctAnswer)){
                correctOpt = i
            }
            optionTv.setOnClickListener{
                selectedOptionView(optionTv)
                selectedOptionIdx = i
            }
        }

        findViewById<Button>(R.id.submit_btn).setOnClickListener(this)


    }

    private fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density

        return (dp*scale+0.5).toInt()
    }

    private fun selectedOptionView(selectedOptionTv: TextView){
        for(optionTv in optionTvs){
            optionTv.setTextColor(ContextCompat.getColor(this, R.color.gray))
            optionTv.typeface = Typeface.DEFAULT
            optionTv.setBackgroundResource(R.drawable.default_option_bg)
        }

        selectedOptionTv.setTextColor(ContextCompat.getColor(this, R.color.black))
        selectedOptionTv.typeface = Typeface.DEFAULT_BOLD
        selectedOptionTv.setBackgroundResource(R.drawable.selected_option_bg)
    }

    private fun answerView(correctOptionTv: TextView) {
        optionTvs[selectedOptionIdx].setBackgroundResource(R.drawable.wrong_option_bg)
        correctOptionTv.setTextColor(ContextCompat.getColor(this, R.color.black))
        correctOptionTv.typeface = Typeface.DEFAULT_BOLD
        correctOptionTv.setBackgroundResource(R.drawable.correct_option_bg)
        for(optionTv in optionTvs){
            optionTv.setOnClickListener(null)
        }
        val submitBtn = findViewById<Button>(R.id.submit_btn)
        if(questionIdx <questions.size - 1){
            submitBtn.text = "Go to next question"
        } else {
            submitBtn.text = "Finish"
        }
        if(correctOptionTv.equals(optionTvs[selectedOptionIdx])) {
            correctAnswers++
        }
        answerRevealed = true
    }

    override fun onClick(view: View?){
        if(view == findViewById<TextView>(R.id.submit_btn)){

            if(!answerRevealed){
                var correctOptionTv = optionTvs[correctOpt]
                answerView(correctOptionTv)
            } else if(questionIdx <questions.size - 1){
                goToQuestion()
            } else {
                goToFinish()
            }

        }
    }

    private fun goToQuestion(){

        answerRevealed = false
        selectedOptionIdx = -1
        questionIdx++
        optionTvs.removeAll(optionTvs)
        findViewById<ProgressBar>(R.id.progress_bar).max = 10
        findViewById<ProgressBar>(R.id.progress_bar).progress = questionIdx + 1
        findViewById<TextView>(R.id.progress_tv).text = getString(R.string.progress, questionIdx + 1, questions.size)
        findViewById<TextView>(R.id.question_tv).text = questions[questionIdx].question
        findViewById<ImageView>(R.id.flag_iv).setImageResource(questions[questionIdx].image)
        findViewById<Button>(R.id.submit_btn).text = "SUBMIT"
        val optionLl = findViewById<LinearLayout>(R.id.option_ll)
        optionLl.removeAllViews()

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.topMargin = dpToPx(16)

        for(i in questions[questionIdx].options.indices){
            val optionTv = TextView(this)
            optionTv.layoutParams = layoutParams
            optionTv.gravity = Gravity.CENTER
            optionTv.setPadding(dpToPx(16),dpToPx(16),dpToPx(16),dpToPx(16))
            optionTv.textSize = 16F
            optionTv.text = questions[questionIdx].options[i]
            optionTv.setBackgroundResource(R.drawable.default_option_bg)

            optionLl.addView(optionTv)
            optionTvs.add(optionTv)
            if (questions[questionIdx].options[i].equals(questions[questionIdx].correctAnswer)){
                correctOpt = i
            }
            optionTv.setOnClickListener{
                selectedOptionView(optionTv)
                selectedOptionIdx = i
            }
        }

    }

    private fun goToFinish(){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("score", correctAnswers)
        intent.putExtra("username", username)
        startActivity(intent)
        finish()
    }

}