package com.bookshare.support.application

import com.bookshare.member.domain.AdminId
import com.bookshare.support.domain.NoticeId

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
