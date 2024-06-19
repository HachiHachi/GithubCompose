package com.julian.githubcompose.model.repo.base

open class ApiErrorException(
    msgCode: String?,
    msgContent: String?
) : Exception() {

    private var msgCode: String? = ""
    private var msgContent: String? = ""

    init {
        this.msgCode = msgCode
        this.msgContent = msgContent
    }

    fun getMsgCode(): String {
        return msgCode ?: ""
    }

    fun getMsgContent(): String? {
        return msgContent
    }

}