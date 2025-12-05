package com.bookshare.review.application

import com.bookshare.member.domain.MemberId
import com.bookshare.review.domain.Book
import com.bookshare.review.domain.MemberExistenceChecker
import com.bookshare.review.domain.Review
import com.bookshare.review.domain.ReviewId
import com.bookshare.review.domain.ReviewRepository
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

typealias CreateReview = (CreateReviewCommand) -> CreateReviewResult

fun createReview(
    reviewRepository: ReviewRepository,
    memberExistenceChecker: MemberExistenceChecker
): CreateReview = { command ->
    when {
        !memberExistenceChecker.exists(command.memberId) -> CreateReviewError(CreateReviewErrorType.MemberNotFound)
        command.content.isBlank() -> CreateReviewError(CreateReviewErrorType.InvalidContent)
        else -> {
            val now = LocalDateTime.now()
            val reviewId = reviewRepository.nextId()
            val review = Review(
                id = reviewId,
                memberId = command.memberId,
                book = command.book,
                content = command.content,
                createdAt = now,
                updatedAt = now
            )
            reviewRepository.save(review)
            ReviewCreated(reviewId, now)
        }
    }
}
