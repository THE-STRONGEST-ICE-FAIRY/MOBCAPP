package com.example.mobcapp

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobcapp.ClickEffect.applyClickEffect
import kotlin.random.Random

class ActivityGame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.linearLayout_Game)) { v, insets ->
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
    }

    var difficulty = 0
    var goldburgs = 0.0
    private lateinit var textView_Score: TextView

    private lateinit var gameTimer_TextView: TextView
    var gameTimer_Long = 0

    private lateinit var button_Forfeit: ImageButton

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
    private lateinit var customerImage1Good_ImageView: Drawable
    private lateinit var customerImage1Bad_ImageView: Drawable
    private lateinit var customerImage2_ImageView: ImageView
    private lateinit var customerImage2Good_ImageView: Drawable
    private lateinit var customerImage2Bad_ImageView: Drawable
    private lateinit var customerImage3_ImageView: ImageView
    private lateinit var customerImage3Good_ImageView: Drawable
    private lateinit var customerImage3Bad_ImageView: Drawable
    private var customer1DialogueFiller_String = ""
    private var customer2DialogueFiller_String = ""
    private var customer3DialogueFiller_String = ""
    private var customer1DialogueIntro_String = ""
    private var customer2DialogueIntro_String = ""
    private var customer3DialogueIntro_String = ""
    private var customer1DialogueOutro_String = ""
    private var customer2DialogueOutro_String = ""
    private var customer3DialogueOutro_String = ""
    private var customer1DialogueGood_String = ""
    private var customer2DialogueGood_String = ""
    private var customer3DialogueGood_String = ""
    private var customer1DialogueBad_String = ""
    private var customer2DialogueBad_String = ""
    private var customer3DialogueBad_String = ""
    private var customer1Dialogue_String = ""
    private var customer2Dialogue_String = ""
    private var customer3Dialogue_String = ""
    private lateinit var customer1Equalizer: EqualizerDataClass
    private lateinit var customer2Equalizer: EqualizerDataClass
    private lateinit var customer3Equalizer: EqualizerDataClass
    private var customerServeNumber = 0

    private lateinit var customerServeName_TextView: TextView
    private lateinit var customerServeImage_ImageView: ImageView
    private lateinit var customerServeTimer_TextView: TextView
    private lateinit var customerServeDialogue_TextView: TextView

    private lateinit var backServeCustomer_Button: Button
    private lateinit var customerServeDrop_Button: Button
    private lateinit var customerServeDialog_Button: Button
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

    var customerDone = 0

    lateinit var layout_Menu: LinearLayout
    lateinit var imageView_Item: ImageView
    lateinit var textView_ItemName: TextView
    lateinit var textView_ItemPrice: TextView
    lateinit var textView_ItemDesc: TextView
    lateinit var textView_ItemLongDesc: TextView
    private lateinit var button_ItemBack: Button

    lateinit var linearLayout_Game: LinearLayout

    private fun init() {

        linearLayout_Game = findViewById(R.id.linearLayout_Game)
        linearLayout_Game.background = Drawable.createFromStream(assets.open("gui/MainMenuBackground.png"), null)

        difficulty = intent.getIntExtra("DIFFICULTY", 1)

        itemDatabase.deleteOrdered()

        textView_Score = findViewById(R.id.textView_Score)

        openShop_Layout = findViewById(R.id.openShop_Layout)
        openShop_Layout.visibility = View.VISIBLE
        val openShop_Button = findViewById<ImageButton>(R.id.openShop_Button)

        openShop_Button.setImageDrawable(Drawable.createFromStream(assets.open("gui/OpenShop.png"), null))
        openShop_Button.applyClickEffect(Color.WHITE, Color.LTGRAY)

        openShop_Button.setOnClickListener {
            openShop_Layout.visibility = View.GONE

            gameTimer_Long = 3050
            customerTimer1Duration_Long = 650
            customerTimer2Duration_Long = 680
            customerTimer3Duration_Long = 710

            loop()
        }

        gameTimer_TextView = findViewById(R.id.gameTimer_TextView)

        button_Forfeit = findViewById(R.id.button_Forfeit)
        button_Forfeit.setImageDrawable(Drawable.createFromStream(assets.open("gui/GameQuitJob.png"), null))
        button_Forfeit.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to quit? Your progress will be lost!")
                .setPositiveButton("Yes") { _, _ ->
                    startActivity(Intent(this, ActivityMainMenu::class.java))
                }
                .setNegativeButton("No", null)
                .show()
        }

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

        val customerServe_Layout = findViewById<LinearLayout>(R.id.customerServe_Layout)
        customerServe_Layout.visibility = View.VISIBLE

        customer1_CardView.setOnClickListener {
            if (customerTimer1Duration_Long in 1..600) {
                customerViews_Layout.visibility = View.GONE

                customerServeName_TextView.text = customerName1_TextView.text
                customerServeImage_ImageView.setImageDrawable(customerImage1_ImageView.drawable)
                customerServeDialogue_TextView.text = weightedRandom(listOf(customer1DialogueIntro_String to 0.5, customer1DialogueFiller_String to 0.4, customer1Dialogue_String to 0.1))
                customerServeNumber = 1
                customerServe_Layout.visibility = View.VISIBLE
            }
        }

        customer2_CardView.setOnClickListener {
            if (customerTimer2Duration_Long in 1..600) {
                customerViews_Layout.visibility = View.GONE

                customerServeName_TextView.text = customerName2_TextView.text
                customerServeImage_ImageView.setImageDrawable(customerImage2_ImageView.drawable)
                customerServeDialogue_TextView.text = weightedRandom(listOf(customer2DialogueIntro_String to 0.5, customer2DialogueFiller_String to 0.4, customer2Dialogue_String to 0.1))
                customerServeNumber = 2
                customerServe_Layout.visibility = View.VISIBLE
            }
        }

        customer3_CardView.setOnClickListener {
            if (customerTimer3Duration_Long in 1..600) {
                customerViews_Layout.visibility = View.GONE

                customerServeName_TextView.text = customerName3_TextView.text
                customerServeImage_ImageView.setImageDrawable(customerImage3_ImageView.drawable)
                customerServeDialogue_TextView.text = weightedRandom(listOf(customer3DialogueIntro_String to 0.5, customer3DialogueFiller_String to 0.4, customer3Dialogue_String to 0.1))
                customerServeNumber = 3
                customerServe_Layout.visibility = View.VISIBLE
            }
        }


        backServeCustomer_Button = findViewById(R.id.backServeCustomer_Button)
        backServeCustomer_Button.setOnClickListener {
            customerViews_Layout.visibility = View.VISIBLE

            updateTextViews()
            if (customerServeDrop_Button.visibility == View.GONE) {
                when (customerDone) {
                    1 -> {
                        customerTimer1Duration_Long = 0
                        updateCustomer(
                            1,
                            customerTimer1Duration_Long,
                            customerName1_TextView,
                            customerImage1_ImageView
                        )
                        customerTimer1Duration_Long = 650
                    }

                    2 -> {
                        customerTimer2Duration_Long = 0
                        updateCustomer(
                            2,
                            customerTimer2Duration_Long,
                            customerName2_TextView,
                            customerImage2_ImageView
                        )
                        customerTimer2Duration_Long = 650
                    }

                    3 -> {
                        customerTimer3Duration_Long = 0
                        updateCustomer(
                            3,
                            customerTimer3Duration_Long,
                            customerName3_TextView,
                            customerImage3_ImageView
                        )
                        customerTimer3Duration_Long = 650
                    }
                }

                customerServeDrop_Button.visibility = View.VISIBLE
                customerServeMenu_Button.visibility = View.VISIBLE
                customerServeDialog_Button.visibility = View.VISIBLE

                customerDone = 0
            }
        }

        customerServeMenu_Button = findViewById(R.id.openMenuServeCustomer_Button)
        customerServeMenu_Button.setOnClickListener {
            customerServe_Layout.visibility = View.GONE

            ordered = 0
            update(1)
        }

        customerServeDialog_Button = findViewById(R.id.dialogueCustomer_Button)
        customerServeDialog_Button.setOnClickListener {
            when (customerServeNumber) {
                1 -> {
                    when (customerServeDialogue_TextView.text) {
                        customer1DialogueFiller_String -> customerServeDialogue_TextView.text = customer1DialogueIntro_String
                        customer1DialogueIntro_String -> customerServeDialogue_TextView.text = customer1Dialogue_String
                        customer1Dialogue_String -> customerServeDialogue_TextView.text = customer1DialogueOutro_String
                        customer1DialogueOutro_String -> customerServeDialogue_TextView.text = customer1DialogueFiller_String
                    }
                }
                2 -> {
                    when (customerServeDialogue_TextView.text) {
                        customer2DialogueFiller_String -> customerServeDialogue_TextView.text = customer2DialogueIntro_String
                        customer2DialogueIntro_String -> customerServeDialogue_TextView.text = customer2Dialogue_String
                        customer2Dialogue_String -> customerServeDialogue_TextView.text = customer2DialogueOutro_String
                        customer2DialogueOutro_String -> customerServeDialogue_TextView.text = customer2DialogueFiller_String
                    }
                }
                3 -> {
                    when (customerServeDialogue_TextView.text) {
                        customer3DialogueFiller_String -> customerServeDialogue_TextView.text = customer3DialogueIntro_String
                        customer3DialogueIntro_String -> customerServeDialogue_TextView.text = customer3Dialogue_String
                        customer3Dialogue_String -> customerServeDialogue_TextView.text = customer3DialogueOutro_String
                        customer3DialogueOutro_String -> customerServeDialogue_TextView.text = customer3DialogueFiller_String
                    }
                }
            }
        }

        recyclerView = findViewById(R.id.cashier_RecyclerView)
        orderCount_TextView = findViewById(R.id.orderCount_TextView)
        orderTotalPrice_TextView = findViewById(R.id.orderTotalPrice_TextView)
        viewOrders_Button = findViewById(R.id.viewOrders_Button)
        giveOrders_Button = findViewById(R.id.giveOrders_Button)
        backToCustomer_Button = findViewById(R.id.backToCustomer_Button)
        customerTimerMenu_TextView = findViewById(R.id.customerTimerMenu_TextView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        update(0)

        viewOrders_Button.setOnClickListener {
            ordered = if (ordered == 0) 1 else 0
            viewOrders_Button.text = if (ordered == 0) "View Orders" else "View Menu"
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, (ordered != 0))
            update(1)
        }

        backToCustomer_Button.setOnClickListener {
            customerServe_Layout.visibility = View.VISIBLE
        }

        giveOrders_Button.setOnClickListener {
            customerServe_Layout.visibility = View.VISIBLE
            val equalizer = itemDatabase.compileOrderedToEqualizer()
            val orders = itemDatabase.getOrderedData(1)
            val totalPrice = orders.sumOf { it.price }
            itemDatabase.deleteOrdered()

            customerServeDrop_Button.visibility = View.GONE
            customerServeMenu_Button.visibility = View.GONE
            customerServeDialog_Button.visibility = View.GONE

            when (customerServeNumber) {
                1 -> {
                    val a = customer1Equalizer.compareWith(equalizer)
                    if (a == 0) {
                        goldburgs += totalPrice
                        customerServeDialogue_TextView.text = customer1DialogueGood_String
                        customerServeImage_ImageView.setImageDrawable(customerImage1Good_ImageView)
                    }
                    else {
                        goldburgs = maxOf(goldburgs - totalPrice, 0.0)
                        customerServeDialogue_TextView.text = customer1DialogueBad_String
                        customerServeImage_ImageView.setImageDrawable(customerImage1Bad_ImageView)
                    }
                    customerDone = 1
                    customerImage1_ImageView.setImageDrawable(Drawable.createFromStream(assets.open("customer/customer.png"), null))

//                    Toast.makeText(this, equalizerToListedForm(equalizer.items!!), Toast.LENGTH_SHORT).show()
//                    Toast.makeText(this, equalizerToListedForm(customer1Equalizer.items!!), Toast.LENGTH_SHORT).show()
                }
                2 -> {
                    val a = customer2Equalizer.compareWith(equalizer)
                    if (a == 0) {
                        goldburgs += totalPrice
                        customerServeDialogue_TextView.text = customer2DialogueGood_String
                        customerServeImage_ImageView.setImageDrawable(customerImage2Good_ImageView)
                    }
                    else {
                        goldburgs = maxOf(goldburgs - totalPrice, 0.0)
                        customerServeDialogue_TextView.text = customer2DialogueBad_String
                        customerServeImage_ImageView.setImageDrawable(customerImage2Bad_ImageView)
                    }
                    customerDone = 2
                    customerImage2_ImageView.setImageDrawable(Drawable.createFromStream(assets.open("customer/customer.png"), null))
                }
                3 -> {
                    val a = customer3Equalizer.compareWith(equalizer)
                    if (a == 0) {
                        goldburgs += totalPrice
                        customerServeDialogue_TextView.text = customer3DialogueGood_String
                        customerServeImage_ImageView.setImageDrawable(customerImage3Good_ImageView)
                    }
                    else {
                        goldburgs = maxOf(goldburgs - totalPrice, 0.0)
                        customerServeDialogue_TextView.text = customer3DialogueBad_String
                        customerServeImage_ImageView.setImageDrawable(customerImage3Bad_ImageView)
                    }
                    customerDone = 3
                    customerImage3_ImageView.setImageDrawable(Drawable.createFromStream(assets.open("customer/customer.png"), null))
                }
            }
        }

        customerServeDrop_Button = findViewById(R.id.dropServeCustomer_Button)
        customerServeDrop_Button.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirm Drop")
                .setMessage("Don't serve this customer?")
                .setPositiveButton("Yes") { _, _ ->
                    when (customerServeNumber) {
                        1 -> {
                            customerTimer1Duration_Long = 0
                            updateCustomer(1, customerTimer1Duration_Long, customerName1_TextView, customerImage1_ImageView)
                            customerTimer1Duration_Long = 750
                        }
                        2 -> {
                            customerTimer2Duration_Long = 0
                            updateCustomer(2, customerTimer2Duration_Long, customerName2_TextView, customerImage2_ImageView)
                            customerTimer2Duration_Long = 750
                        }
                        3 -> {
                            customerTimer3Duration_Long = 0
                            updateCustomer(3, customerTimer3Duration_Long, customerName3_TextView, customerImage3_ImageView)
                            customerTimer3Duration_Long = 750
                        }
                    }
                }
                .setNegativeButton("No", null) // Dismisses dialog
                .show()
        }

        layout_Menu = findViewById(R.id.layout_Menu)
        layout_Menu.visibility = View.VISIBLE
        imageView_Item = findViewById(R.id.imageView_Item)
        textView_ItemName = findViewById(R.id.textView_ItemName)
        textView_ItemPrice = findViewById(R.id.textView_ItemPrice)
        textView_ItemDesc = findViewById(R.id.textView_ItemDesc)
        textView_ItemLongDesc = findViewById(R.id.textView_ItemLongDesc)
        button_ItemBack = findViewById(R.id.button_ItemBack)

        button_ItemBack.setOnClickListener {
            layout_Menu.visibility = View.VISIBLE
        }
    }

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private fun loop() {
        handler = Handler(Looper.getMainLooper())

        val customerViews = this
        runnable = object : Runnable {
            override fun run() {
                updateTimers()
                textView_Score.text = "Sales: $goldburgs Goldburgs"

                if (gameTimer_Long == 0 && openShop_Layout.visibility == View.GONE) {
                    handler.removeCallbacks(this)

//                    Toast.makeText(customerViews, "message", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(customerViews, ActivityGameOver::class.java).putExtra("SCORE", goldburgs))
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

    override fun onPause() {
        super.onPause()
        if (openShop_Layout.visibility == View.GONE && gameTimer_Long != 0) {
            handler.removeCallbacks(runnable)
            startActivity(Intent(this, ActivityMainMenu::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        if (openShop_Layout.visibility == View.GONE && gameTimer_Long != 0) {
            handler.removeCallbacks(runnable)
            startActivity(Intent(this, ActivityMainMenu::class.java))
        }
    }

    fun updateTimers() {
        gameTimer_Long = maxOf(--gameTimer_Long, 0)
        gameTimer_TextView.text = "${kotlin.math.ceil(gameTimer_Long / 10.0).toInt()} second/s left"

        customerTimer1Duration_Long = if (customerTimer1Duration_Long == 0) 650 else --customerTimer1Duration_Long
        customerTimer2Duration_Long = if (customerTimer2Duration_Long == 0) 650 else --customerTimer2Duration_Long
        customerTimer3Duration_Long = if (customerTimer3Duration_Long == 0) 650 else --customerTimer3Duration_Long

        customerTimer1_TextView.text = if (customerTimer1Duration_Long in 0..600) "${kotlin.math.ceil(customerTimer1Duration_Long / 10.0).toInt()} second/s left" else ""
        customerTimer2_TextView.text = if (customerTimer2Duration_Long in 0..600) "${kotlin.math.ceil(customerTimer2Duration_Long / 10.0).toInt()} second/s left" else ""
        customerTimer3_TextView.text = if (customerTimer3Duration_Long in 0..600) "${kotlin.math.ceil(customerTimer3Duration_Long / 10.0).toInt()} second/s left" else ""
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
                imageView.setImageDrawable(Drawable.createFromStream(assets.open("customer/customer.png"), null))
            }
            630 -> {
                nameTextView.text = "A customer is arriving..."
            }
            600 -> {
                val customer = customerDatabase.getRandomCustomer()!!
                val customerPrefs = customerPrefsDatabase.getRandomCustomer()!!
                nameTextView.text = customerDatabase.getRandomCustomer()!!.name
                imageView.setImageDrawable(Drawable.createFromStream(assets.open(customer.image[0]), null))
                var choose = difficulty
//                when (difficulty) {
//                    1 -> choose = weightedRandom(listOf(1 to 0.6, 2 to 0.35, 3 to 0.05))
//                    2 -> choose = weightedRandom(listOf(1 to 0.35, 2 to 0.45, 3 to 0.2))
//                    3 -> choose = weightedRandom(listOf(1 to 0.2, 2 to 0.4, 3 to 0.4))
//                }
//                choose = 1 // force easy
                when (customerIndex) {
                    1 -> {
                        when (choose) {
                            1 -> {
                                val easy = easyGenerator(4)
                                customer1Equalizer = EqualizerDataClass(items = easy)
                                customer1Dialogue_String = equalizerToListedForm(easy)
                            }
                            2 -> {
                                val easy = easyGenerator(10)
                                customer1Dialogue_String = equalizerToListedForm(easy)
                                customer1Equalizer = EqualizerDataClass(items = easy)
                            }
                            3 -> {
                                val easy = easyGenerator(20)
                                customer1Dialogue_String = equalizerToListedForm(easy)
                                customer1Equalizer = EqualizerDataClass(items = easy)
                            }
                        }
                        customer1DialogueFiller_String = customerDatabase.getRandomCustomer()!!.dialogFiller
                        customer1DialogueIntro_String = customerDatabase.getRandomCustomer()!!.dialogIntro
                        customer1DialogueOutro_String = customerDatabase.getRandomCustomer()!!.dialogOutro
                        customer1DialogueGood_String = customerDatabase.getRandomCustomer()!!.dialogGood
                        customer1DialogueBad_String = customerDatabase.getRandomCustomer()!!.dialogBad
                        customerImage1_ImageView.setImageDrawable(Drawable.createFromStream(assets.open(customer.image[0]), null))
                        customerImage1Good_ImageView = Drawable.createFromStream(assets.open(customer.image[1]), null)!!
                        customerImage1Bad_ImageView = Drawable.createFromStream(assets.open(customer.image[2]), null)!!
                    }
                    2 -> {
                        when (choose) {
                            1 -> {
                                val easy = easyGenerator(4)
                                customer2Equalizer = EqualizerDataClass(items = easy)
                                customer2Dialogue_String = equalizerToListedForm(easy)
                            }
                            2 -> {
                                val easy = easyGenerator(10)
                                customer2Dialogue_String = equalizerToListedForm(easy)
                                customer2Equalizer = EqualizerDataClass(items = easy)
                            }
                            3 -> {
                                val easy = easyGenerator(20)
                                customer2Dialogue_String = equalizerToListedForm(easy)
                                customer2Equalizer = EqualizerDataClass(items = easy)
                            }
                        }
                        customer2DialogueFiller_String = customerDatabase.getRandomCustomer()!!.dialogFiller
                        customer2DialogueIntro_String = customerDatabase.getRandomCustomer()!!.dialogIntro
                        customer2DialogueOutro_String = customerDatabase.getRandomCustomer()!!.dialogOutro
                        customer2DialogueGood_String = customerDatabase.getRandomCustomer()!!.dialogGood
                        customer2DialogueBad_String = customerDatabase.getRandomCustomer()!!.dialogBad
                        customerImage2_ImageView.setImageDrawable(Drawable.createFromStream(assets.open(customer.image[0]), null))
                        customerImage2Good_ImageView = Drawable.createFromStream(assets.open(customer.image[1]), null)!!
                        customerImage2Bad_ImageView = Drawable.createFromStream(assets.open(customer.image[2]), null)!!
                    }
                    3 -> {
                        when (choose) {
                            1 -> {
                                val easy = easyGenerator(4)
                                customer3Equalizer = EqualizerDataClass(items = easy)
                                customer3Dialogue_String = equalizerToListedForm(easy)
                            }
                            2 -> {
                                val easy = easyGenerator(10)
                                customer3Dialogue_String = equalizerToListedForm(easy)
                                customer3Equalizer = EqualizerDataClass(items = easy)
                            }
                            3 -> {
                                val easy = easyGenerator(20)
                                customer3Dialogue_String = equalizerToListedForm(easy)
                                customer3Equalizer = EqualizerDataClass(items = easy)
                            }
                        }
                        customer3DialogueFiller_String = customerDatabase.getRandomCustomer()!!.dialogFiller
                        customer3DialogueIntro_String = customerDatabase.getRandomCustomer()!!.dialogIntro
                        customer3DialogueOutro_String = customerDatabase.getRandomCustomer()!!.dialogOutro
                        customer3DialogueGood_String = customerDatabase.getRandomCustomer()!!.dialogGood
                        customer3DialogueBad_String = customerDatabase.getRandomCustomer()!!.dialogBad
                        customerImage3_ImageView.setImageDrawable(Drawable.createFromStream(assets.open(customer.image[0]), null))
                        customerImage3Good_ImageView = Drawable.createFromStream(assets.open(customer.image[1]), null)!!
                        customerImage3Bad_ImageView = Drawable.createFromStream(assets.open(customer.image[2]), null)!!
                    }
                }
            }
        }
    }

    fun <T> weightedRandom(items: List<Pair<T, Double>>): T {
        val totalWeight = items.sumOf { it.second }
        val randomValue = Random.nextDouble(totalWeight)

        var cumulativeWeight = 0.0
        for ((item, weight) in items) {
            cumulativeWeight += weight
            if (randomValue < cumulativeWeight) {
                return item
            }
        }

        throw IllegalArgumentException("Items list cannot be empty")
    }

    private fun easyGenerator(maxItems: Int): List<Pair<String, Int>> {
        val items = listOf(
            "Classic Burger", "Cheese Burger", "Porkinator", "The Kurumburger",
            "The Denburger", "The Juliussaucy", "The Ronin Deluxe", "Classic Fry Bucket",
            "Cheese Fry Bucket", "Onion Rings", "Chicken Fingers", "Milktea",
            "Matcha Milktea", "Taro Milktea", "Wintermelon Milktea"
        )

        val itemQuantities = mutableMapOf<String, Int>()

        // Ensure at least one item is included
        val guaranteedItem = items.random()
        itemQuantities[guaranteedItem] = 1

        // Generate random probabilities for each item (0.1 to 0.9 for controlled randomness)
        val probabilities = items.associateWith { Random.nextDouble(0.1, 0.9) }

        // Select up to maxItems items, allowing duplicates (but increasing quantity instead)
        repeat(maxItems - 1) {
            val item = items.random() // Random item selection

            if (Random.nextDouble() < probabilities[item]!!) {
                itemQuantities[item] = itemQuantities.getOrDefault(item, 0) + 1
            }
        }

        return itemQuantities.toList()
    }

    private fun equalizerToListedForm(items: List<Pair<String, Int>>): String {
        return items.joinToString(", ") { "${it.first} x${it.second}" }
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Exit")
            .setMessage("Are you sure you want to go? Progress will be lost!")
            .setPositiveButton("Yes") { _, _ -> finish() } // Closes activity
            .setNegativeButton("No", null) // Dismisses dialog
            .show()
    }

    fun update(shuffle: Int) {
        val allItems = itemDatabase.getOrderedData(ordered)

        var shuffledList = allItems
        if (shuffle == 1) {
// Separate ordered and non-ordered items
            val orderedItems = allItems.filter { it.ordered == 1 }
            val nonOrderedItems =
                allItems.filter { it.ordered == 0 }.shuffled() // Shuffle only unordered items

// Merge them back
            shuffledList = orderedItems + nonOrderedItems
        }

        val adapter = SampleAdapter(this, shuffledList, itemDatabase, this) { item ->
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
//        Snackbar.make(findViewById(android.R.id.content), "${item.name} added to cart", Snackbar.LENGTH_SHORT).show()
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