package com.example.mobcapp

import android.app.Activity
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.drawable.Drawable


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
//        sharedPreferences.edit().putBoolean("hasCompletedOnboarding", true).apply()

        val hasCompleted = sharedPreferences.getBoolean("hasCompletedOnboarding", false)
        if (!hasCompleted) {
            start()
            sharedPreferences.edit().putBoolean("hasCompletedOnboarding", true).apply()
        }
        else {
            val intent = Intent(this, ActivityMainMenu::class.java)
            startActivity(intent)
        }
    }

    private lateinit var meals_Spinner: Spinner
    private lateinit var mealImage_ImageView: ImageView
    private lateinit var price_TextView: TextView
    private lateinit var mealTypes_RadioGroup: RadioGroup
    private lateinit var alaCarte_RadioButton: RadioButton
    private lateinit var valueMeal_RadioButton: RadioButton
    private lateinit var valueMealGroup_LinearLayout: LinearLayout
    private lateinit var fries_CheckBox: CheckBox
    private lateinit var friesSizes_RadioGroup: RadioGroup
    private lateinit var friesMedium_RadioButton: RadioButton
    private lateinit var friesLarge_RadioButton: RadioButton
    private lateinit var drink_CheckBox: CheckBox
    private lateinit var drinkSizes_RadioGroup: RadioGroup
    private lateinit var drinkMedium_RadioButton: RadioButton
    private lateinit var drinkLarge_RadioButton: RadioButton
    private lateinit var orderAmount_EditText: EditText
    private lateinit var incrementOrderAmount_Button: Button
    private lateinit var decrementOrderAmount_Button: Button
    private lateinit var resetOrderAmount_Button: Button
    private lateinit var totalPrice_TextView: TextView
    private lateinit var completeOrder_Button: Button
    private lateinit var mealData: Map<String, Pair<Double, Drawable?>>
    private fun start() {
        meals_Spinner = findViewById(R.id.meals_Spinner)
        mealImage_ImageView = findViewById(R.id.mealImage_ImageView)
        price_TextView = findViewById(R.id.price_TextView)
        mealTypes_RadioGroup = findViewById(R.id.mealTypes_RadioGroup)
        alaCarte_RadioButton = findViewById(R.id.alaCarte_RadioButton)
        valueMeal_RadioButton = findViewById(R.id.valueMeal_RadioButton)
        valueMealGroup_LinearLayout = findViewById(R.id.valueMealGroup_LinearLayout)
        fries_CheckBox = findViewById(R.id.fries_CheckBox)
        friesSizes_RadioGroup = findViewById(R.id.friesSizes_RadioGroup)
        friesMedium_RadioButton = findViewById(R.id.friesMedium_RadioButton)
        friesLarge_RadioButton = findViewById(R.id.friesLarge_RadioButton)
        drink_CheckBox = findViewById(R.id.drink_CheckBox)
        drinkSizes_RadioGroup = findViewById(R.id.drinkSizes_RadioGroup)
        drinkMedium_RadioButton = findViewById(R.id.drinkMedium_CheckBox)
        drinkLarge_RadioButton = findViewById(R.id.drinkLarge_CheckBox)
        orderAmount_EditText = findViewById(R.id.orderAmount_EditText)
        incrementOrderAmount_Button = findViewById(R.id.increment_button)
        decrementOrderAmount_Button = findViewById(R.id.decrement_Button)
        resetOrderAmount_Button = findViewById(R.id.reset_Button)
        totalPrice_TextView = findViewById(R.id.totalPrice_TextView)
        completeOrder_Button = findViewById(R.id.completeOrder_Button)

        mealData = mapOf(
            "Burger" to Pair(39.99, Drawable.createFromStream(assets.open("items/burger.png"), null)),
            "Spaghetti" to Pair(49.99, Drawable.createFromStream(assets.open("items/spaghetti.png"), null)),
            "Pizza" to Pair(99.99, Drawable.createFromStream(assets.open("items/pizza.png"), null)),
            "Chicken" to Pair(89.99, Drawable.createFromStream(assets.open("items/chicken.png"), null)),
            "Sandwich" to Pair(29.99, Drawable.createFromStream(assets.open("items/sandwich.png"), null)),
            "Tapsilog" to Pair(119.99, Drawable.createFromStream(assets.open("items/tapsilog.png"), null)),
            "Tilapia" to Pair(109.99, Drawable.createFromStream(assets.open("items/tilapia.png"), null)),
            "Halo-Halo" to Pair(99.99, Drawable.createFromStream(assets.open("items/halohalo.png"), null)),
            "Lumpiang Shanghai" to Pair(89.99, Drawable.createFromStream(assets.open("items/lumpia.png"), null)),
            "Puto" to Pair(49.99, Drawable.createFromStream(assets.open("items/puto.png"), null))
        )

        val meals = arrayOf("Burger", "Spaghetti", "Pizza", "Chicken", "Sandwich",
            "Tapsilog", "Tilapia", "Halo-Halo", "Lumpiang Shanghai", "Puto"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, meals)
        meals_Spinner.adapter = adapter

        meals_Spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedMeal = meals[position]  // Get the selected meal name

                val (price, imageResId) = mealData[selectedMeal] ?: return

                price_TextView.text = "P%.2f".format(price)
//                mealImage_ImageView.setImageResource(imageResId)
                mealImage_ImageView.setImageDrawable(imageResId)
                updateTotalPrice()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        mealTypes_RadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.valueMeal_RadioButton) {
                valueMealGroup_LinearLayout.visibility = View.VISIBLE
                fries_CheckBox.setEnabled(true)
                drink_CheckBox.setEnabled(true)
            } else {
                valueMealGroup_LinearLayout.visibility = View.GONE
                fries_CheckBox.setEnabled(false)
                drink_CheckBox.setEnabled(false)
            }
            updateTotalPrice()
        }

        fries_CheckBox.setOnCheckedChangeListener { _, isChecked ->
            friesSizes_RadioGroup.visibility = if (isChecked) View.VISIBLE else View.GONE
            updateTotalPrice()
        }

        friesMedium_RadioButton.setOnCheckedChangeListener { _, isChecked ->
            updateTotalPrice()
        }

        friesLarge_RadioButton.setOnCheckedChangeListener { _, isChecked ->
            updateTotalPrice()
        }

        drink_CheckBox.setOnCheckedChangeListener { _, isChecked ->
            drinkSizes_RadioGroup.visibility = if (isChecked) View.VISIBLE else View.GONE
            updateTotalPrice()
        }

        drinkMedium_RadioButton.setOnCheckedChangeListener { _, isChecked ->
            updateTotalPrice()
        }

        drinkLarge_RadioButton.setOnCheckedChangeListener { _, isChecked ->
            updateTotalPrice()
        }

        orderAmount_EditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val orderAmount = orderAmount_EditText.text.toString().toIntOrNull()?.coerceIn(1, 10) ?: 1
                orderAmount_EditText.setText(orderAmount.toString())
                updateTotalPrice()
            }
        }

        incrementOrderAmount_Button.setOnClickListener {
            val orderAmount = (orderAmount_EditText.text.toString().toIntOrNull()?: 1) + 1
            orderAmount_EditText.setText(orderAmount.coerceIn(1, 10).toString())
            updateTotalPrice()
        }

        decrementOrderAmount_Button.setOnClickListener {
            val orderAmount = (orderAmount_EditText.text.toString().toIntOrNull()?: 1) - 1
            orderAmount_EditText.setText(orderAmount.coerceIn(1, 10).toString())
            updateTotalPrice()
        }

        resetOrderAmount_Button.setOnClickListener {
            orderAmount_EditText.setText("1")
            updateTotalPrice()
        }

        completeOrder_Button.setOnClickListener {
            val intent = Intent(this, ActivityMainMenu::class.java)
            startActivity(intent)

//            val selectedMeal = meals_Spinner.selectedItem.toString()
//            val (mealPrice, _) = mealData[selectedMeal] ?: return@setOnClickListener
//
//            val orderAmount = orderAmount_EditText.text.toString().toIntOrNull()?.coerceIn(1, 10) ?: 1
//            val isValueMeal = valueMeal_RadioButton.isChecked
//            val friesSize = if (fries_CheckBox.isChecked) {
//                if (friesLarge_RadioButton.isChecked) "Large" else "Medium"
//            } else null
//            val drinkSize = if (drink_CheckBox.isChecked) {
//                if (drinkLarge_RadioButton.isChecked) "Large" else "Medium"
//            } else null
//
//            val totalPrice = (mealPrice + (if (isValueMeal) 2.00 else 0.0) +
//                    (if (friesSize != null) if (friesSize == "Large") 2.00 else 1.50 else 0.0) +
//                    (if (drinkSize != null) if (drinkSize == "Large") 1.50 else 1.00 else 0.0)) * orderAmount
//
//            val dbHelper = Database(this)
//            val result = dbHelper.insertOrder(selectedMeal, mealPrice, orderAmount, isValueMeal, friesSize, drinkSize, totalPrice)
//
//            if (result != -1L) {
//                Toast.makeText(this, "Order saved successfully!", Toast.LENGTH_SHORT).show()
//                finish()
//                startActivity(intent)
//            } else {
//                Toast.makeText(this, "Failed to save order.", Toast.LENGTH_SHORT).show()
//            }
        }
    }

    private fun updateTotalPrice() {
        val selectedMeal = meals_Spinner.selectedItem.toString()
        val (basePrice, _) = mealData[selectedMeal] ?: return

        val amount = orderAmount_EditText.text.toString().toIntOrNull() ?: 1
        var extraCost = 0.0

        if (valueMeal_RadioButton.isChecked) {
            if (fries_CheckBox.isChecked)
                extraCost += if (friesLarge_RadioButton.isChecked) 34.99 else 24.99
            if (drink_CheckBox.isChecked)
                extraCost += if (drinkLarge_RadioButton.isChecked) 24.99 else 14.99
        }

        val totalPrice = (basePrice + extraCost) * amount
        totalPrice_TextView.text = "Total Price: " + "P%.2f".format(totalPrice)
    }

    fun Activity.hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = currentFocus ?: View(this) // Get current focused view or create a dummy one
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus() // Remove focus from the current view
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is EditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    hideKeyboard() // Hide keyboard and remove focus if clicked outside
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

}