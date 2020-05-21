package com.pearldroidos.intentservicereceiver.intent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pearldroidos.intentservicereceiver.R
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {

    private lateinit var intent2: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        this.intent2 = intent
        var bundle = intent2.extras

        tvIntent2.text = bundle?.getString("Title") ?: "Name"
        tvIntent22.text = bundle?.getString("Age") ?: "Age"
    }

    override fun onBackPressed() {
        //Should declaration before super
        Toast.makeText(this, "Back", Toast.LENGTH_LONG).show()
        intent2.putExtra("Result", "How are you?")
        setResult(Activity.RESULT_OK, intent2)

        //Should define because it will not back if you won't set it
        super.onBackPressed()
    }
}

















