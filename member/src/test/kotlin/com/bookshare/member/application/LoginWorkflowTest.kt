package com.bookshare.member.application

import com.bookshare.member.domain.*
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class LoginWorkflowTest {

    private val memberRepository = mockk<MemberRepository>()
    private val passwordEncoder = mockk<PasswordEncoder>()
    private val tokenGenerator = mockk<TokenGenerator>()
    private val sut = login(memberRepository, passwordEncoder, tokenGenerator)

    private val memberId = RegularMemberId("member-1")
    private val member = RegularMember(
        id = memberId,
        name = "홍길동",
        email = "test@example.com",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    @Test
    fun `로그인 성공`() {
        every { memberRepository.findByEmail("test@example.com") } returns member
        every { passwordEncoder.matches("password123", "test@example.com") } returns true
        every { tokenGenerator.generate(memberId) } returns "token-123"

        val result = sut(LoginCommand("test@example.com", "password123"))

        assertIs<LoggedIn>(result)
        assertEquals(memberId, result.memberId)
        assertEquals("token-123", result.token)
    }

    @Test
    fun `회원을 찾을 수 없으면 MemberNotFound 에러`() {
        every { memberRepository.findByEmail("unknown@example.com") } returns null

        val result = sut(LoginCommand("unknown@example.com", "password123"))

        assertIs<LoginError>(result)
        assertEquals(LoginErrorType.MemberNotFound, result.type)
    }

    @Test
    fun `비밀번호가 틀리면 InvalidPassword 에러`() {
        every { memberRepository.findByEmail("test@example.com") } returns member
        every { passwordEncoder.matches("wrongpassword", "test@example.com") } returns false

        val result = sut(LoginCommand("test@example.com", "wrongpassword"))

        assertIs<LoginError>(result)
        assertEquals(LoginErrorType.InvalidPassword, result.type)
    }
}
