package com.example.spotibae.Models

class Messages {
    var message: String? = null
    var senderId: String? = null
    var timestamp: Long = 0
    var currenttime: String? = null

    constructor() {}
    constructor(message: String?, senderId: String?, timestamp: Long, currenttime: String?) {
        this.message = message
        this.senderId = senderId
        this.timestamp = timestamp
        this.currenttime = currenttime
    }

    override fun toString(): String {
        return "$message $senderId $timestamp $currenttime"
    }
}