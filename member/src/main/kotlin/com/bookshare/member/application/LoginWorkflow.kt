package com.bookshare.member.application

import com.bookshare.member.domain.MemberId
import com.bookshare.member.domain.MemberRepository
import com.bookshare.member.domain.PasswordEncoder
import com.bookshare.member.domain.TokenGenerator

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

fun login(
    memberRepository: MemberRepository,
    passwordEncoder: PasswordEncoder,
    tokenGenerator: TokenGenerator
): Login = { command ->
    val member = memberRepository.findByEmail(command.email)
    when {
        member == null -> LoginError(LoginErrorType.MemberNotFound)
        !passwordEncoder.matches(command.password, command.email) -> LoginError(LoginErrorType.InvalidPassword)
        else -> {
            val token = tokenGenerator.generate(member.id)
            LoggedIn(member.id, token)
        }
    }
}
