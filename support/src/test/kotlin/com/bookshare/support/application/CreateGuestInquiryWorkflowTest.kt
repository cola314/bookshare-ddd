package com.bookshare.support.application

import com.bookshare.support.domain.InquiryId
import com.bookshare.support.domain.InquiryRepository
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CreateGuestInquiryWorkflowTest {

    private val inquiryRepository = mockk<InquiryRepository>()
    private val sut = createGuestInquiry(inquiryRepository)

    private val inquiryId = InquiryId("inquiry-1")

    @Test
    fun `비회원 문의 등록 성공`() {
        every { inquiryRepository.nextId() } returns inquiryId
        every { inquiryRepository.save(any()) } answers { firstArg() }

        val result = sut(CreateGuestInquiryCommand("guest@example.com", "문의 제목", "문의 내용"))

        assertIs<GuestInquiryCreated>(result)
        assertEquals(inquiryId, result.inquiryId)
    }

    @Test
    fun `연락처가 비어있으면 InvalidContact 에러`() {
        val result = sut(CreateGuestInquiryCommand("", "문의 제목", "문의 내용"))

        assertIs<CreateGuestInquiryError>(result)
        assertEquals(CreateGuestInquiryErrorType.InvalidContact, result.type)
    }

    @Test
    fun `제목이 비어있으면 InvalidTitle 에러`() {
        val result = sut(CreateGuestInquiryCommand("guest@example.com", "", "문의 내용"))

        assertIs<CreateGuestInquiryError>(result)
        assertEquals(CreateGuestInquiryErrorType.InvalidTitle, result.type)
    }

    @Test
    fun `내용이 비어있으면 InvalidContent 에러`() {
        val result = sut(CreateGuestInquiryCommand("guest@example.com", "문의 제목", ""))

        assertIs<CreateGuestInquiryError>(result)
        assertEquals(CreateGuestInquiryErrorType.InvalidContent, result.type)
    }
}
