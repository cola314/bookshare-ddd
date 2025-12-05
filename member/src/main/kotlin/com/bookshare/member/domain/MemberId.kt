package com.bookshare.member.domain

sealed class MemberId {
    abstract val value: String
}

data class RegularMemberId(override val value: String) : MemberId()

data class AdminId(override val value: String) : MemberId()
