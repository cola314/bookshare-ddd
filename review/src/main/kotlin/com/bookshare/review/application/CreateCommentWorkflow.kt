package com.bookshare.review.application

import com.bookshare.member.domain.MemberId
import com.bookshare.review.domain.ActiveComment
import com.bookshare.review.domain.CommentId
import com.bookshare.review.domain.CommentRepository
import com.bookshare.review.domain.MemberExistenceChecker
import com.bookshare.review.domain.ReviewId
import com.bookshare.review.domain.ReviewRepository
import java.time.LocalDateTime

data class CreateCommentCommand(
    val reviewId: ReviewId,
    val memberId: MemberId,
    val content: String
)

sealed class CreateCommentResult

data class CommentCreated(
    val commentId: CommentId,
    val createdAt: LocalDateTime
) : CreateCommentResult()

data class CreateCommentError(
    val type: CreateCommentErrorType
) : CreateCommentResult()

enum class CreateCommentErrorType {
    ReviewNotFound,
    MemberNotFound,
    InvalidContent
}

typealias CreateComment = (CreateCommentCommand) -> CreateCommentResult

fun createComment(
    reviewRepository: ReviewRepository,
    commentRepository: CommentRepository,
    memberExistenceChecker: MemberExistenceChecker
): CreateComment = { command ->
    val review = reviewRepository.findById(command.reviewId)
    when {
        review == null -> CreateCommentError(CreateCommentErrorType.ReviewNotFound)
        !memberExistenceChecker.exists(command.memberId) -> CreateCommentError(CreateCommentErrorType.MemberNotFound)
        command.content.isBlank() -> CreateCommentError(CreateCommentErrorType.InvalidContent)
        else -> {
            val now = LocalDateTime.now()
            val commentId = commentRepository.nextId()
            val comment = ActiveComment(
                id = commentId,
                reviewId = command.reviewId,
                memberId = command.memberId,
                content = command.content,
                createdAt = now,
                updatedAt = now
            )
            commentRepository.save(comment)
            CommentCreated(commentId, now)
        }
    }
}
