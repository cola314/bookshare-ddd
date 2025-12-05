package com.bookshare.member.domain

interface TokenGenerator {
    fun generate(memberId: RegularMemberId): String
}
