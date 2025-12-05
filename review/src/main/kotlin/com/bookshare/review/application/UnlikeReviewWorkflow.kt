package com.bookshare.review.application

import com.bookshare.member.domain.MemberId
import com.bookshare.review.domain.LikeId
import com.bookshare.review.domain.ReviewId

data class UnlikeReviewCommand(
    val reviewId: ReviewId,
    val memberId: MemberId
)

sealed class UnlikeReviewResult

data class ReviewUnliked(
    val likeId: LikeId
) : UnlikeReviewResult()

data class UnlikeReviewError(
    val type: UnlikeReviewErrorType
) : UnlikeReviewResult()

enum class UnlikeReviewErrorType {
    ReviewNotFound,
    LikeNotFound
}
