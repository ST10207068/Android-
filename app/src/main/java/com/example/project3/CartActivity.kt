package com.example.project3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.database.*

class CartActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var ordersListView: ListView
    private lateinit var ordersAdapter: ArrayAdapter<String>
    private val ordersList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("cart")

        // Initialize UI components
        val paymentTypeSpinner: Spinner = findViewById(R.id.TypeofX)
        val locationInput: EditText = findViewById(R.id.location_input)
        ordersListView = findViewById(R.id.orders_list_view)

        // Populate payment type spinner
        val paymentsAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.Type_Of_Payment, android.R.layout.simple_spinner_item
        )
        paymentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        paymentTypeSpinner.adapter = paymentsAdapter

        // Initialize ListView Adapter
        ordersAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ordersList)
        ordersListView.adapter = ordersAdapter

        // Fetch and display orders
        fetchOrdersFromDatabase()

        // Set up buy button
        val buyButton = findViewById<Button>(R.id.buy_button10)
        buyButton.setOnClickListener {
            val location = locationInput.text.toString().trim()
            val price = "R50.00" // Example price
            val paymentType = paymentTypeSpinner.selectedItem.toString()

            if (location.isNotEmpty()) {
                saveOrderToDatabase(location, price, paymentType)
            } else {
                Toast.makeText(this, "Please enter your location", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up continue shopping button
        val continueShoppingButton = findViewById<Button>(R.id.continue_shopping_button)
        continueShoppingButton.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }


    private fun saveOrderToDatabase(location: String, price: String, paymentType: String) {
        val orderId = database.push().key
        val order = Order(orderId, location, price, paymentType)

        if (orderId != null) {
            database.child(orderId).setValue(order).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Order added to cart", Toast.LENGTH_SHORT).show()
                    ordersList.add("Order: $location, $price, $paymentType")
                    ordersAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Failed to add order", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchOrdersFromDatabase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ordersList.clear()
                for (orderSnapshot in snapshot.children) {
                    val order = orderSnapshot.getValue(Order::class.java)
                    order?.let {
                        ordersList.add("Order: ${it.location}, ${it.price}, ${it.paymentType}")
                    }
                }
                ordersAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CartActivity, "Failed to fetch orders", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

data class Order(
    val orderId: String? = null,
    val location: String = "",
    val price: String = "",
    val paymentType: String = ""
)
