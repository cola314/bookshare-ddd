package com.bookshare.support.application

import com.bookshare.member.domain.MemberId
import com.bookshare.support.domain.InquiryId
import java.time.LocalDateTime

data class CreateMemberInquiryCommand(
    val memberId: MemberId,
    val title: String,
    val content: String
)

sealed class CreateMemberInquiryResult

data class MemberInquiryCreated(
    val inquiryId: InquiryId,
    val createdAt: LocalDateTime
) : CreateMemberInquiryResult()

data class CreateMemberInquiryError(
    val type: CreateMemberInquiryErrorType
) : CreateMemberInquiryResult()

enum class CreateMemberInquiryErrorType {
    MemberNotFound,
    InvalidTitle,
    InvalidContent
}
