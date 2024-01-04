package com.example.quacksrandomizer.data

import com.example.quacksrandomizer.R

object BookColorRepository {
    fun getAllColors(): List<BookColor> {
        return listOf(blue, red, green, yellow, purple, orange, black)
    }
    fun getColorByName(name: String): BookColor? {
        return when (name) {
            "Blue" -> blue
            "Red" -> red
            "Green" -> green
            "Yellow" -> yellow
            "Purple" -> purple
            "Orange" -> orange
            "Black" -> black
            else -> null
        }
    }
    private val blue = BookColor("Blue",
        listOf("Crow Skull 1", "Crow Skull 2", "Crow Skull 3", "Crow Skull 4"),
        listOf(R.drawable.blue1, R.drawable.blue2, R.drawable.blue3, R.drawable.blue4))
    private val red = BookColor("Red",
        listOf("Toadstool 1", "Toadstool 2", "Toadstool 3", "Toadstool 4"),
        listOf(R.drawable.red1, R.drawable.red2, R.drawable.red3, R.drawable.red4))
    private val green = BookColor("Green",
        listOf("Garden Spider 1", "Garden Spider 2", "Garden Spider 3", "Garden Spider 4"),
        listOf(R.drawable.green1, R.drawable.green2, R.drawable.green3, R.drawable.green4))
    private val yellow = BookColor("Yellow",
        listOf("Mandrake 1", "Mandrake 2", "Mandrake 3", "Mandrake 4"),
        listOf(R.drawable.yellow1, R.drawable.yellow2, R.drawable.yellow3, R.drawable.yellow4))
    private val purple = BookColor("Purple",
        listOf("Ghost's Breath 1", "Ghost's Breath 2", "Ghost's Breath 3", "Ghost's Breath 4"),
        listOf(R.drawable.purple1, R.drawable.purple2, R.drawable.purple3, R.drawable.purple4))
    private val orange = BookColor("Orange",
        listOf("Pumpkin"),
        listOf(R.drawable.orange1))
    private val black = BookColor("Black",
        listOf("African Death's Head Hawkmoth 1", "African Death's Head Hawkmoth 2"),
        listOf(R.drawable.black1, R.drawable.black2))
}