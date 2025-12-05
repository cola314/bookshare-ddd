package com.bookshare.review.application

import com.bookshare.member.domain.RegularMemberId
import com.bookshare.review.domain.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DeleteReviewWorkflowTest {

    private val reviewRepository = mockk<ReviewRepository>(relaxed = true)
    private val sut = deleteReview(reviewRepository)

    private val memberId = RegularMemberId("member-1")
    private val otherMemberId = RegularMemberId("member-2")
    private val reviewId = ReviewId("review-1")
    private val book = Book("978-89-123-4567-8", "클린 코드")
    private val review = Review(
        id = reviewId,
        memberId = memberId,
        book = book,
        content = "내용",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    @Test
    fun `리뷰 삭제 성공`() {
        every { reviewRepository.findById(reviewId) } returns review

        val result = sut(DeleteReviewCommand(reviewId, memberId))

        assertIs<ReviewDeleted>(result)
        assertEquals(reviewId, result.reviewId)
        verify { reviewRepository.delete(reviewId) }
    }

    @Test
    fun `리뷰가 존재하지 않으면 ReviewNotFound 에러`() {
        every { reviewRepository.findById(reviewId) } returns null

        val result = sut(DeleteReviewCommand(reviewId, memberId))

        assertIs<DeleteReviewError>(result)
        assertEquals(DeleteReviewErrorType.ReviewNotFound, result.type)
    }

    @Test
    fun `리뷰 작성자가 아니면 NotReviewOwner 에러`() {
        every { reviewRepository.findById(reviewId) } returns review

        val result = sut(DeleteReviewCommand(reviewId, otherMemberId))

        assertIs<DeleteReviewError>(result)
        assertEquals(DeleteReviewErrorType.NotReviewOwner, result.type)
    }
}
