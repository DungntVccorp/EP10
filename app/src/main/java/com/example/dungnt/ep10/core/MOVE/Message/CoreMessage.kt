package com.example.dungnt.ep10.core.MOVE.Message

/**
 * Created by dung.nt on 6/28/17.
 */
class CoreMessage{

    var msg_size : Int = 0
    var msg_type : Int = 0
    var msg_rID  : Long = 0
    var msg_data : ByteArray? = null

    fun encryptMessage(){

    }

    companion object {

        fun decryptMessage(replyData : ByteArray) : CoreMessage?{

            return null
        }
    }
}

