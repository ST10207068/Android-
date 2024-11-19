package com.example.project3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class Login : AppCompatActivity() {
    private lateinit var editTextUsernameOrEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Make sure your XML is named activity_login.xml

        editTextUsernameOrEmail = findViewById(R.id.editTextText)
        editTextPassword = findViewById(R.id.editTextTextPassword)
        loginButton = findViewById(R.id.LogIn_button)

        loginButton.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val usernameOrEmail = editTextUsernameOrEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        if (usernameOrEmail.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter your username/email and password", Toast.LENGTH_SHORT).show()
            return
        }

        // Send login request to server
        val url = Database.URL_LOGIN // Reference the URL from the Database class

        val stringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener<String> { response ->
                try {
                    val jsonResponse = JSONObject(response)
                    val error = jsonResponse.getBoolean("error")
                    val message = jsonResponse.getString("message")

                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

                    if (!error) {
                        // If login is successful, navigate to ChooseAccountActivity
                        val intent = Intent(this, ChooseaccountActivity::class.java)
                        startActivity(intent)
                        finish() // Optional: finish the login activity
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Response error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Network error: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["username"] = usernameOrEmail // Use this for both username and email
                params["password"] = password
                return params
            }
        }

        // Add the request to the RequestQueue
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}