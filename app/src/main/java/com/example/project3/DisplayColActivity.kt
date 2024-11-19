package com.example.project3

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DisplayColActivity : AppCompatActivity() {

    private lateinit var displayImageView: ImageView
    private lateinit var displayPriceTextView: TextView
    private lateinit var displayLocationTextView: TextView
    private lateinit var displayPhoneTextView: TextView
    private lateinit var achievementTextView: TextView
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_col)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("kotacreatortbl")

        // Initialize views
        displayImageView = findViewById(R.id.displayImageView)
        displayPriceTextView = findViewById(R.id.displayPriceTextView)
        displayLocationTextView = findViewById(R.id.displayLocationTextView)
        displayPhoneTextView = findViewById(R.id.displayPhoneTextView)
        achievementTextView = findViewById(R.id.achievementTextView)

        // Retrieve data passed from previous activity
        val price = intent.getStringExtra("PRICE")
        val location = intent.getStringExtra("LOCATION")
        val phone = intent.getStringExtra("PHONE")
        val imageUri = intent.getStringExtra("IMAGE_URI")

        // Set data to views
        displayPriceTextView.text = "Price: $price"
        displayLocationTextView.text = "Location: $location"
        displayPhoneTextView.text = "Phone: $phone"
        if (!imageUri.isNullOrEmpty()) {
            displayImageView.setImageURI(Uri.parse(imageUri))
        }

        // Check for achievements
        checkAchievements()
    }

    private fun checkAchievements() {
        val user: FirebaseUser? = auth.currentUser
        user?.let {
            val userId = user.uid
            val achievementsRef = FirebaseDatabase.getInstance().getReference("achievements")

            achievementsRef.child(userId).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val itemCount = snapshot.child("user_items_count").getValue(Int::class.java) ?: 0
                    updateAchievementText(itemCount)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }

    private fun updateAchievementText(itemCount: Int) {
        val achievementText = when {
            itemCount >= 10 -> "Packrat"
            itemCount >= 3 -> "Collector"
            itemCount >= 1 -> "Starter"
            else -> "No achievements yet"
        }
        achievementTextView.text="ACHIEVEMENT SYSTEM"
        achievementTextView.text = "Achievement: $achievementText"
    }
}