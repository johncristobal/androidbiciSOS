package com.bicisos.i7.bicisos.model

data class ErrorResponse(
    val msg: String,
    val status: Boolean,
    val error: String? = ""
)
