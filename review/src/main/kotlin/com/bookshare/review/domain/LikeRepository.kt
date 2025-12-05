package com.bookshare.review.domain

import com.bookshare.member.domain.MemberId

interface LikeRepository {
    fun findByReviewIdAndMemberId(reviewId: ReviewId, memberId: MemberId): Like?
    fun save(like: Like): Like
    fun delete(id: LikeId)
    fun nextId(): LikeId
}
