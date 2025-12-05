package com.bookshare.member.application

import com.bookshare.member.domain.MemberId

data class LogoutCommand(
    val memberId: MemberId
)

sealed class LogoutResult

data class LoggedOut(
    val memberId: MemberId
) : LogoutResult()

data class LogoutError(
    val type: LogoutErrorType
) : LogoutResult()

enum class LogoutErrorType {
    NotLoggedIn
}

typealias Logout = (LogoutCommand) -> LogoutResult
