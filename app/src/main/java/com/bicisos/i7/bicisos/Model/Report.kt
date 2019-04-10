package com.bicisos.i7.bicisos.Model

data class Report (val id: String, val name: String, val serie: String, val description: String, val estatus: Int, val date: String)
{
    constructor() : this ("","","","",-1,"")
}