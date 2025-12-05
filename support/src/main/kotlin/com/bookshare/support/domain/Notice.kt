package com.bookshare.support.domain

import com.bookshare.member.domain.AdminId
import java.time.LocalDateTime

data class Notice(
    val id: NoticeId,
    val adminId: AdminId,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
