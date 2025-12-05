package com.bookshare.support.application

import com.bookshare.member.domain.AdminId
import com.bookshare.support.domain.Notice
import com.bookshare.support.domain.NoticeId
import com.bookshare.support.domain.NoticeRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DeleteNoticeWorkflowTest {

    private val noticeRepository = mockk<NoticeRepository>(relaxed = true)
    private val sut = deleteNotice(noticeRepository)

    private val adminId = AdminId("admin-1")
    private val noticeId = NoticeId("notice-1")
    private val notice = Notice(
        id = noticeId,
        adminId = adminId,
        title = "공지 제목",
        content = "공지 내용",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    @Test
    fun `공지사항 삭제 성공`() {
        every { noticeRepository.findById(noticeId) } returns notice

        val result = sut(DeleteNoticeCommand(noticeId, adminId))

        assertIs<NoticeDeleted>(result)
        assertEquals(noticeId, result.noticeId)
        verify { noticeRepository.delete(noticeId) }
    }

    @Test
    fun `공지사항이 존재하지 않으면 NoticeNotFound 에러`() {
        every { noticeRepository.findById(noticeId) } returns null

        val result = sut(DeleteNoticeCommand(noticeId, adminId))

        assertIs<DeleteNoticeError>(result)
        assertEquals(DeleteNoticeErrorType.NoticeNotFound, result.type)
    }
}
