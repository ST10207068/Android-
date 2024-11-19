package com.example.project3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class Register : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button
    private lateinit var loadingProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize views
        usernameEditText = findViewById(R.id.editTextText2)
        emailEditText = findViewById(R.id.editTextTextEmailAddress)
        passwordEditText = findViewById(R.id.editTextTextPassword2)
        registerButton = findViewById(R.id.register_button)
        loadingProgressBar = findViewById(R.id.loadingProgressBar) // Ensure this ProgressBar is in your layout

        registerButton.setOnClickListener {
            performRegistration()
        }
    }

    private fun performRegistration() {
        val username = usernameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
            return
        }

        registerUser(username, email, password)
    }

    private fun registerUser(username: String, email: String, password: String) {
        loadingProgressBar.visibility = ProgressBar.VISIBLE

        val stringRequest = object : StringRequest(
            Request.Method.POST,
            Database.URL_REGISTER, // Use the URL from the Database class
            Response.Listener { response ->
                loadingProgressBar.visibility = ProgressBar.GONE
                try {
                    val jsonObject = JSONObject(response)
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_LONG).show()
                    if (jsonObject.getString("message") == "Registration successful") {
                        startActivity(Intent(this, ChooseaccountActivity::class.java))
                        finish()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Registration failed: Invalid response", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                loadingProgressBar.visibility = ProgressBar.GONE
                Log.e("Register Error", error.toString())
                Toast.makeText(this, "Registration failed: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["email"] = email
                params["password"] = password
                return params
            }
        }

        // Add the request to the RequestQueue.
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}