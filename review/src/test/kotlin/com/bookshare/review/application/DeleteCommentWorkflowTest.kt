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

class DeleteCommentWorkflowTest {

    private val commentRepository = mockk<CommentRepository>(relaxed = true)
    private val sut = deleteComment(commentRepository)

    private val memberId = RegularMemberId("member-1")
    private val otherMemberId = RegularMemberId("member-2")
    private val reviewId = ReviewId("review-1")
    private val commentId = CommentId("comment-1")
    private val comment = ActiveComment(
        id = commentId,
        reviewId = reviewId,
        memberId = memberId,
        content = "댓글 내용",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    @Test
    fun `댓글 삭제 성공`() {
        every { commentRepository.findById(commentId) } returns comment

        val result = sut(DeleteCommentCommand(commentId, memberId))

        assertIs<CommentDeleted>(result)
        assertEquals(commentId, result.commentId)
        verify { commentRepository.delete(any()) }
    }

    @Test
    fun `댓글이 존재하지 않으면 CommentNotFound 에러`() {
        every { commentRepository.findById(commentId) } returns null

        val result = sut(DeleteCommentCommand(commentId, memberId))

        assertIs<DeleteCommentError>(result)
        assertEquals(DeleteCommentErrorType.CommentNotFound, result.type)
    }

    @Test
    fun `댓글 작성자가 아니면 NotCommentOwner 에러`() {
        every { commentRepository.findById(commentId) } returns comment

        val result = sut(DeleteCommentCommand(commentId, otherMemberId))

        assertIs<DeleteCommentError>(result)
        assertEquals(DeleteCommentErrorType.NotCommentOwner, result.type)
    }
}
