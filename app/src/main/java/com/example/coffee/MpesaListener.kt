package com.example.coffee

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.Transaction

interface MpesaListener {

    fun sendFailed(reason: String)
    fun sendSuccessful(amount: String, phone: String, date: String, receipt: String)
}