package com.susumunoda.kansha.data

object DataSource {
    fun allMessages() = listOf(
        Message(User.BOB, User.ALICE, "Thanks for your help!"),
        Message(User.BOB, User.ALICE, "Dinner was delicious :)"),
        Message(User.ALICE, User.BOB, "Thanks for a fun weekend :D"),
        Message(User.BOB, User.BILL, "Appreciate the help moving"),
        Message(User.ALICE, User.BOB, "What a thoughtful gift!"),
        Message(User.BOB, User.ALICE, "Thank you for the birthday card ❤️"),
        Message(User.ALICE, User.JANE, "Thank you for the ride yesterday!"),
        Message(User.ALICE, User.BOB, "Thanks for getting us tickets!"),
        Message(User.BILL, User.JANE, "That was a fun game night!"),
        Message(User.JANE, User.CHRIS, "Thanks for the coffee ☕️"),
        Message(User.CHRIS, User.MARGE, "Thanks for your help!"),
        Message(User.CHRIS, User.MARGE, "Dinner was delicious :)"),
        Message(User.MARGE, User.CHRIS, "Thanks for a fun weekend :D"),
        Message(User.CHRIS, User.BILL, "Appreciate the help moving"),
        Message(User.MARGE, User.CHRIS, "What a thoughtful gift!"),
        Message(User.CHRIS, User.MARGE, "Thank you for the birthday card ❤️"),
        Message(User.MARGE, User.JANE, "Thank you for the ride yesterday!"),
        Message(User.MARGE, User.CHRIS, "Thanks for getting us tickets!"),
    )

    fun allUsers() = allMessages().map { it.sender }.distinct().sortedBy { it.name }
}