package com.bookshare.review.domain

import com.bookshare.member.domain.MemberId

interface MemberExistenceChecker {
    fun exists(memberId: MemberId): Boolean
}
