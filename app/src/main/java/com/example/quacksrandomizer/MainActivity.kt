package com.example.quacksrandomizer

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.quacksrandomizer.data.BookColor
import com.example.quacksrandomizer.data.BookColorRepository

class MainActivity : AppCompatActivity() {

    private var selectedPlayers = 2 // Default to 2 players
    private var blackBookImageResourceId: Int = 0

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

                // Store the black book image resource ID based on player count
                val imgBlack: ImageView = findViewById(R.id.imgBlack)
                // Check if the black ImageView is currently visible
                val isBlackVisible = imgBlack.visibility == View.VISIBLE
                blackBookImageResourceId = if (selectedPlayers >= 3) {
                    BookColorRepository.getColorByName("Black")?.getImageResourceId(1) ?: 0
                } else {
                    BookColorRepository.getColorByName("Black")?.getImageResourceId(0) ?: 0
                }
                // If the black ImageView is already visible, update its displayed image
                if (isBlackVisible) {
                    imgBlack.setImageResource(blackBookImageResourceId)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Get references to UI elements
        val btnRandomizeAll: Button = findViewById(R.id.btnRandomizeAll)

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

        val btnSelectColors: Button = findViewById(R.id.btnSelectColors)
        btnSelectColors.setOnClickListener {
            showColorSelectionDialog()
        }
    }
    private fun displayBookColors(bookColors: List<BookColor>) {
        // Randomize each book color and display the corresponding image
        for (color in bookColors) {
            val optionToDisplay = color.selectedOption ?: color.getRandomOption()
            val imageId = if (color.name == "Black") {
                blackBookImageResourceId
            } else {
                color.getImageResourceId(optionToDisplay)
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

    fun onLockColorsButtonClick() {
        val colors = BookColorRepository.getAllColors().filter { it.name != "Orange" && it.name != "Black" }
        val colorNames = colors.map { it.name }.toTypedArray()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Colors to Lock")
            .setMultiChoiceItems(colorNames, null) { dialog, which, isChecked ->
                // Handle the user's selection (if needed)
            }
            .setPositiveButton("OK") { dialog, which ->
                // Handle the OK button click
                val selectedColors = colors.filterIndexed { index, _ ->
                    (dialog as AlertDialog).listView.isItemChecked(index)
                }
                // Handle the selected colors
            }
            .setNegativeButton("Cancel", null)
            .create()

        builder.show()
    }

    private fun showColorSelectionDialog() {
        val colors = BookColorRepository.getAllColors()

        // Filter out Orange and Black
        val filteredColors = colors.filter { it.name !in listOf("Orange", "Black") }

        // Check if any color has a selected option
        val hasSelectedOption = filteredColors.any { it.selectedOption != null }

        // Create a list of color names
        val colorNames = filteredColors.map { it.name }.toMutableList()

        // Add "Clear All" option if at least one color has a selected option
        if (hasSelectedOption) {
            colorNames.add("Clear All")
        }

        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            colorNames
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                val colorName = getItem(position)

                // Check if the color has a selected option
                if (colorName != null && filteredColors.any { it.name == colorName && it.selectedOption != null }) {
                    // Apply bold and color to the text
                    view.setTypeface(null, Typeface.BOLD)
                    view.setTextColor(ContextCompat.getColor(context, filteredColors.first { it.name == colorName }.getColorResId()))
                } else {
                    // Reset styles for other items
                    view.setTypeface(null, Typeface.NORMAL)
                    view.setTextColor(resources.getColor(R.color.defaultColor, null))
                }

                return view
            }
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select a Color")
            .setAdapter(adapter) { _, which ->
                if (which < filteredColors.size) {
                    val selectedColor = filteredColors[which]
                    showOptionSelectionDialog(selectedColor)
                } else {
                    // "Clear All" option selected, clear all selected options
                    filteredColors.forEach { it.selectedOption = null }
                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged()
                }
            }

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(R.color.lightGrayBackground)
    }

    private fun showOptionSelectionDialog(selectedColor: BookColor) {
        val options = selectedColor.options.toMutableList()
        if (selectedColor.selectedOption != null) {
            options.add("Clear Selection")
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select an Option")
            .setItems(options.toTypedArray()) { _, which ->

                if (which < selectedColor.options.size) {
                    // Store the selected option for later use
                    selectedColor.selectedOption = which

                    val imageViewId = resources.getIdentifier("img${selectedColor.name}", "id", packageName)
                    val imageView: ImageView = findViewById(imageViewId)
                    if (imageView.isVisible) {
                        val imageResourceId = selectedColor.getImageResourceId(which)
                        imageView.setImageResource(imageResourceId)
                    }
                } else {
                    // Clear the selected option
                    selectedColor.selectedOption = null
                }
            }

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(R.color.lightGrayBackground)
    }
}
