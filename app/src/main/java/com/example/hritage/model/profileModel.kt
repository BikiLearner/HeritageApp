package com.example.hritage.model

import android.graphics.Bitmap


data class profileModel(
    val id: Int,
    val name: String,
    val email: String,
    val address: String,
    val phoneNumber: String,
    val collegeRoll: String,
    val collegeID: String,
    val dateOfBirth: String,
    val password: String,
    val defaultProfile: Bitmap
)
