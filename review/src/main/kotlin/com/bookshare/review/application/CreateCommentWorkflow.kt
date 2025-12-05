package com.bookshare.review.application

import com.bookshare.member.domain.MemberId
import com.bookshare.review.domain.CommentId
import com.bookshare.review.domain.ReviewId
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
