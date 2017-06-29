package com.example.dungnt.ep10.core.MOVE.Operation

import com.example.dungnt.ep10.core.MOVE.Communication.TcpComponent
import com.example.dungnt.ep10.core.MOVE.Component.ComponentType
import com.example.dungnt.ep10.core.MOVE.Engine
import com.example.dungnt.ep10.core.MOVE.Message.CoreMessage


/**
 * Created by dung.nt on 6/27/17.
 */
abstract class TcpOperation() : BaseOperation(){

    var replyData : ByteArray? = null
    var ex : Exception? = null

    abstract fun buildRequest() : ByteArray?
    abstract fun onReplyRequest()
    abstract fun apiId() : Int
    override protected fun fire() {
        if (replyData == null){
            var data = this.buildRequest()
            if(data != null){
                val coreMessage = CoreMessage(this.apiId(), 1, data)
                var tcp = Engine.instance.getComponent(ComponentType.TCP)
                if(tcp is TcpComponent){
                    tcp
                }
            }else{

            }
        }else{
            // REPLY
            this.onReplyRequest()
        }
    }
}
