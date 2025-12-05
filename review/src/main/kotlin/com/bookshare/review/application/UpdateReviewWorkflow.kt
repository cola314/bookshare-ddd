package com.bookshare.review.application

import com.bookshare.member.domain.MemberId
import com.bookshare.review.domain.ReviewId
import com.bookshare.review.domain.ReviewRepository
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

typealias UpdateReview = (UpdateReviewCommand) -> UpdateReviewResult

fun updateReview(reviewRepository: ReviewRepository): UpdateReview = { command ->
    val review = reviewRepository.findById(command.reviewId)
    when {
        review == null -> UpdateReviewError(UpdateReviewErrorType.ReviewNotFound)
        review.memberId != command.memberId -> UpdateReviewError(UpdateReviewErrorType.NotReviewOwner)
        command.content.isBlank() -> UpdateReviewError(UpdateReviewErrorType.InvalidContent)
        else -> {
            val now = LocalDateTime.now()
            val updated = review.copy(content = command.content, updatedAt = now)
            reviewRepository.save(updated)
            ReviewUpdated(command.reviewId, now)
        }
    }
}
