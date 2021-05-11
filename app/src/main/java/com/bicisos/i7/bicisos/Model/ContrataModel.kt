package com.bicisos.i7.bicisos.Model

import java.io.Serializable

// To parse the JSON, install Klaxon and do:
//
//   val contrataModel = ContrataModel.fromJson(jsonString)

data class ContrataModel (
    var ejecutivo: String,
    var fechaContratacion: String,
    var nombreTitular: String,
    var fechaNacimiento: String,
    var rfc: String,
    var telefono: String,
    var correo: String,
    var direccion: String,
    var cp: String,
    var colonia: String,
    var alcaldia: String
) : Serializable {
    constructor() : this ("","","","","","","","","","","")

}
