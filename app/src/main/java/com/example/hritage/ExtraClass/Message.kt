package com.example.hritage.ExtraClass

class Message(var message: String, var sentBy: String) {

    companion object {
        var SENT_BY_ME = "me"
        var SENT_BY_BOT = "bot"
    }
}