package edu.harrisburgu.cisc349.adaptertrick

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    var english: Array<String> = arrayOf(
        "One",
        "Two",
        "Three",
        "Four",
        "Five",
        "Six",
        "Seven",
        "Eight",
        "Nine"
    )

    var spanish: Array<String> = arrayOf(
        "Uno",
        "Dos",
        "Tres",
        "Quatro",
        "Cinco",
        "Seis",
        "Siete",
        "Ocho",
        "Nueve"
    )

    var useEnglish: Boolean = true

    var englishAdapter: ArrayAdapter<String>? = null
    var spanishAdapter: ArrayAdapter<String>? = null

    var list: ListView? = null
    var switchButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        list = findViewById<View>(R.id.list_view) as ListView
        englishAdapter = ArrayAdapter(this, R.layout.list_item, english)
        spanishAdapter = ArrayAdapter(this, R.layout.list_item, spanish)
        if (useEnglish) list!!.adapter = englishAdapter
        else list!!.adapter = spanishAdapter

        switchButton = findViewById<View>(R.id.switch_button) as Button

        switchButton!!.setOnClickListener {
            useEnglish = !useEnglish
            if (useEnglish) list!!.adapter = englishAdapter
            else list!!.adapter = spanishAdapter
        }
    }
}