package com.bookshare.support.application

import com.bookshare.member.domain.AdminId
import com.bookshare.support.domain.AdminExistenceChecker
import com.bookshare.support.domain.Notice
import com.bookshare.support.domain.NoticeId
import com.bookshare.support.domain.NoticeRepository
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

typealias CreateNotice = (CreateNoticeCommand) -> CreateNoticeResult

fun createNotice(
    noticeRepository: NoticeRepository,
    adminExistenceChecker: AdminExistenceChecker
): CreateNotice = { command ->
    when {
        !adminExistenceChecker.exists(command.adminId) -> CreateNoticeError(CreateNoticeErrorType.AdminNotFound)
        command.title.isBlank() -> CreateNoticeError(CreateNoticeErrorType.InvalidTitle)
        command.content.isBlank() -> CreateNoticeError(CreateNoticeErrorType.InvalidContent)
        else -> {
            val now = LocalDateTime.now()
            val noticeId = noticeRepository.nextId()
            val notice = Notice(
                id = noticeId,
                adminId = command.adminId,
                title = command.title,
                content = command.content,
                createdAt = now,
                updatedAt = now
            )
            noticeRepository.save(notice)
            NoticeCreated(noticeId, now)
        }
    }
}
