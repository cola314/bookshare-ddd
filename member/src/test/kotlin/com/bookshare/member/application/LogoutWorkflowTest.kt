package com.bookshare.member.application

import com.bookshare.member.domain.AdminId
import com.bookshare.member.domain.RegularMemberId
import com.bookshare.member.domain.SessionRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class LogoutWorkflowTest {

    private val sessionRepository = mockk<SessionRepository>(relaxed = true)
    private val sut = logout(sessionRepository)

    private val memberId = RegularMemberId("member-1")

    @Test
    fun `로그아웃 성공`() {
        every { sessionRepository.exists(memberId) } returns true

        val result = sut(LogoutCommand(memberId))

        assertIs<LoggedOut>(result)
        assertEquals(memberId, result.memberId)
        verify { sessionRepository.delete(memberId) }
    }

    @Test
    fun `로그인 상태가 아니면 NotLoggedIn 에러`() {
        every { sessionRepository.exists(memberId) } returns false

        val result = sut(LogoutCommand(memberId))

        assertIs<LogoutError>(result)
        assertEquals(LogoutErrorType.NotLoggedIn, result.type)
    }

    @Test
    fun `관리자 ID로 로그아웃 시도시 NotLoggedIn 에러`() {
        val adminId = AdminId("admin-1")

        val result = sut(LogoutCommand(adminId))

        assertIs<LogoutError>(result)
        assertEquals(LogoutErrorType.NotLoggedIn, result.type)
    }
}
