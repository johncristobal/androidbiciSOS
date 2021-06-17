package com.bicisos.i7.bicisos.model.polizas

data class Polizas (
    val estado: Boolean,
    val id: String,
    val cliente: Cliente,
    val polizasInfo: List<PolizasInfo>,
    val correo: String,
    val telefono: String,
    val fechaUpdate: String,
    val direccion: Direccion,
    val v: Long
)