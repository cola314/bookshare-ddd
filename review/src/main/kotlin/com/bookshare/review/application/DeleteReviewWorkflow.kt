package com.bookshare.review.application

import com.bookshare.member.domain.MemberId
import com.bookshare.review.domain.ReviewId

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
