package com.bookshare.review.application

import com.bookshare.member.domain.MemberId
import com.bookshare.review.domain.CommentId
import com.bookshare.review.domain.CommentRepository
import com.bookshare.review.domain.DeletedComment
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

typealias DeleteComment = (DeleteCommentCommand) -> DeleteCommentResult

fun deleteComment(commentRepository: CommentRepository): DeleteComment = { command ->
    val comment = commentRepository.findById(command.commentId)
    when {
        comment == null -> DeleteCommentError(DeleteCommentErrorType.CommentNotFound)
        comment.memberId != command.memberId -> DeleteCommentError(DeleteCommentErrorType.NotCommentOwner)
        else -> {
            val now = LocalDateTime.now()
            val deleted = DeletedComment(
                id = comment.id,
                reviewId = comment.reviewId,
                memberId = comment.memberId,
                deletedAt = now
            )
            commentRepository.delete(deleted)
            CommentDeleted(command.commentId, now)
        }
    }
}
