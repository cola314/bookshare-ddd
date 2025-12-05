package com.bookshare.support.application

import com.bookshare.support.domain.GuestInquiry
import com.bookshare.support.domain.InquiryId
import com.bookshare.support.domain.InquiryRepository
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

typealias CreateGuestInquiry = (CreateGuestInquiryCommand) -> CreateGuestInquiryResult

fun createGuestInquiry(inquiryRepository: InquiryRepository): CreateGuestInquiry = { command ->
    when {
        command.guestContact.isBlank() -> CreateGuestInquiryError(CreateGuestInquiryErrorType.InvalidContact)
        command.title.isBlank() -> CreateGuestInquiryError(CreateGuestInquiryErrorType.InvalidTitle)
        command.content.isBlank() -> CreateGuestInquiryError(CreateGuestInquiryErrorType.InvalidContent)
        else -> {
            val now = LocalDateTime.now()
            val inquiryId = inquiryRepository.nextId()
            val inquiry = GuestInquiry(
                id = inquiryId,
                guestContact = command.guestContact,
                title = command.title,
                content = command.content,
                createdAt = now
            )
            inquiryRepository.save(inquiry)
            GuestInquiryCreated(inquiryId, now)
        }
    }
}
