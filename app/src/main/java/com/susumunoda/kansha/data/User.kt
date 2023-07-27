package com.susumunoda.kansha.data

import androidx.annotation.DrawableRes
import com.susumunoda.kansha.R

data class User(val id: Int, val name: String, @DrawableRes val profilePhotoId: Int) {
    companion object {
        val ALICE = User(1, "Alice", R.drawable.alice)
        val BILL = User(2, "Bill", R.drawable.bill)
        val BOB = User(3, "Bob", R.drawable.bob)
        val CHRIS = User(4, "Chris", R.drawable.chris)
        val JANE = User(5, "Jane", R.drawable.jane)
        val MARGE = User(6, "Marge", R.drawable.marge)
    }
}

fun List<User>.filterByNameStartsWith(startsWith: String) =
    filter { it.name.lowercase().startsWith(startsWith.lowercase()) }