package com.bookshare.review.domain

import com.bookshare.member.domain.MemberId
import java.time.LocalDateTime

data class Like(
    val id: LikeId,
    val reviewId: ReviewId,
    val memberId: MemberId,
    val createdAt: LocalDateTime
)
