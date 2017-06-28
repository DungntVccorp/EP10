package com.example.dungnt.ep10.core.MOVE

import com.example.dungnt.ep10.core.MOVE.Connection.HttpComponent
import com.example.dungnt.ep10.core.MOVE.Connection.TcpComponent
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
    addComponent(OperationManager())
    addComponent(HttpComponent())

}

fun TcpComponent.loadConfigTcp(){
    this.addReceiveOperation("com.example.dungnt.ep10.core.MOVE.Operation.HttpOperation")
    this.addReceiveOperation("com.example.dungnt.ep10.core.MOVE.Operation.TcpOperation")

}

fun HttpComponent.loadConfigHttp(){
    this.baseUrl = "https://auth-vt2-beta.wala.vn/vt/r/"
}