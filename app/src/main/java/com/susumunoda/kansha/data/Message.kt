package com.susumunoda.kansha.data

import androidx.annotation.DrawableRes

data class Message(
    @DrawableRes val senderPhotoId: Int,
    val sender: String,
    val recipient: String,
    val message: String
)

fun List<Message>.filterBySender(sender: String) = filter { it.sender == sender }

fun List<Message>.filterByRecipient(recipient: String) = filter { it.recipient == recipient }