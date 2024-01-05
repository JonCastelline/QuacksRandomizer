package com.example.quacksrandomizer.data

import com.example.quacksrandomizer.R

data class BookColor(
    val name: String,
    val options: List<String>,
    val imageResourceIds: List<Int>,
    var selectedOption: Int? = null
) {
    fun getRandomOption(): Int {
        return (imageResourceIds.indices).random()
    }

    fun getImageResourceId(selectedOption: Int): Int {
        return if (selectedOption != -1 && selectedOption < imageResourceIds.size) {
            imageResourceIds[selectedOption]
        } else {
            R.drawable.unabletofind
        }
    }

    fun getColorResId(): Int {
        return when (name) {
            "Blue" -> R.color.blueColor
            "Red" -> R.color.redColor
            "Green" -> R.color.greenColor
            "Yellow" -> R.color.yellowColor
            "Purple" -> R.color.purpleColor
            "Orange" -> R.color.orangeColor
            "Black" -> R.color.blackColor
            else -> R.color.defaultColor
        }
    }

    override fun toString(): String {
        return name
    }
}
