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

class UnlikeReviewWorkflowTest {

    private val reviewRepository = mockk<ReviewRepository>()
    private val likeRepository = mockk<LikeRepository>(relaxed = true)
    private val sut = unlikeReview(reviewRepository, likeRepository)

    private val memberId = RegularMemberId("member-1")
    private val reviewId = ReviewId("review-1")
    private val likeId = LikeId("like-1")
    private val book = Book("978-89-123-4567-8", "클린 코드")
    private val review = Review(
        id = reviewId,
        memberId = memberId,
        book = book,
        content = "내용",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )
    private val like = Like(likeId, reviewId, memberId, LocalDateTime.now())

    @Test
    fun `좋아요 취소 성공`() {
        every { reviewRepository.findById(reviewId) } returns review
        every { likeRepository.findByReviewIdAndMemberId(reviewId, memberId) } returns like

        val result = sut(UnlikeReviewCommand(reviewId, memberId))

        assertIs<ReviewUnliked>(result)
        assertEquals(likeId, result.likeId)
        verify { likeRepository.delete(likeId) }
    }

    @Test
    fun `리뷰가 존재하지 않으면 ReviewNotFound 에러`() {
        every { reviewRepository.findById(reviewId) } returns null
        every { likeRepository.findByReviewIdAndMemberId(reviewId, memberId) } returns like

        val result = sut(UnlikeReviewCommand(reviewId, memberId))

        assertIs<UnlikeReviewError>(result)
        assertEquals(UnlikeReviewErrorType.ReviewNotFound, result.type)
    }

    @Test
    fun `좋아요가 존재하지 않으면 LikeNotFound 에러`() {
        every { reviewRepository.findById(reviewId) } returns review
        every { likeRepository.findByReviewIdAndMemberId(reviewId, memberId) } returns null

        val result = sut(UnlikeReviewCommand(reviewId, memberId))

        assertIs<UnlikeReviewError>(result)
        assertEquals(UnlikeReviewErrorType.LikeNotFound, result.type)
    }
}
