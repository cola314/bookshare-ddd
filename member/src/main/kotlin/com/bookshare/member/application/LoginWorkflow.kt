package com.bookshare.member.application

import com.bookshare.member.domain.MemberId

data class LoginCommand(
    val email: String,
    val password: String
)

sealed class LoginResult

data class LoggedIn(
    val memberId: MemberId,
    val token: String
) : LoginResult()

data class LoginError(
    val type: LoginErrorType
) : LoginResult()

enum class LoginErrorType {
    MemberNotFound,
    InvalidPassword
}

typealias Login = (LoginCommand) -> LoginResult
