package com.bicisos.i7.bicisos.Model

data class Report (val id: String, val imageUrl: String, val textTitle: String)
{
    constructor() : this ("","","")
}