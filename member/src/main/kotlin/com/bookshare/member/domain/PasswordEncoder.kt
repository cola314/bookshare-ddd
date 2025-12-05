package com.bookshare.member.domain

interface PasswordEncoder {
    fun matches(rawPassword: String, email: String): Boolean
}
