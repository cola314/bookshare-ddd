package com.bookshare.review.domain

interface CommentRepository {
    fun findById(id: CommentId): ActiveComment?
    fun save(comment: ActiveComment): ActiveComment
    fun delete(comment: DeletedComment)
    fun nextId(): CommentId
}
