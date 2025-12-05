package com.bookshare.support.application

import com.bookshare.member.domain.AdminId
import com.bookshare.support.domain.NoticeId
import java.time.LocalDateTime

data class CreateNoticeCommand(
    val adminId: AdminId,
    val title: String,
    val content: String
)

sealed class CreateNoticeResult

data class NoticeCreated(
    val noticeId: NoticeId,
    val createdAt: LocalDateTime
) : CreateNoticeResult()

data class CreateNoticeError(
    val type: CreateNoticeErrorType
) : CreateNoticeResult()

enum class CreateNoticeErrorType {
    AdminNotFound,
    InvalidTitle,
    InvalidContent
}
