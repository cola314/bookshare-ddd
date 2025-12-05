package com.bookshare.review.application

import com.bookshare.member.domain.RegularMemberId
import com.bookshare.review.domain.*
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CreateCommentWorkflowTest {

    private val reviewRepository = mockk<ReviewRepository>()
    private val commentRepository = mockk<CommentRepository>()
    private val memberExistenceChecker = mockk<MemberExistenceChecker>()
    private val sut = createComment(reviewRepository, commentRepository, memberExistenceChecker)

    private val memberId = RegularMemberId("member-1")
    private val reviewId = ReviewId("review-1")
    private val commentId = CommentId("comment-1")
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
    fun `댓글 작성 성공`() {
        every { reviewRepository.findById(reviewId) } returns review
        every { memberExistenceChecker.exists(memberId) } returns true
        every { commentRepository.nextId() } returns commentId
        every { commentRepository.save(any()) } answers { firstArg() }

        val result = sut(CreateCommentCommand(reviewId, memberId, "좋은 리뷰입니다"))

        assertIs<CommentCreated>(result)
        assertEquals(commentId, result.commentId)
    }

    @Test
    fun `리뷰가 존재하지 않으면 ReviewNotFound 에러`() {
        every { reviewRepository.findById(reviewId) } returns null

        val result = sut(CreateCommentCommand(reviewId, memberId, "좋은 리뷰입니다"))

        assertIs<CreateCommentError>(result)
        assertEquals(CreateCommentErrorType.ReviewNotFound, result.type)
    }

    @Test
    fun `회원이 존재하지 않으면 MemberNotFound 에러`() {
        every { reviewRepository.findById(reviewId) } returns review
        every { memberExistenceChecker.exists(memberId) } returns false

        val result = sut(CreateCommentCommand(reviewId, memberId, "좋은 리뷰입니다"))

        assertIs<CreateCommentError>(result)
        assertEquals(CreateCommentErrorType.MemberNotFound, result.type)
    }

    @Test
    fun `내용이 비어있으면 InvalidContent 에러`() {
        every { reviewRepository.findById(reviewId) } returns review
        every { memberExistenceChecker.exists(memberId) } returns true

        val result = sut(CreateCommentCommand(reviewId, memberId, ""))

        assertIs<CreateCommentError>(result)
        assertEquals(CreateCommentErrorType.InvalidContent, result.type)
    }
}
