package com.susumunoda.kansha.data

data class Message(
    val sender: User,
    val recipient: User,
    val message: String
)

fun List<Message>.filterBySender(sender: User) = filter { it.sender.id == sender.id }

fun List<Message>.filterByRecipient(recipient: User) = filter { it.recipient.id == recipient.id }