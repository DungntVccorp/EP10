package com.example.dungnt.ep10.core.MOVE.Operation

import java.io.InputStream

/**
 * Created by dung.nt on 6/27/17.
 */
abstract class TcpOperation() : BaseOperation(){

    var replyData : InputStream? = null
    var ex : Exception? = null

    abstract fun buildRequest() : InputStream?
    abstract fun onReplyRequest()

    override protected fun fire() {
        if (replyData == null){
            var data = this.buildRequest()
            if(data != null){

            }else{

            }
        }else{
            // REPLY
            this.onReplyRequest()
        }
    }
}
