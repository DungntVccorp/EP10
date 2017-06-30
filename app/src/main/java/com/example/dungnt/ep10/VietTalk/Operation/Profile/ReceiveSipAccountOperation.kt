package com.example.dungnt.ep10.VietTalk.Operation.Profile

import com.example.dungnt.ep10.core.MOVE.Operation.TcpOperation
import comm.CommProvision

/**
 * Created by dung.nt on 6/30/17.
 */
class ReceiveSipAccountOperation : TcpOperation(){
    override fun buildRequest(): ByteArray? {
        return null
    }

    override fun onReplyRequest() {
        println("Receive Sip ACCOUNT")
    }

    override fun apiId(): Int {
        return CommProvision.ReceiveSipAccount.RECEIVE_SIP_ACCOUNT_FIELD_NUMBER
    }
}