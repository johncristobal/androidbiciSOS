package com.bicisos.i7.bicisos.model

data class InfoWindowData (
    val id: String,
    val name: String,
    val serie: String,
    val description: String,
    val estatus: Int,
    val date: String,
    val fotos: String,
    val tipo:Int,
    val latitude: Double,
    val longitude: Double)