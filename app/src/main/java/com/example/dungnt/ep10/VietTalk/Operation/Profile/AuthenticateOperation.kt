package com.example.dungnt.ep10.VietTalk.Operation.Profile

import com.example.dungnt.ep10.core.MOVE.Operation.HttpOperation
import com.google.protobuf.ExtensionRegistryLite
import comm.CommModel
import comm.CommProfile

/**
 * Created by dung.nt on 6/29/17.
 */
class AuthenticateOperation : HttpOperation(){




    override fun apiId(): Int {
        return CommProfile.Authenticate.ID_FIELD_NUMBER
    }

    override fun buildRequest(): ByteArray? {
        return CommProfile.Authenticate.Request.newBuilder()
                .setDeviceName("dungnt")
                .setPassword("2")
                .setPlatform(2)
                .setUid("841645352285")
                .setPlatformVersion("10.3.3")
                .build().toByteArray()
    }

    override fun onReplyRequest() {
        ExtensionRegistryLite.newInstance().unmodifiable
        val extension = ExtensionRegistryLite.newInstance()
        extension.add(CommProfile.Authenticate.iD)
        val rep = CommModel.Reply.parseFrom(this.replyData!!,extension)
        var rep2 = rep.getExtension(CommProfile.Authenticate.iD)
        if(rep2 is CommProfile.Authenticate.Reply){
            if(this.onSuccess != null){
                this.onSuccess!!(rep2)
            }

        }
    }
}