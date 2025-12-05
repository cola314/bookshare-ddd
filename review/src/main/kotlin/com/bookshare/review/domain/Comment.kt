package com.bookshare.review.domain

import com.bookshare.member.domain.MemberId
import java.time.LocalDateTime

sealed class Comment

data class ActiveComment(
    val id: CommentId,
    val reviewId: ReviewId,
    val memberId: MemberId,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) : Comment()

data class DeletedComment(
    val id: CommentId,
    val reviewId: ReviewId,
    val memberId: MemberId,
    val deletedAt: LocalDateTime
) : Comment()
