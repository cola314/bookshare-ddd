package com.bookshare.support.application

import com.bookshare.member.domain.AdminId
import com.bookshare.support.domain.AdminExistenceChecker
import com.bookshare.support.domain.NoticeId
import com.bookshare.support.domain.NoticeRepository
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CreateNoticeWorkflowTest {

    private val noticeRepository = mockk<NoticeRepository>()
    private val adminExistenceChecker = mockk<AdminExistenceChecker>()
    private val sut = createNotice(noticeRepository, adminExistenceChecker)

    private val adminId = AdminId("admin-1")
    private val noticeId = NoticeId("notice-1")

    @Test
    fun `공지사항 등록 성공`() {
        every { adminExistenceChecker.exists(adminId) } returns true
        every { noticeRepository.nextId() } returns noticeId
        every { noticeRepository.save(any()) } answers { firstArg() }

        val result = sut(CreateNoticeCommand(adminId, "공지 제목", "공지 내용"))

        assertIs<NoticeCreated>(result)
        assertEquals(noticeId, result.noticeId)
    }

    @Test
    fun `관리자가 존재하지 않으면 AdminNotFound 에러`() {
        every { adminExistenceChecker.exists(adminId) } returns false

        val result = sut(CreateNoticeCommand(adminId, "공지 제목", "공지 내용"))

        assertIs<CreateNoticeError>(result)
        assertEquals(CreateNoticeErrorType.AdminNotFound, result.type)
    }

    @Test
    fun `제목이 비어있으면 InvalidTitle 에러`() {
        every { adminExistenceChecker.exists(adminId) } returns true

        val result = sut(CreateNoticeCommand(adminId, "", "공지 내용"))

        assertIs<CreateNoticeError>(result)
        assertEquals(CreateNoticeErrorType.InvalidTitle, result.type)
    }

    @Test
    fun `내용이 비어있으면 InvalidContent 에러`() {
        every { adminExistenceChecker.exists(adminId) } returns true

        val result = sut(CreateNoticeCommand(adminId, "공지 제목", ""))

        assertIs<CreateNoticeError>(result)
        assertEquals(CreateNoticeErrorType.InvalidContent, result.type)
    }
}
