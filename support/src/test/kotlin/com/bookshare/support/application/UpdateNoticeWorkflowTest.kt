package com.bookshare.support.application

import com.bookshare.member.domain.AdminId
import com.bookshare.support.domain.Notice
import com.bookshare.support.domain.NoticeId
import com.bookshare.support.domain.NoticeRepository
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class UpdateNoticeWorkflowTest {

    private val noticeRepository = mockk<NoticeRepository>()
    private val sut = updateNotice(noticeRepository)

    private val adminId = AdminId("admin-1")
    private val noticeId = NoticeId("notice-1")
    private val notice = Notice(
        id = noticeId,
        adminId = adminId,
        title = "원래 제목",
        content = "원래 내용",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    @Test
    fun `공지사항 수정 성공`() {
        every { noticeRepository.findById(noticeId) } returns notice
        every { noticeRepository.save(any()) } answers { firstArg() }

        val result = sut(UpdateNoticeCommand(noticeId, adminId, "수정된 제목", "수정된 내용"))

        assertIs<NoticeUpdated>(result)
        assertEquals(noticeId, result.noticeId)
    }

    @Test
    fun `공지사항이 존재하지 않으면 NoticeNotFound 에러`() {
        every { noticeRepository.findById(noticeId) } returns null

        val result = sut(UpdateNoticeCommand(noticeId, adminId, "수정된 제목", "수정된 내용"))

        assertIs<UpdateNoticeError>(result)
        assertEquals(UpdateNoticeErrorType.NoticeNotFound, result.type)
    }

    @Test
    fun `제목이 비어있으면 InvalidTitle 에러`() {
        every { noticeRepository.findById(noticeId) } returns notice

        val result = sut(UpdateNoticeCommand(noticeId, adminId, "", "수정된 내용"))

        assertIs<UpdateNoticeError>(result)
        assertEquals(UpdateNoticeErrorType.InvalidTitle, result.type)
    }

    @Test
    fun `내용이 비어있으면 InvalidContent 에러`() {
        every { noticeRepository.findById(noticeId) } returns notice

        val result = sut(UpdateNoticeCommand(noticeId, adminId, "수정된 제목", ""))

        assertIs<UpdateNoticeError>(result)
        assertEquals(UpdateNoticeErrorType.InvalidContent, result.type)
    }
}
