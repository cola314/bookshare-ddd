package com.bookshare.support.application

import com.bookshare.support.domain.InquiryId
import java.time.LocalDateTime

data class CreateGuestInquiryCommand(
    val guestContact: String,
    val title: String,
    val content: String
)

sealed class CreateGuestInquiryResult

data class GuestInquiryCreated(
    val inquiryId: InquiryId,
    val createdAt: LocalDateTime
) : CreateGuestInquiryResult()

data class CreateGuestInquiryError(
    val type: CreateGuestInquiryErrorType
) : CreateGuestInquiryResult()

enum class CreateGuestInquiryErrorType {
    InvalidContact,
    InvalidTitle,
    InvalidContent
}
