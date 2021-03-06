package com.example.dungnt.ep10.core.MOVE.Exception

import com.example.dungnt.ep10.core.MOVE.HTTP_ERROR

/**
 * Created by dung.nt on 6/28/17.
 */
open class BaseException : Exception{

    constructor(message: String?) : super(message) {
    }
}
class HttpException : BaseException{
    var er_code : HTTP_ERROR = HTTP_ERROR.NONE

    constructor(message: String?, er_code: HTTP_ERROR) : super(message) {
        this.er_code = er_code
    }
}