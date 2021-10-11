package com.bicisos.i7.bicisos.model.reportes

import com.google.gson.annotations.SerializedName

data class Reporte (
    val iduser: String,
    @SerializedName("type_report")
    val typeReport: String,

    val latitude: String,
    val longitude: String,
    val description: String,
    val robery: Robery?
)

data class Robery (
    val serie: String,
    val photos: List<String>
)
