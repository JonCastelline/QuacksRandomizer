package com.example.quacksrandomizer.data

import com.example.quacksrandomizer.R

data class BookColor(
    val name: String,
    val options: List<String>,
    val imageResourceIds: List<Int>
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
}
