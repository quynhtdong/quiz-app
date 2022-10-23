package edu.tcu.quynhtdong.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra("username")
        if(username == null) {
            setContentView(R.layout.activity_main)
            val nameEt = findViewById<TextInputEditText>(R.id.name_et)
            val startButton = findViewById<Button>(R.id.start_btn)
            startButton.setOnClickListener{ goToQuestion(nameEt) }

            nameEt.setOnEditorActionListener{ _, actionId, _ ->
                if(actionId == EditorInfo.IME_ACTION_GO){
                    goToQuestion(nameEt)
                    true
                } else false
            }
        }
    else {
            setContentView(R.layout.activity_result)
            val score = intent.getIntExtra("score", 0)
            findViewById<TextView>(R.id.goodluck_tv).text = getString(R.string.goodluck_name,username)
            findViewById<TextView>(R.id.score_tv).text = getString(R.string.score, score)
        }


    }

    private fun goToQuestion(nameEt: TextInputEditText){
        val intent = Intent(this, QuestionActivity::class.java)
        intent.putExtra("username", nameEt.text.toString())
        startActivity(intent)
        finish()
    }
}