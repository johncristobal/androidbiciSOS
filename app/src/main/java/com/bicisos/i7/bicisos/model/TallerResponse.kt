package com.bicisos.i7.bicisos.model

data class TallerResponse (
    val ok: Boolean,
    val talleres: List<Tallere>
)

data class Tallere (
    val nombre: String,
    val descripcion: String,
    val latitud: String,
    val longitud: String,
    val activo: Boolean
)
