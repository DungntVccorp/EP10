package com.example.dungnt.ep10.core.MOVE

import com.example.dungnt.ep10.core.MOVE.Communication.HttpComponent
import com.example.dungnt.ep10.core.MOVE.Communication.TcpComponent
import com.example.dungnt.ep10.core.MOVE.Operation.OperationManager

/**
 * Created by dung.nt on 6/27/17.
 */

enum class HTTP_ERROR{
    NONE,
    CONFIG_FAILURE,
    LOST_CONNECTION,
    REQUEST_BUILD_FAILURE,

}


fun Engine.loadConfigComponent(){
    this.addComponent(OperationManager())
    this.addComponent(HttpComponent())
    this.addComponent(TcpComponent())

}

fun TcpComponent.loadConfigTcp(){
    this.addReceiveOperation("com.example.dungnt.ep10.VietTalk.Operation.Profile.ReceiveSipAccountOperation",comm.CommProvision.ReceiveSipAccount.RECEIVE_SIP_ACCOUNT_FIELD_NUMBER)
}

fun HttpComponent.loadConfigHttp(){
    this.baseUrl = "https://auth-vt2-beta.wala.vn/vt/r/"
}