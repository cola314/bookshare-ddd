package com.bookshare.support.domain

import com.bookshare.member.domain.AdminId

interface AdminExistenceChecker {
    fun exists(adminId: AdminId): Boolean
}
