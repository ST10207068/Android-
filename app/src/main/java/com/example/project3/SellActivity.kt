package com.example.project3

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

class SellActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var priceEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var numberEditText: EditText  // Change from phoneEditText to numberEditText
    private lateinit var saveButton: Button
    private var imageUri: Uri? = null  // To store the selected image URI

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell)

        // Set up navigation to view collection
        val textButton1 = findViewById<TextView>(R.id.viewCollectionButton)
        textButton1.setOnClickListener {
            startActivity(Intent(this, DisplayColActivity::class.java))
        }

        // Initialize views
        imageView = findViewById(R.id.imageView)
        priceEditText = findViewById(R.id.priceEditText)
        locationEditText = findViewById(R.id.locationEditText)
        numberEditText = findViewById(R.id.phoneEditText)  // Change variable name to numberEditText
        saveButton = findViewById(R.id.saveButton)

        // Set click listener for image view to open gallery
        imageView.setOnClickListener {
            openGallery()
        }

        // Set click listener for save button
        saveButton.setOnClickListener {
            saveInfo()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data  // Store the image URI
            imageView.setImageURI(imageUri)  // Display the image
        }
    }

    private fun saveInfo() {
        val price = priceEditText.text.toString()
        val location = locationEditText.text.toString()
        val number = numberEditText.text.toString()  // Change from phone to number

        // Check if any field is empty
        if (price.isBlank() || location.isBlank() || number.isBlank()) {
            // Prompt the user to enter all details
            Toast.makeText(this, "Please enter all details.", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a string request
        val url = Database.URL_SAVE_ITEM // Ensure this URL points to your save_item.php
        val stringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    val error = jsonResponse.getBoolean("error")

                    if (!error) {
                        Toast.makeText(this, "Item saved successfully", Toast.LENGTH_SHORT).show()

                        // Optionally pass data to DisplayColActivity
                        val intent = Intent(this, DisplayColActivity::class.java).apply {
                            putExtra("PRICE", price)
                            putExtra("LOCATION", location)
                            putExtra("NUMBER", number)  // Change from PHONE to NUMBER
                        }
                        startActivity(intent)
                    } else {
                        val message = jsonResponse.getString("message")
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Volley Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["price"] = price
                params["location"] = location
                params["number"] = number  // Change from PHONE to NUMBER
                return params
            }
        }

        // Add the request to the RequestQueue.
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}