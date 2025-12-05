package com.bookshare.member.application

import com.bookshare.member.domain.AdminId
import com.bookshare.member.domain.MemberRepository
import com.bookshare.member.domain.RegularMember
import com.bookshare.member.domain.RegularMemberId
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ChangeNameWorkflowTest {

    private val memberRepository = mockk<MemberRepository>()
    private val sut = changeName(memberRepository)

    private val memberId = RegularMemberId("member-1")
    private val member = RegularMember(
        id = memberId,
        name = "홍길동",
        email = "test@example.com",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    @Test
    fun `이름 변경 성공`() {
        val savedMember = slot<RegularMember>()
        every { memberRepository.findById(memberId) } returns member
        every { memberRepository.save(capture(savedMember)) } answers { savedMember.captured }

        val result = sut(ChangeNameCommand(memberId, "김철수"))

        assertIs<NameChanged>(result)
        assertEquals(memberId, result.memberId)
        assertEquals("김철수", savedMember.captured.name)
    }

    @Test
    fun `회원을 찾을 수 없으면 MemberNotFound 에러`() {
        every { memberRepository.findById(memberId) } returns null

        val result = sut(ChangeNameCommand(memberId, "김철수"))

        assertIs<ChangeNameError>(result)
        assertEquals(ChangeNameErrorType.MemberNotFound, result.type)
    }

    @Test
    fun `빈 이름이면 InvalidName 에러`() {
        val result = sut(ChangeNameCommand(memberId, ""))

        assertIs<ChangeNameError>(result)
        assertEquals(ChangeNameErrorType.InvalidName, result.type)
    }

    @Test
    fun `공백만 있는 이름이면 InvalidName 에러`() {
        val result = sut(ChangeNameCommand(memberId, "   "))

        assertIs<ChangeNameError>(result)
        assertEquals(ChangeNameErrorType.InvalidName, result.type)
    }

    @Test
    fun `관리자 ID로 이름 변경 시도시 MemberNotFound 에러`() {
        val adminId = AdminId("admin-1")

        val result = sut(ChangeNameCommand(adminId, "김철수"))

        assertIs<ChangeNameError>(result)
        assertEquals(ChangeNameErrorType.MemberNotFound, result.type)
    }
}
