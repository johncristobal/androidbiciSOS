package com.bicisos.i7.bicisos.model.reportes

data class Reportes (
    val status: Boolean,
    val msg: String,
    val error: String,
    val reportes: List<Reporte>
)

