package com.bookshare.review.application

import com.bookshare.member.domain.MemberId
import com.bookshare.review.domain.Book
import com.bookshare.review.domain.ReviewId
import java.time.LocalDateTime

data class CreateReviewCommand(
    val memberId: MemberId,
    val book: Book,
    val content: String
)

sealed class CreateReviewResult

data class ReviewCreated(
    val reviewId: ReviewId,
    val createdAt: LocalDateTime
) : CreateReviewResult()

data class CreateReviewError(
    val type: CreateReviewErrorType
) : CreateReviewResult()

enum class CreateReviewErrorType {
    MemberNotFound,
    InvalidContent
}
