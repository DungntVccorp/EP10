package com.example.dungnt.ep10.VietTalk.Operation.Profile

import com.example.dungnt.ep10.core.MOVE.Operation.TcpOperation
import comm.CommModel
import comm.CommProfile
import java.io.InputStream

/**
 * Created by dung.nt on 6/29/17.
 */
class PingOperation : TcpOperation(){
    override fun buildRequest(): ByteArray? {
        return CommProfile.KeepAlive.Request.newBuilder().build().toByteArray()
    }

    override fun onReplyRequest() {
        println("PONG")
    }

    override fun apiId(): Int {
        return CommProfile.KeepAlive.ID_FIELD_NUMBER
    }
}