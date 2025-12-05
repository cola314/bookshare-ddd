package com.bookshare.review.domain

interface ReviewRepository {
    fun findById(id: ReviewId): Review?
    fun save(review: Review): Review
    fun delete(id: ReviewId)
    fun nextId(): ReviewId
}
