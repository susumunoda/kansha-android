package com.susumunoda.kansha.data

import androidx.annotation.DrawableRes
import com.susumunoda.kansha.R

data class User(val id: Int, val name: String, @DrawableRes val profilePhotoId: Int) {
    companion object {
        val NONE = User(-1, "", R.drawable.blank_profile_picture)
        val ALICE = User(0, "Alice", R.drawable.alice)
        val BILL = User(1, "Bill", R.drawable.bill)
        val BOB = User(2, "Bob", R.drawable.bob)
        val CHRIS = User(3, "Chris", R.drawable.chris)
        val JANE = User(4, "Jane", R.drawable.jane)
        val MARGE = User(5, "Marge", R.drawable.marge)
    }
}

fun List<User>.filterByNameStartsWith(startsWith: String) =
    filter { it.name.lowercase().startsWith(startsWith.lowercase()) }