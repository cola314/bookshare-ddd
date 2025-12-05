package com.bookshare.review.application

import com.bookshare.member.domain.RegularMemberId
import com.bookshare.review.domain.*
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class LikeReviewWorkflowTest {

    private val reviewRepository = mockk<ReviewRepository>()
    private val likeRepository = mockk<LikeRepository>()
    private val memberExistenceChecker = mockk<MemberExistenceChecker>()
    private val sut = likeReview(reviewRepository, likeRepository, memberExistenceChecker)

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

    @Test
    fun `좋아요 성공`() {
        every { reviewRepository.findById(reviewId) } returns review
        every { memberExistenceChecker.exists(memberId) } returns true
        every { likeRepository.findByReviewIdAndMemberId(reviewId, memberId) } returns null
        every { likeRepository.nextId() } returns likeId
        every { likeRepository.save(any()) } answers { firstArg() }

        val result = sut(LikeReviewCommand(reviewId, memberId))

        assertIs<ReviewLiked>(result)
        assertEquals(likeId, result.likeId)
    }

    @Test
    fun `리뷰가 존재하지 않으면 ReviewNotFound 에러`() {
        every { reviewRepository.findById(reviewId) } returns null
        every { likeRepository.findByReviewIdAndMemberId(reviewId, memberId) } returns null

        val result = sut(LikeReviewCommand(reviewId, memberId))

        assertIs<LikeReviewError>(result)
        assertEquals(LikeReviewErrorType.ReviewNotFound, result.type)
    }

    @Test
    fun `회원이 존재하지 않으면 MemberNotFound 에러`() {
        every { reviewRepository.findById(reviewId) } returns review
        every { memberExistenceChecker.exists(memberId) } returns false
        every { likeRepository.findByReviewIdAndMemberId(reviewId, memberId) } returns null

        val result = sut(LikeReviewCommand(reviewId, memberId))

        assertIs<LikeReviewError>(result)
        assertEquals(LikeReviewErrorType.MemberNotFound, result.type)
    }

    @Test
    fun `이미 좋아요한 경우 AlreadyLiked 에러`() {
        val existingLike = Like(likeId, reviewId, memberId, LocalDateTime.now())
        every { reviewRepository.findById(reviewId) } returns review
        every { memberExistenceChecker.exists(memberId) } returns true
        every { likeRepository.findByReviewIdAndMemberId(reviewId, memberId) } returns existingLike

        val result = sut(LikeReviewCommand(reviewId, memberId))

        assertIs<LikeReviewError>(result)
        assertEquals(LikeReviewErrorType.AlreadyLiked, result.type)
    }
}
