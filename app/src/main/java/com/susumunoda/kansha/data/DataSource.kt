package com.susumunoda.kansha.data

object DataSource {
    fun allMessages() = listOf(
        Message("Bob", "Alice", "Thanks for your help!"),
        Message("Bob", "Alice", "Dinner was delicious :)"),
        Message("Alice", "Bob", "Thanks for a fun weekend :D"),
        Message("Bob", "Bill", "Appreciate the help moving"),
        Message("Alice", "Bob", "What a thoughtful gift!"),
        Message("Bob", "Alice", "Thank you for the birthday card ❤️"),
        Message("Alice", "Jane", "Thank you for the ride yesterday!"),
        Message("Alice", "Bob", "That was a great play - thanks for getting us tickets!")
    )
}