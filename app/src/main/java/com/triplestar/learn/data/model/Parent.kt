package com.triplestar.learn.data.model

import com.google.firebase.Timestamp

data class Parent(
    val id: String = "",
    val email: String = "",
    val displayName: String = "",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)
