package com.bookshare.review.domain

import com.bookshare.member.domain.MemberId
import java.time.LocalDateTime

data class Review(
    val id: ReviewId,
    val memberId: MemberId,
    val book: Book,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
