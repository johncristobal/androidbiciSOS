package com.bicisos.i7.bicisos.model.reportes

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Reporte (
    val iduser: String,
    @SerializedName("type_report")
    val typeReport: String,
    val id: String,

    val latitude: String,
    val longitude: String,
    val description: String,
    val robery: Robery?
) : Serializable

data class Robery (
    val serie: String,
    val photos: List<String>
)
