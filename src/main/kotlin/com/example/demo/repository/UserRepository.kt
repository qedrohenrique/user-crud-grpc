package com.example.demo.repository

import com.example.demo.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    override fun findAll(pageable: Pageable): Page<User>
    fun findByUsername(username: String): User?
}