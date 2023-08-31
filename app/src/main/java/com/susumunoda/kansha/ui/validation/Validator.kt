package com.susumunoda.kansha.ui.validation

abstract class Validator<T>(private val validationMessage: String) {
    fun validate(value: T) = if (isValid(value)) null else validationMessage
    abstract fun isValid(value: T): Boolean
}

abstract class StringValidator(validationMessage: String) : Validator<String>(validationMessage)