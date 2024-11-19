package com.example.project3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    //lets make button functionsfor log in
    val button = findViewById<Button>(R.id.button_login1)
    button.setOnClickListener(){
        val intent = Intent (this,Login::class.java)
        startActivity(intent)
    }
    //lets make for register now
    val textButton = findViewById<TextView>(R.id.textView_Register)
    textButton.setOnClickListener(){
        val intent = Intent (this,Register::class.java)
        startActivity(intent)
    }
}
}