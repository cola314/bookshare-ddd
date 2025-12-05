package com.bookshare.support.domain

import com.bookshare.member.domain.MemberId
import java.time.LocalDateTime

sealed class Inquiry

data class MemberInquiry(
    val id: InquiryId,
    val memberId: MemberId,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime
) : Inquiry()

data class GuestInquiry(
    val id: InquiryId,
    val guestContact: String,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime
) : Inquiry()
