package com.susumunoda.kansha.ui.screen

abstract class Validator {
    fun validate(value: String) = if (isValid(value)) null else validationMessage()
    abstract fun isValid(value: String): Boolean
    abstract fun validationMessage(): String
}