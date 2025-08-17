package com.example.demo.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false)
    val password: String
){
    companion object{
        fun create(username:String, password:String): User =
            User(username = username, password = password)
    }
}