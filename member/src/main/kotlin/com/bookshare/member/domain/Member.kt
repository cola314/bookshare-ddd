package com.bookshare.member.domain

import java.time.LocalDateTime

sealed class Member

data class RegularMember(
    val id: RegularMemberId,
    val name: String,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) : Member()

data class AdminMember(
    val id: AdminId,
    val name: String,
    val email: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) : Member()
