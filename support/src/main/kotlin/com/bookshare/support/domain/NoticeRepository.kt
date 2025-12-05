package com.bookshare.support.domain

interface NoticeRepository {
    fun findById(id: NoticeId): Notice?
    fun save(notice: Notice): Notice
    fun delete(id: NoticeId)
    fun nextId(): NoticeId
}
