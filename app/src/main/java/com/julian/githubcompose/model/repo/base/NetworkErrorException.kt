package com.julian.githubcompose.model.repo.base

import java.io.IOException

class NetworkErrorException : IOException() {
    var msgTitle: String? = ""
    var msgCode: String = "9999"
    private var msgContent: String? = ""

}