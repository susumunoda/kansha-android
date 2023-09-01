package com.susumunoda.kansha.repository.category

import com.google.firebase.firestore.Exclude

data class FirebaseCategory(
    // var because ID is not a field in the document and will be added dynamically after retrieval
    // @get:Exclude because we don't want to save this field in the document on write
    @get:Exclude override var id: String = "",
    override val order: Int = 0,
    override val name: String = "",
    override val photoUrl: String = ""
) : Category
