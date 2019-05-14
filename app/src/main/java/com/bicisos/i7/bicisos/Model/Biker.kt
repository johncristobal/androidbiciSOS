package com.bicisos.i7.bicisos.Model

import java.io.Serializable

data class Biker (
    val id: String,
    val name: String,
    val bici: Int,
    val latitud: Double,
    val longitude: Double) : Serializable {
    constructor() : this ("","",-1,0.0,0.0)
}