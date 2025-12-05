package com.bookshare.member.domain

import java.time.LocalDateTime

sealed class Member {
    abstract val id: MemberId
    abstract val name: String
    abstract val email: String
    abstract val createdAt: LocalDateTime
    abstract val updatedAt: LocalDateTime
}

data class RegularMember(
    override val id: RegularMemberId,
    override val name: String,
    override val email: String,
    override val createdAt: LocalDateTime,
    override val updatedAt: LocalDateTime
) : Member()

data class AdminMember(
    override val id: AdminId,
    override val name: String,
    override val email: String,
    override val createdAt: LocalDateTime,
    override val updatedAt: LocalDateTime
) : Member()
