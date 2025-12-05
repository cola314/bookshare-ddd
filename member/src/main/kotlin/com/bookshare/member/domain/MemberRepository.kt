package com.bookshare.member.domain

interface MemberRepository {
    fun findByEmail(email: String): RegularMember?
    fun findById(id: RegularMemberId): RegularMember?
    fun save(member: RegularMember): RegularMember
}
