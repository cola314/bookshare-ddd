package com.bookshare.review.application

import com.bookshare.member.domain.MemberId
import com.bookshare.review.domain.ReviewId
import java.time.LocalDateTime

data class UpdateReviewCommand(
    val reviewId: ReviewId,
    val memberId: MemberId,
    val content: String
)

sealed class UpdateReviewResult

data class ReviewUpdated(
    val reviewId: ReviewId,
    val updatedAt: LocalDateTime
) : UpdateReviewResult()

data class UpdateReviewError(
    val type: UpdateReviewErrorType
) : UpdateReviewResult()

enum class UpdateReviewErrorType {
    ReviewNotFound,
    NotReviewOwner,
    InvalidContent
}
