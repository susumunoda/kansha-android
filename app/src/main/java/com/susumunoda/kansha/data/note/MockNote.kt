package com.susumunoda.kansha.data.note

import java.util.Calendar
import java.util.Date

data class MockNote(
    override val createdAt: Date = Calendar.getInstance().time,
    override val message: String = "",
    override val labels: List<Label> = emptyList()
) : Note {
    class Builder private constructor(
        private val message: String,
        private val labels: List<Label>
    ) {
        constructor() : this("", emptyList())

        companion object {
            private const val MESSAGE =
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras pulvinar rhoncus magna, sit amet mollis lorem consequat at. Proin eu fermentum odio. Maecenas vitae convallis ante, eu facilisis sapien. Nulla condimentum neque at ante elementum, vel efficitur ipsum rutrum. Praesent metus quam, ullamcorper at orci quis, dictum accumsan dui. Cras suscipit, eros in viverra pretium, massa nulla consectetur felis, at posuere ex ex id nisi. Nunc consequat lobortis venenatis. Quisque massa quam, tristique fringilla ligula ut, consequat viverra erat."
            private val SHORT_MESSAGE = MESSAGE.substring(0, 10)
            private val MEDIUM_MESSAGE = MESSAGE.substring(0, 50)
            private val LONG_MESSAGE = MESSAGE.substring(0, 200)
        }

        fun message(message: String) = Builder(message, labels)
        fun shortMessage() = message(SHORT_MESSAGE)
        fun mediumMessage() = message(MEDIUM_MESSAGE)
        fun longMessage() = message(LONG_MESSAGE)
        fun labels(vararg labels: Label) = Builder(message, labels.asList())
        fun build() = MockNote(message = message, labels = labels)
    }
}
