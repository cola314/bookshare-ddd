package com.bookshare.review.domain

import com.bookshare.member.domain.MemberId
import java.time.LocalDateTime

sealed class Comment {
    abstract val id: CommentId
    abstract val reviewId: ReviewId
    abstract val memberId: MemberId
}

data class ActiveComment(
    override val id: CommentId,
    override val reviewId: ReviewId,
    override val memberId: MemberId,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) : Comment()

data class DeletedComment(
    override val id: CommentId,
    override val reviewId: ReviewId,
    override val memberId: MemberId,
    val deletedAt: LocalDateTime
) : Comment()
