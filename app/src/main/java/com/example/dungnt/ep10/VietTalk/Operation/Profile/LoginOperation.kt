package com.example.dungnt.ep10.VietTalk.Operation.Profile

import com.example.dungnt.ep10.core.MOVE.Communication.TcpComponent
import com.example.dungnt.ep10.core.MOVE.Component.ComponentType
import com.example.dungnt.ep10.core.MOVE.Engine
import com.example.dungnt.ep10.core.MOVE.Operation.TcpOperation
import com.google.protobuf.ExtensionRegistryLite
import comm.CommModel
import comm.CommProfile

/**
 * Created by dung.nt on 6/29/17.
 */
class LoginOperation : TcpOperation() {
    override fun buildRequest(): ByteArray? {
        val tcp = Engine.instance.getComponent(ComponentType.TCP)
        if(tcp is TcpComponent){
            return CommProfile.Login.Request.newBuilder().setSessionId(tcp.sid).setUserId(tcp.uid).build().toByteArray()
        }
        return CommProfile.Login.Request.newBuilder().build().toByteArray()
    }

    override fun onReplyRequest() {
        if (this.replyData != null){
            ExtensionRegistryLite.newInstance().unmodifiable
            val extension = ExtensionRegistryLite.newInstance()
            extension.add(CommProfile.Login.iD)
            val rep = CommModel.Reply.parseFrom(this.replyData!!,extension)
            var rep2 = rep.getExtension(CommProfile.Login.iD)
            if(rep2 is CommProfile.Login.Reply) {
                println(rep2)
            }
        }


    }
    override fun apiId(): Int {
        return CommProfile.Login.ID_FIELD_NUMBER
    }

}