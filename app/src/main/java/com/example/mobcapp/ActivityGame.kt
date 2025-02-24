package com.example.mobcapp

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class ActivityGame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // leave blank
            }
        })

        init()
        loop()
    }

    private lateinit var gameTimer_TextView: TextView
    var gameTimer_Long = 0

    private lateinit var customer1_CardView: CardView
    private lateinit var customer2_CardView: CardView
    private lateinit var customer3_CardView: CardView
    private lateinit var customerName1_TextView: TextView
    private lateinit var customerName2_TextView: TextView
    private lateinit var customerName3_TextView: TextView
    private lateinit var customerTimer1_TextView: TextView
    var customerTimer1Duration_Long = 0
    private lateinit var customerTimer2_TextView: TextView
    var customerTimer2Duration_Long = 0
    private lateinit var customerTimer3_TextView: TextView
    var customerTimer3Duration_Long = 0
    private lateinit var customerImage1_ImageView: ImageView
    private lateinit var customerImage2_ImageView: ImageView
    private lateinit var customerImage3_ImageView: ImageView
    private lateinit var dropCustomer1_Button: Button
    private lateinit var dropCustomer2_Button: Button
    private lateinit var dropCustomer3_Button: Button
    private var customer1Dialogue_String = ""
    private var customer2Dialogue_String = ""
    private var customer3Dialogue_String = ""
    private var customerServeNumber = 0

    private lateinit var customerServeName_TextView: TextView
    private lateinit var customerServeImage_ImageView: ImageView
    private lateinit var customerServeTimer_TextView: TextView
    private lateinit var customerServeDialogue_TextView: TextView

    private lateinit var backServeCustomer_Button: Button
    private lateinit var customerServeDrop_Button: Button
    private lateinit var customerServeGive_Button: Button
    private lateinit var customerServeMenu_Button: Button

    private val itemDatabase by lazy { ItemsDatabase(this) }
    private val customerDatabase by lazy { CustomerDatabase(this) }
    private val customerPrefsDatabase by lazy { CustomerPrefsDatabase(this) }

    private lateinit var customerViews_Layout: LinearLayout

    private lateinit var recyclerView: RecyclerView
    private lateinit var orderCount_TextView: TextView
    private lateinit var orderTotalPrice_TextView: TextView
    private lateinit var viewOrders_Button: Button
    private lateinit var giveOrders_Button: Button
    private lateinit var backToCustomer_Button: Button
    private lateinit var customerTimerMenu_TextView: TextView
    private var ordered = 0

    private lateinit var openShop_Layout: LinearLayout

    private var textView_Score = 0

    private fun init() {

        openShop_Layout = findViewById(R.id.openShop_Layout)
        openShop_Layout.visibility = View.VISIBLE
        val openShop_Button = findViewById<Button>(R.id.openShop_Button)

        openShop_Button.setOnClickListener {
            openShop_Layout.visibility = View.GONE

            gameTimer_Long = 50
            customerTimer1Duration_Long = 300
            customerTimer2Duration_Long = 350
            customerTimer3Duration_Long = 400
        }

        gameTimer_TextView = findViewById(R.id.gameTimer_TextView)

        customerImage1_ImageView = findViewById(R.id.customerImage1_ImageView)
        customerImage2_ImageView = findViewById(R.id.customerImage2_ImageView)
        customerImage3_ImageView = findViewById(R.id.customerImage3_ImageView)
        customerImage1_ImageView.setImageDrawable(Drawable.createFromStream(assets.open("customer/customer.png"), null))
        customerImage2_ImageView.setImageDrawable(Drawable.createFromStream(assets.open("customer/customer.png"), null))
        customerImage3_ImageView.setImageDrawable(Drawable.createFromStream(assets.open("customer/customer.png"), null))

        customerName1_TextView = findViewById(R.id.customerName1_TextView)
        customerName2_TextView = findViewById(R.id.customerName2_TextView)
        customerName3_TextView = findViewById(R.id.customerName3_TextView)
        customerName1_TextView.text = "No one here yet..."
        customerName2_TextView.text = "No one here yet..."
        customerName3_TextView.text = "No one here yet..."

        customerTimer1_TextView = findViewById(R.id.customerTimer1_TextView)
        customerTimer2_TextView = findViewById(R.id.customerTimer2_TextView)
        customerTimer3_TextView = findViewById(R.id.customerTimer3_TextView)

        dropCustomer1_Button = findViewById(R.id.dropCustomer1_Button)
        dropCustomer2_Button = findViewById(R.id.dropCustomer2_Button)
        dropCustomer3_Button = findViewById(R.id.dropCustomer3_Button)

        customer1_CardView = findViewById(R.id.customer1_CardView)
        customer2_CardView = findViewById(R.id.customer2_CardView)
        customer3_CardView = findViewById(R.id.customer3_CardView)

        customerViews_Layout = findViewById(R.id.customerViews_Layout)
        customerViews_Layout.visibility = View.VISIBLE

        customerServeName_TextView = findViewById(R.id.customerServeName_TextView)
        customerServeName_TextView.text = "No one here yet..."
        customerServeImage_ImageView = findViewById(R.id.customerServeImage_ImageView)
        customerServeTimer_TextView = findViewById(R.id.customerServeTimer_TextView)
        customerServeDialogue_TextView = findViewById(R.id.customerServeDialogue_TextView)

        customer1_CardView.setOnClickListener {
            if (customerTimer1Duration_Long in 1..200) {
                customerViews_Layout.visibility = View.GONE

                customerServeName_TextView.text = customerName1_TextView.text
                customerServeImage_ImageView.setImageDrawable(customerImage1_ImageView.drawable)
                customerServeDialogue_TextView.text = customer1Dialogue_String
                customerServeNumber = 1
            }
        }

        customer2_CardView.setOnClickListener {
            if (customerTimer2Duration_Long in 1..200) {
                customerViews_Layout.visibility = View.GONE

                customerServeName_TextView.text = customerName2_TextView.text
                customerServeImage_ImageView.setImageDrawable(customerImage2_ImageView.drawable)
                customerServeDialogue_TextView.text = customer2Dialogue_String
                customerServeNumber = 2
            }
        }

        customer3_CardView.setOnClickListener {
            if (customerTimer3Duration_Long in 1..200) {
                customerViews_Layout.visibility = View.GONE

                customerServeName_TextView.text = customerName3_TextView.text
                customerServeImage_ImageView.setImageDrawable(customerImage3_ImageView.drawable)
                customerServeDialogue_TextView.text = customer3Dialogue_String
                customerServeNumber = 3
            }
        }

        val customerServe_Layout = findViewById<LinearLayout>(R.id.customerServe_Layout)
        customerServe_Layout.visibility = View.VISIBLE

        backServeCustomer_Button = findViewById(R.id.backServeCustomer_Button)
        backServeCustomer_Button.setOnClickListener {
            customerViews_Layout.visibility = View.VISIBLE
        }

        customerServeMenu_Button = findViewById(R.id.openMenuServeCustomer_Button)
        customerServeMenu_Button.setOnClickListener {
            customerServe_Layout.visibility = View.GONE
        }

        recyclerView = findViewById(R.id.cashier_RecyclerView)
        orderCount_TextView = findViewById(R.id.orderCount_TextView)
        orderTotalPrice_TextView = findViewById(R.id.orderTotalPrice_TextView)
        viewOrders_Button = findViewById(R.id.viewOrders_Button)
        giveOrders_Button = findViewById(R.id.giveOrders_Button)
        backToCustomer_Button = findViewById(R.id.backToCustomer_Button)
        customerTimerMenu_TextView = findViewById(R.id.customerTimerMenu_TextView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        update()

        viewOrders_Button.setOnClickListener {
            ordered = if (ordered == 0) 1 else 0
            viewOrders_Button.text = if (ordered == 0) "View Orders" else "View Menu"
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, (ordered != 0))
            update()
        }

        backToCustomer_Button.setOnClickListener {
            customerServe_Layout.visibility = View.VISIBLE
        }
    }

    private fun loop() {
        val handler = Handler(Looper.getMainLooper())

        val customerViews = this
        val runnable = object : Runnable {
            override fun run() {
                updateTimers()

                if (gameTimer_Long == 0 && openShop_Layout.visibility == View.GONE) {
                    handler.removeCallbacks(this)
                    startActivity(Intent(customerViews, ActivityGameOver::class.java).putExtra("SCORE", textView_Score))
                    return
                }

                handler.postDelayed(this, 100)
            }
        }

// Start loop
        handler.post(runnable)

// Stop loop if needed
// handler.removeCallbacks(runnable)
    }

    fun updateTimers() {
        gameTimer_Long = maxOf(--gameTimer_Long, 0)
        gameTimer_TextView.text = "${kotlin.math.ceil(gameTimer_Long / 10.0).toInt()}"

        customerTimer1Duration_Long = if (customerTimer1Duration_Long == 0) 250 else --customerTimer1Duration_Long
        customerTimer2Duration_Long = if (customerTimer2Duration_Long == 0) 250 else --customerTimer2Duration_Long
        customerTimer3Duration_Long = if (customerTimer3Duration_Long == 0) 250 else --customerTimer3Duration_Long

        customerTimer1_TextView.text = if (customerTimer1Duration_Long in 0..200) "${kotlin.math.ceil(customerTimer1Duration_Long / 10.0).toInt()}" else ""
        customerTimer2_TextView.text = if (customerTimer2Duration_Long in 0..200) "${kotlin.math.ceil(customerTimer2Duration_Long / 10.0).toInt()}" else ""
        customerTimer3_TextView.text = if (customerTimer3Duration_Long in 0..200) "${kotlin.math.ceil(customerTimer3Duration_Long / 10.0).toInt()}" else ""
        when (customerServeNumber) {
            1 -> {
                customerServeTimer_TextView.text = customerTimer1_TextView.text
                customerTimerMenu_TextView.text = "" + customerName1_TextView.text + ": " + customerTimer1_TextView.text
            }
            2 -> {
                customerServeTimer_TextView.text = customerTimer2_TextView.text
                customerTimerMenu_TextView.text = "" + customerName2_TextView.text + ": " + customerTimer2_TextView.text
            }
            3 -> {
                customerServeTimer_TextView.text = customerTimer3_TextView.text
                customerTimerMenu_TextView.text = "" + customerName3_TextView.text + ": " + customerTimer3_TextView.text
            }
        }
        updateCustomer(1, customerTimer1Duration_Long, customerName1_TextView, customerImage1_ImageView)
        updateCustomer(2, customerTimer2Duration_Long, customerName2_TextView, customerImage2_ImageView)
        updateCustomer(3, customerTimer3Duration_Long, customerName3_TextView, customerImage3_ImageView)
    }

    fun updateCustomer(customerIndex: Int, timer: Int, nameTextView: TextView, imageView: ImageView) {
        when (timer) {
            0 -> {
                if (customerViews_Layout.visibility == View.GONE && customerIndex == customerServeNumber) customerViews_Layout.visibility = View.VISIBLE

                val customer = customerDatabase.getCustomerByName("Customer Sample $customerIndex")
                nameTextView.text = "No one here yet..."
                imageView.setImageDrawable(Drawable.createFromStream(assets.open(customer!!.image[0]), null))
            }
            230 -> {
                nameTextView.text = "A customer is arriving..."
            }
            200 -> {
                val customer = customerDatabase.getRandomCustomer()
                nameTextView.text = customer!!.name
                imageView.setImageDrawable(Drawable.createFromStream(assets.open(customer.image[0]), null))
                when (customerIndex) {
                    1 -> customer1Dialogue_String = customer.dialogFiller[0]
                    2 -> customer2Dialogue_String = customer.dialogFiller[0]
                    3 -> customer3Dialogue_String = customer.dialogFiller[0]
                }
            }
        }
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Exit")
            .setMessage("Are you sure you want to go? Progress will be lost!")
            .setPositiveButton("Yes") { _, _ -> finish() } // Closes activity
            .setNegativeButton("No", null) // Dismisses dialog
            .show()
    }

    fun update() {
        val adapter = SampleAdapter(this, itemDatabase.getOrderedData(ordered), itemDatabase, this) { item ->
            if (item.ordered == 0) {
                itemDatabase.insertData(
                    item.name,
                    item.image,
                    item.desc,
                    item.price,
                    ordered = 1,
                    item.descLong,
                    item.calories,
                    item.grams,
                    item.ingredients
                )
                Snackbar.make(findViewById(android.R.id.content), "${item.name} added to cart", Snackbar.LENGTH_SHORT).show()
            }
            updateTextViews()

        }

        recyclerView.adapter = adapter
    }

    fun updateTextViews() {
        val orders = itemDatabase.getOrderedData(1)
        val totalAmount = orders.size
        val totalPrice = orders.sumOf { it.price }

        orderCount_TextView.text = "Orders: $totalAmount"
        orderTotalPrice_TextView.text = "Total: $totalPrice"
    }
}