package com.susumunoda.kansha.data

data class Message(val from: String, val to: String, val message: String)

fun List<Message>.filterBySender(sender: String) {
    filter { it.from == sender }
}

fun List<Message>.filterByRecipient(recipient: String) {
    filter { it.to == recipient }
}