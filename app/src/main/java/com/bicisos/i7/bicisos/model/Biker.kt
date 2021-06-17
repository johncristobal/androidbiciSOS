package com.bicisos.i7.bicisos.model

import com.google.firebase.database.Exclude
import java.io.Serializable

data class Biker (
    val id: String,
    val name: String,
    val bici: Int,
    val latitud: Double,
    val longitude: Double) : Serializable {

    constructor() : this ("","",-1,0.0,0.0)

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "bici" to bici,
            "latitud" to latitud,
            "longitude" to longitude
        )
    }
}