package com.example.project3

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    // Example data representing item prices
    private val items = mutableListOf(
        Pair("Royal Feast", 50),
        Pair("Golden Delight", 60),
        Pair("Zani’s Signature", 40),
        Pair("Garden Harvest", 70),
        Pair("Spice of Life", 20),
        Pair("Sunrise Special", 30),
        Pair("Mzanzi Magic", 40),
        Pair("Morning Glory", 35),
        Pair("Firecracker Kota", 130)
    )

    // Example data for ingredient prices
    private val ingredientPrices = mapOf(
        "Cheese" to 5,
        "Egg" to 4,
        "Tomato" to 3,
        "Chili" to 2,
        "Mayo" to 5,
        "Cheese" to -5,
        "Egg" to -4,
        "Tomato" to -3,
        "Chili" to -2,
        "Mayo" to -5,

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Get all spinners and TextViews for prices
        val spinners = listOf<Spinner>(
            findViewById(R.id.ingredient_spinner1),
            findViewById(R.id.ingredients_list),
            findViewById(R.id.ingredients_list1),
            findViewById(R.id.ingredients_list2),
            findViewById(R.id.ingredients_list4),
            findViewById(R.id.ingredients_list5),
            findViewById(R.id.ingredients_list6),
            findViewById(R.id.ingredients_list7),
            findViewById(R.id.ingredients_list8)
        )

        val priceTextViews = listOf<TextView>(
            findViewById(R.id.textView7),
            findViewById(R.id.textView20),
            findViewById(R.id.textView14),
            findViewById(R.id.textView13),
            findViewById(R.id.textView18),
            findViewById(R.id.textView188),
            findViewById(R.id.textView455),
            findViewById(R.id.textView108),
            findViewById(R.id.textView272)
        )

        // Set up spinners with listener for ingredient selection
        spinners.forEachIndexed { index, spinner ->
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: android.view.View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedIngredient = parent?.getItemAtPosition(position) as? String
                    val basePrice = items[index].second
                    val ingredientPrice = ingredientPrices[selectedIngredient] ?: 0

                    // Update the corresponding TextView with the new price
                    val totalPrice = basePrice + ingredientPrice
                    priceTextViews[index].text = "Price: R$totalPrice.00"
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }
        }

        // Handle "Order Now" button clicks
        val orderButtons = listOf<Button>(
            findViewById(R.id.button1),
            findViewById(R.id.button2),
            findViewById(R.id.button3),
            findViewById(R.id.button5),
            findViewById(R.id.button6),
            findViewById(R.id.button7),
            findViewById(R.id.button8),
            findViewById(R.id.button10),
            findViewById(R.id.button9)
        )

        orderButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                val selectedIngredient =
                    spinners[index].selectedItem as? String ?: "No ingredient"
                val itemName = items[index].first
                val totalPrice = priceTextViews[index].text.toString()

                Toast.makeText(
                    this@DashboardActivity,
                    "Ordered $itemName with $selectedIngredient for $totalPrice",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
