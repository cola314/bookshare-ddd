package com.bookshare.support.application

import com.bookshare.member.domain.AdminId
import com.bookshare.support.domain.NoticeId
import com.bookshare.support.domain.NoticeRepository

data class DeleteNoticeCommand(
    val noticeId: NoticeId,
    val adminId: AdminId
)

sealed class DeleteNoticeResult

data class NoticeDeleted(
    val noticeId: NoticeId
) : DeleteNoticeResult()

data class DeleteNoticeError(
    val type: DeleteNoticeErrorType
) : DeleteNoticeResult()

enum class DeleteNoticeErrorType {
    NoticeNotFound
}

typealias DeleteNotice = (DeleteNoticeCommand) -> DeleteNoticeResult

fun deleteNotice(noticeRepository: NoticeRepository): DeleteNotice = { command ->
    val notice = noticeRepository.findById(command.noticeId)
    when (notice) {
        null -> DeleteNoticeError(DeleteNoticeErrorType.NoticeNotFound)
        else -> {
            noticeRepository.delete(command.noticeId)
            NoticeDeleted(command.noticeId)
        }
    }
}
