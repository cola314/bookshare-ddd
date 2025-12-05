package com.bookshare.member.domain

interface SessionRepository {
    fun exists(memberId: RegularMemberId): Boolean
    fun delete(memberId: RegularMemberId)
}
