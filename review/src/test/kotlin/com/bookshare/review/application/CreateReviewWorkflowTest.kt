package com.bookshare.review.application

import com.bookshare.member.domain.RegularMemberId
import com.bookshare.review.domain.*
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CreateReviewWorkflowTest {

    private val reviewRepository = mockk<ReviewRepository>()
    private val memberExistenceChecker = mockk<MemberExistenceChecker>()
    private val sut = createReview(reviewRepository, memberExistenceChecker)

    private val memberId = RegularMemberId("member-1")
    private val reviewId = ReviewId("review-1")
    private val book = Book("978-89-123-4567-8", "클린 코드")

    @Test
    fun `리뷰 작성 성공`() {
        every { memberExistenceChecker.exists(memberId) } returns true
        every { reviewRepository.nextId() } returns reviewId
        every { reviewRepository.save(any()) } answers { firstArg() }

        val result = sut(CreateReviewCommand(memberId, book, "좋은 책입니다"))

        assertIs<ReviewCreated>(result)
        assertEquals(reviewId, result.reviewId)
    }

    @Test
    fun `회원이 존재하지 않으면 MemberNotFound 에러`() {
        every { memberExistenceChecker.exists(memberId) } returns false

        val result = sut(CreateReviewCommand(memberId, book, "좋은 책입니다"))

        assertIs<CreateReviewError>(result)
        assertEquals(CreateReviewErrorType.MemberNotFound, result.type)
    }

    @Test
    fun `내용이 비어있으면 InvalidContent 에러`() {
        every { memberExistenceChecker.exists(memberId) } returns true

        val result = sut(CreateReviewCommand(memberId, book, ""))

        assertIs<CreateReviewError>(result)
        assertEquals(CreateReviewErrorType.InvalidContent, result.type)
    }
}
