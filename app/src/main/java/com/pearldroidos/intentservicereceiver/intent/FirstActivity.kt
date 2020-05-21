package com.pearldroidos.intentservicereceiver.intent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pearldroidos.intentservicereceiver.R
import kotlinx.android.synthetic.main.activity_first.*

class FirstActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        Toast.makeText(this, "Start App!!", Toast.LENGTH_LONG).show()

        btnIntent1.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("Title", "My name is DroidOs")
            intent.putExtra("Age", "I am 25 year old")
            startActivityForResult(intent,100)
        }
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 100){
            if(resultCode == Activity.RESULT_OK){
                val result = data?.getStringExtra("Result") ?: "Error"
                tvIntent1.text = result
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


}