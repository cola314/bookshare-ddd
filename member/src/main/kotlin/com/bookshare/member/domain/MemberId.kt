package com.bookshare.member.domain

sealed class MemberId

data class RegularMemberId(val value: String) : MemberId()

data class AdminId(val value: String) : MemberId()
