package com.bookshare.review.application

import com.bookshare.member.domain.MemberId
import com.bookshare.review.domain.Like
import com.bookshare.review.domain.LikeId
import com.bookshare.review.domain.LikeRepository
import com.bookshare.review.domain.MemberExistenceChecker
import com.bookshare.review.domain.ReviewId
import com.bookshare.review.domain.ReviewRepository
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

typealias LikeReview = (LikeReviewCommand) -> LikeReviewResult

fun likeReview(
    reviewRepository: ReviewRepository,
    likeRepository: LikeRepository,
    memberExistenceChecker: MemberExistenceChecker
): LikeReview = { command ->
    val review = reviewRepository.findById(command.reviewId)
    val existingLike = likeRepository.findByReviewIdAndMemberId(command.reviewId, command.memberId)
    when {
        review == null -> LikeReviewError(LikeReviewErrorType.ReviewNotFound)
        !memberExistenceChecker.exists(command.memberId) -> LikeReviewError(LikeReviewErrorType.MemberNotFound)
        existingLike != null -> LikeReviewError(LikeReviewErrorType.AlreadyLiked)
        else -> {
            val now = LocalDateTime.now()
            val likeId = likeRepository.nextId()
            val like = Like(
                id = likeId,
                reviewId = command.reviewId,
                memberId = command.memberId,
                createdAt = now
            )
            likeRepository.save(like)
            ReviewLiked(likeId, now)
        }
    }
}
