package com.bicisos.i7.bicisos.Model

import java.io.Serializable

data class Report (
    val id: String,
    val name: String,
    val serie: String,
    val description: String,
    val estatus: Int,
    val date: String,
    val fotos: String,
    val tipo:Int,
    val latitude: Double,
    val longitude: Double) : Serializable {

    constructor() : this ("","","","",-1,"","",-1,0.0,0.0)
}