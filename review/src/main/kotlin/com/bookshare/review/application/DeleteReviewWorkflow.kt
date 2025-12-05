package com.bookshare.review.application

import com.bookshare.member.domain.MemberId
import com.bookshare.review.domain.ReviewId
import com.bookshare.review.domain.ReviewRepository

data class DeleteReviewCommand(
    val reviewId: ReviewId,
    val memberId: MemberId
)

sealed class DeleteReviewResult

data class ReviewDeleted(
    val reviewId: ReviewId
) : DeleteReviewResult()

data class DeleteReviewError(
    val type: DeleteReviewErrorType
) : DeleteReviewResult()

enum class DeleteReviewErrorType {
    ReviewNotFound,
    NotReviewOwner
}

typealias DeleteReview = (DeleteReviewCommand) -> DeleteReviewResult

fun deleteReview(reviewRepository: ReviewRepository): DeleteReview = { command ->
    val review = reviewRepository.findById(command.reviewId)
    when {
        review == null -> DeleteReviewError(DeleteReviewErrorType.ReviewNotFound)
        review.memberId != command.memberId -> DeleteReviewError(DeleteReviewErrorType.NotReviewOwner)
        else -> {
            reviewRepository.delete(command.reviewId)
            ReviewDeleted(command.reviewId)
        }
    }
}
