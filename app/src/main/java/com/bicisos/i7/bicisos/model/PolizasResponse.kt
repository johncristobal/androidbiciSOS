package com.bicisos.i7.bicisos.model

import com.bicisos.i7.bicisos.model.polizas.Polizas

data class PolizasResponse (
    val msg: String,
    val status: Boolean,
    val polizas: Polizas
)
