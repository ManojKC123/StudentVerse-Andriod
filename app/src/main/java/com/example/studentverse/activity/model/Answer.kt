package com.example.studentverse.activity.model

data class Answer (
    val _id: String? = null,
    val createdAt: String? =null,
    val post: String? =null,
    val text: String? =null,
    val comment: ArrayList<Comment>? =null,
    val score: Int? = null,
    val votes: ArrayList<Votes>? = null,
    val author:String? =null
)