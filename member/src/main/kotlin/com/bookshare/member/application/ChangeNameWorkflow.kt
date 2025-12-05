package com.bookshare.member.application

import com.bookshare.member.domain.MemberId
import java.time.LocalDateTime

data class ChangeNameCommand(
    val memberId: MemberId,
    val newName: String
)

sealed class ChangeNameResult

data class NameChanged(
    val memberId: MemberId,
    val updatedAt: LocalDateTime
) : ChangeNameResult()

data class ChangeNameError(
    val type: ChangeNameErrorType
) : ChangeNameResult()

enum class ChangeNameErrorType {
    MemberNotFound,
    InvalidName
}

typealias ChangeName = (ChangeNameCommand) -> ChangeNameResult
