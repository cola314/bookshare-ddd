package com.bookshare.member.application

import com.bookshare.member.domain.MemberId
import com.bookshare.member.domain.RegularMemberId
import com.bookshare.member.domain.SessionRepository

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

fun logout(sessionRepository: SessionRepository): Logout = { command ->
    val memberId = command.memberId
    when {
        memberId !is RegularMemberId -> LogoutError(LogoutErrorType.NotLoggedIn)
        !sessionRepository.exists(memberId) -> LogoutError(LogoutErrorType.NotLoggedIn)
        else -> {
            sessionRepository.delete(memberId)
            LoggedOut(memberId)
        }
    }
}
