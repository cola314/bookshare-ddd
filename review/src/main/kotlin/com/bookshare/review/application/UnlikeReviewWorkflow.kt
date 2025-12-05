package com.bookshare.review.application

import com.bookshare.member.domain.MemberId
import com.bookshare.review.domain.LikeId
import com.bookshare.review.domain.LikeRepository
import com.bookshare.review.domain.ReviewId
import com.bookshare.review.domain.ReviewRepository

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

typealias UnlikeReview = (UnlikeReviewCommand) -> UnlikeReviewResult

fun unlikeReview(
    reviewRepository: ReviewRepository,
    likeRepository: LikeRepository
): UnlikeReview = { command ->
    val review = reviewRepository.findById(command.reviewId)
    val like = likeRepository.findByReviewIdAndMemberId(command.reviewId, command.memberId)
    when {
        review == null -> UnlikeReviewError(UnlikeReviewErrorType.ReviewNotFound)
        like == null -> UnlikeReviewError(UnlikeReviewErrorType.LikeNotFound)
        else -> {
            likeRepository.delete(like.id)
            ReviewUnliked(like.id)
        }
    }
}
