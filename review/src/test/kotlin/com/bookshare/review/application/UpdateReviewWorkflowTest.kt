package com.bookshare.review.application

import com.bookshare.member.domain.RegularMemberId
import com.bookshare.review.domain.*
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class UpdateReviewWorkflowTest {

    private val reviewRepository = mockk<ReviewRepository>()
    private val sut = updateReview(reviewRepository)

    private val memberId = RegularMemberId("member-1")
    private val otherMemberId = RegularMemberId("member-2")
    private val reviewId = ReviewId("review-1")
    private val book = Book("978-89-123-4567-8", "클린 코드")
    private val review = Review(
        id = reviewId,
        memberId = memberId,
        book = book,
        content = "원래 내용",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    @Test
    fun `리뷰 수정 성공`() {
        every { reviewRepository.findById(reviewId) } returns review
        every { reviewRepository.save(any()) } answers { firstArg() }

        val result = sut(UpdateReviewCommand(reviewId, memberId, "수정된 내용"))

        assertIs<ReviewUpdated>(result)
        assertEquals(reviewId, result.reviewId)
    }

    @Test
    fun `리뷰가 존재하지 않으면 ReviewNotFound 에러`() {
        every { reviewRepository.findById(reviewId) } returns null

        val result = sut(UpdateReviewCommand(reviewId, memberId, "수정된 내용"))

        assertIs<UpdateReviewError>(result)
        assertEquals(UpdateReviewErrorType.ReviewNotFound, result.type)
    }

    @Test
    fun `리뷰 작성자가 아니면 NotReviewOwner 에러`() {
        every { reviewRepository.findById(reviewId) } returns review

        val result = sut(UpdateReviewCommand(reviewId, otherMemberId, "수정된 내용"))

        assertIs<UpdateReviewError>(result)
        assertEquals(UpdateReviewErrorType.NotReviewOwner, result.type)
    }

    @Test
    fun `내용이 비어있으면 InvalidContent 에러`() {
        every { reviewRepository.findById(reviewId) } returns review

        val result = sut(UpdateReviewCommand(reviewId, memberId, ""))

        assertIs<UpdateReviewError>(result)
        assertEquals(UpdateReviewErrorType.InvalidContent, result.type)
    }
}
