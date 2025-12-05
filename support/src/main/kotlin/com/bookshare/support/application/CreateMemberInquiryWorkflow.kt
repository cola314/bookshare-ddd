package com.bookshare.support.application

import com.bookshare.member.domain.MemberId
import com.bookshare.support.domain.InquiryId
import com.bookshare.support.domain.InquiryRepository
import com.bookshare.support.domain.MemberExistenceChecker
import com.bookshare.support.domain.MemberInquiry
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

typealias CreateMemberInquiry = (CreateMemberInquiryCommand) -> CreateMemberInquiryResult

fun createMemberInquiry(
    inquiryRepository: InquiryRepository,
    memberExistenceChecker: MemberExistenceChecker
): CreateMemberInquiry = { command ->
    when {
        !memberExistenceChecker.exists(command.memberId) -> CreateMemberInquiryError(CreateMemberInquiryErrorType.MemberNotFound)
        command.title.isBlank() -> CreateMemberInquiryError(CreateMemberInquiryErrorType.InvalidTitle)
        command.content.isBlank() -> CreateMemberInquiryError(CreateMemberInquiryErrorType.InvalidContent)
        else -> {
            val now = LocalDateTime.now()
            val inquiryId = inquiryRepository.nextId()
            val inquiry = MemberInquiry(
                id = inquiryId,
                memberId = command.memberId,
                title = command.title,
                content = command.content,
                createdAt = now
            )
            inquiryRepository.save(inquiry)
            MemberInquiryCreated(inquiryId, now)
        }
    }
}
