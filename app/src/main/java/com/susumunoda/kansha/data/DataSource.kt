package com.susumunoda.kansha.data

import com.susumunoda.kansha.R

object DataSource {
    fun allMessages() = listOf(
        Message(R.drawable.bob, "Bob", "Alice", "Thanks for your help!"),
        Message(R.drawable.bob, "Bob", "Alice", "Dinner was delicious :)"),
        Message(R.drawable.alice, "Alice", "Bob", "Thanks for a fun weekend :D"),
        Message(R.drawable.bob, "Bob", "Bill", "Appreciate the help moving"),
        Message(R.drawable.alice, "Alice", "Bob", "What a thoughtful gift!"),
        Message(R.drawable.bob, "Bob", "Alice", "Thank you for the birthday card ❤️"),
        Message(R.drawable.alice, "Alice", "Jane", "Thank you for the ride yesterday!"),
        Message(R.drawable.alice, "Alice", "Bob", "Thanks for getting us tickets!"),
        Message(R.drawable.bill, "Bill", "Jane", "That was a fun game night!"),
        Message(R.drawable.jane, "Jane", "Chris", "Thanks for the coffee ☕️"),
        Message(R.drawable.chris, "Chris", "Marge", "Thanks for your help!"),
        Message(R.drawable.chris, "Chris", "Marge", "Dinner was delicious :)"),
        Message(R.drawable.marge, "Marge", "Chris", "Thanks for a fun weekend :D"),
        Message(R.drawable.chris, "Chris", "Bill", "Appreciate the help moving"),
        Message(R.drawable.marge, "Marge", "Chris", "What a thoughtful gift!"),
        Message(R.drawable.chris, "Chris", "Marge", "Thank you for the birthday card ❤️"),
        Message(R.drawable.marge, "Marge", "Jane", "Thank you for the ride yesterday!"),
        Message(R.drawable.marge, "Marge", "Chris", "Thanks for getting us tickets!"),
    )
}