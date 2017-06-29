package com.example.dungnt.ep10.VietTalk.Operation.Profile

import com.example.dungnt.ep10.core.MOVE.Operation.HttpOperation
import com.google.protobuf.ExtensionRegistryLite
import comm.CommModel
import comm.CommProfile

/**
 * Created by dung.nt on 6/29/17.
 */
class CheckAccountOperation : HttpOperation{
    var telNumber: String? = null

    constructor() : super()

    override fun apiId(): Int {
        return CommProfile.CheckAccount.ID_FIELD_NUMBER
    }

    override fun buildRequest(): ByteArray? {
        if (this.telNumber == null){
            return null
        }
        return CommProfile.CheckAccount.Request.newBuilder().setTel(telNumber!!).build().toByteArray()
    }

    override fun onReplyRequest() {
        if (this.replyData != null){
            ExtensionRegistryLite.newInstance().unmodifiable
            val extension = ExtensionRegistryLite.newInstance()
            extension.add(CommProfile.CheckAccount.iD)
            var reply = CommModel.Reply.parseFrom(this.replyData!!,extension)
            if (reply.hasType()){
                val checkaccountReply = reply.getExtension(CommProfile.CheckAccount.iD)
                if (checkaccountReply is CommProfile.CheckAccount.Reply){

                }
            }
        }
    }
}