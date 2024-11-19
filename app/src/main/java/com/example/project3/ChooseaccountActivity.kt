package com.example.project3


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class ChooseaccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooseaccount)


        //for BUY TEXT

        //for BUY TEXT
        val textButton = findViewById<TextView>(R.id.textView7_Buy)
        textButton.setOnClickListener { v: View? ->
            val intent = Intent(
                this,
                DashboardActivity::class.java
            )
            startActivity(intent)
        }

        //for SELL TEXT

        //for SELL TEXT
        val textButton1 = findViewById<TextView>(R.id.textView6_sell)
        textButton1.setOnClickListener { v: View? ->
            val intent = Intent(
                this,
                SellActivity::class.java
            )
            startActivity(intent)
        }
    }
}