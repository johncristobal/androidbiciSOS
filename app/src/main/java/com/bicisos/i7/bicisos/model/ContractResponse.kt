package com.bicisos.i7.bicisos.model

data class ContractResponse (
    val msg: String,
    val email: Email
)

data class Email (
    val accepted: List<String>,
    val rejected: List<String>,
    val envelopeTime: Long,
    val messageTime: Long,
    val messageSize: Long,
    val response: String,
    val envelope: Envelope,
    val messageID: String
)

data class Envelope (
    val from: String,
    val to: List<String>
)

