package com.bookshare.support.domain

interface InquiryRepository {
    fun save(inquiry: Inquiry): Inquiry
    fun nextId(): InquiryId
}
