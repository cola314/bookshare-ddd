package com.bookshare.support.application

import com.bookshare.member.domain.AdminId
import com.bookshare.support.domain.NoticeId
import com.bookshare.support.domain.NoticeRepository
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

typealias UpdateNotice = (UpdateNoticeCommand) -> UpdateNoticeResult

fun updateNotice(noticeRepository: NoticeRepository): UpdateNotice = { command ->
    val notice = noticeRepository.findById(command.noticeId)
    when {
        notice == null -> UpdateNoticeError(UpdateNoticeErrorType.NoticeNotFound)
        command.title.isBlank() -> UpdateNoticeError(UpdateNoticeErrorType.InvalidTitle)
        command.content.isBlank() -> UpdateNoticeError(UpdateNoticeErrorType.InvalidContent)
        else -> {
            val now = LocalDateTime.now()
            val updated = notice.copy(
                title = command.title,
                content = command.content,
                updatedAt = now
            )
            noticeRepository.save(updated)
            NoticeUpdated(command.noticeId, now)
        }
    }
}
