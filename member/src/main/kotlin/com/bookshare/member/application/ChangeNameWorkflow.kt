package com.bookshare.member.application

import com.bookshare.member.domain.MemberId
import com.bookshare.member.domain.MemberRepository
import com.bookshare.member.domain.RegularMemberId
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

fun changeName(memberRepository: MemberRepository): ChangeName = { command ->
    val memberId = command.memberId
    when {
        memberId !is RegularMemberId -> ChangeNameError(ChangeNameErrorType.MemberNotFound)
        command.newName.isBlank() -> ChangeNameError(ChangeNameErrorType.InvalidName)
        else -> {
            val member = memberRepository.findById(memberId)
            when (member) {
                null -> ChangeNameError(ChangeNameErrorType.MemberNotFound)
                else -> {
                    val now = LocalDateTime.now()
                    val updated = member.copy(name = command.newName, updatedAt = now)
                    memberRepository.save(updated)
                    NameChanged(memberId, now)
                }
            }
        }
    }
}
