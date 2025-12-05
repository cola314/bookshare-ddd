package com.bookshare.review.application

import com.bookshare.member.domain.MemberId
import com.bookshare.review.domain.LikeId
import com.bookshare.review.domain.ReviewId
import java.time.LocalDateTime

data class LikeReviewCommand(
    val reviewId: ReviewId,
    val memberId: MemberId
)

sealed class LikeReviewResult

data class ReviewLiked(
    val likeId: LikeId,
    val createdAt: LocalDateTime
) : LikeReviewResult()

data class LikeReviewError(
    val type: LikeReviewErrorType
) : LikeReviewResult()

enum class LikeReviewErrorType {
    ReviewNotFound,
    MemberNotFound,
    AlreadyLiked
}
