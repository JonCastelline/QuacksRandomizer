package com.example.quacksrandomizer

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.quacksrandomizer.data.BookColor
import com.example.quacksrandomizer.data.BookColorRepository
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    private var selectedPlayers = 2 // Default to 2 players

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up the number of players dropdown
        val spinnerPlayers: Spinner = findViewById(R.id.spinnerPlayers)
        ArrayAdapter.createFromResource(
            this,
            R.array.players_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerPlayers.adapter = adapter
        }

        // Handle spinner item selection
        spinnerPlayers.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                selectedPlayers = position + 2 // +2 because the array starts from 0 and represents 2, 3, 4 players
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Get references to UI elements
        val btnRandomizeAll: Button = findViewById(R.id.btnRandomizeAll)
        val llBookColorImages: LinearLayout = findViewById(R.id.llBookColorImages)

        // Initialize the visibility of all ImageViews to GONE

        for (bookColor in BookColorRepository.getAllColors()) {
            val imageViewId = resources.getIdentifier("img${bookColor.name}", "id", packageName)
            val imageView: ImageView = findViewById(imageViewId)
            imageView.visibility = View.GONE
        }

        // Set up the Randomize All button click listener
        btnRandomizeAll.setOnClickListener {
            // Get all book colors
            val allColors = BookColorRepository.getAllColors()

            // Randomize and display book colors
            displayBookColors(allColors)
        }
    }
    private fun displayBookColors(bookColors: List<BookColor>) {
        // Randomize each book color and display the corresponding image
        for (color in bookColors) {
            val randomOption = color.getRandomOption()
            val imageId = if (color.name == "Black") {
                if (selectedPlayers >= 3) {
                    color.getImageResourceId(1) // Use black2.png for 3 or more players
                } else {
                    color.getImageResourceId(0) // Use black1.png for 2 players
                }
            } else {
                color.getImageResourceId(randomOption)
            }
            // Find the corresponding ImageView based on the color
            val imageViewId = resources.getIdentifier("img${color.name}", "id", packageName)
            val imageView: ImageView = findViewById(imageViewId)

            // Set the visibility of the ImageView
            if (imageId != R.drawable.unabletofind) {
                // Set the image resource if it's not the unabletofind image
                imageView.setImageResource(imageId)
                imageView.visibility = View.VISIBLE
            } else {
                // Set the unabletofind image if the image resource is unabletofind
                imageView.setImageResource(R.drawable.unabletofind)
                imageView.visibility = View.VISIBLE
            }
        }
    }
}
