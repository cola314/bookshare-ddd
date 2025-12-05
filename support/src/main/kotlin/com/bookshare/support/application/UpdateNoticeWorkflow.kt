package com.bookshare.support.application

import com.bookshare.member.domain.AdminId
import com.bookshare.support.domain.NoticeId
import java.time.LocalDateTime

data class UpdateNoticeCommand(
    val noticeId: NoticeId,
    val adminId: AdminId,
    val title: String,
    val content: String
)

sealed class UpdateNoticeResult

data class NoticeUpdated(
    val noticeId: NoticeId,
    val updatedAt: LocalDateTime
) : UpdateNoticeResult()

data class UpdateNoticeError(
    val type: UpdateNoticeErrorType
) : UpdateNoticeResult()

enum class UpdateNoticeErrorType {
    NoticeNotFound,
    InvalidTitle,
    InvalidContent
}
