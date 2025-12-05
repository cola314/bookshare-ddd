package com.bookshare.review.application

import com.bookshare.member.domain.MemberId
import com.bookshare.review.domain.CommentId
import java.time.LocalDateTime

data class DeleteCommentCommand(
    val commentId: CommentId,
    val memberId: MemberId
)

sealed class DeleteCommentResult

data class CommentDeleted(
    val commentId: CommentId,
    val deletedAt: LocalDateTime
) : DeleteCommentResult()

data class DeleteCommentError(
    val type: DeleteCommentErrorType
) : DeleteCommentResult()

enum class DeleteCommentErrorType {
    CommentNotFound,
    NotCommentOwner
}
