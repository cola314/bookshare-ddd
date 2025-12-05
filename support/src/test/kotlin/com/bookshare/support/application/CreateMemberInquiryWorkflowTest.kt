package com.bookshare.support.application

import com.bookshare.member.domain.RegularMemberId
import com.bookshare.support.domain.InquiryId
import com.bookshare.support.domain.InquiryRepository
import com.bookshare.support.domain.MemberExistenceChecker
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CreateMemberInquiryWorkflowTest {

    private val inquiryRepository = mockk<InquiryRepository>()
    private val memberExistenceChecker = mockk<MemberExistenceChecker>()
    private val sut = createMemberInquiry(inquiryRepository, memberExistenceChecker)

    private val memberId = RegularMemberId("member-1")
    private val inquiryId = InquiryId("inquiry-1")

    @Test
    fun `회원 문의 등록 성공`() {
        every { memberExistenceChecker.exists(memberId) } returns true
        every { inquiryRepository.nextId() } returns inquiryId
        every { inquiryRepository.save(any()) } answers { firstArg() }

        val result = sut(CreateMemberInquiryCommand(memberId, "문의 제목", "문의 내용"))

        assertIs<MemberInquiryCreated>(result)
        assertEquals(inquiryId, result.inquiryId)
    }

    @Test
    fun `회원이 존재하지 않으면 MemberNotFound 에러`() {
        every { memberExistenceChecker.exists(memberId) } returns false

        val result = sut(CreateMemberInquiryCommand(memberId, "문의 제목", "문의 내용"))

        assertIs<CreateMemberInquiryError>(result)
        assertEquals(CreateMemberInquiryErrorType.MemberNotFound, result.type)
    }

    @Test
    fun `제목이 비어있으면 InvalidTitle 에러`() {
        every { memberExistenceChecker.exists(memberId) } returns true

        val result = sut(CreateMemberInquiryCommand(memberId, "", "문의 내용"))

        assertIs<CreateMemberInquiryError>(result)
        assertEquals(CreateMemberInquiryErrorType.InvalidTitle, result.type)
    }

    @Test
    fun `내용이 비어있으면 InvalidContent 에러`() {
        every { memberExistenceChecker.exists(memberId) } returns true

        val result = sut(CreateMemberInquiryCommand(memberId, "문의 제목", ""))

        assertIs<CreateMemberInquiryError>(result)
        assertEquals(CreateMemberInquiryErrorType.InvalidContent, result.type)
    }
}
