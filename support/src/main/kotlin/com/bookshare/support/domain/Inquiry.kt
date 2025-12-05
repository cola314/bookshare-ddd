package com.bookshare.support.domain

import com.bookshare.member.domain.MemberId
import java.time.LocalDateTime

sealed class Inquiry {
    abstract val id: InquiryId
    abstract val title: String
    abstract val content: String
    abstract val createdAt: LocalDateTime
}

data class MemberInquiry(
    override val id: InquiryId,
    val memberId: MemberId,
    override val title: String,
    override val content: String,
    override val createdAt: LocalDateTime
) : Inquiry()

data class GuestInquiry(
    override val id: InquiryId,
    val guestContact: String,
    override val title: String,
    override val content: String,
    override val createdAt: LocalDateTime
) : Inquiry()
