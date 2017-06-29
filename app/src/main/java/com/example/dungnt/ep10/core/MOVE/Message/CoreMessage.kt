package com.example.dungnt.ep10.core.MOVE.Message

/**
 * Created by dung.nt on 6/28/17.
 */


/**
    Cấu trúc header của message như sau
    0xEE 0xEE (2)|Size (3)|Type (2)|Flag(1)|Message Id (8)|Data (Protobuf message)
 */
class CoreMessage{

    var msg_size : Int = 8
    var msg_type : Int = 0
    var msg_rID  : Long = 0
    var msg_payload : ByteArray? = null

    constructor(msg_type: Int, msg_rID: Long, msg_payload: ByteArray?) {
        this.msg_size = msg_size
        this.msg_type = msg_type
        this.msg_rID = msg_rID
        this.msg_payload = msg_payload
    }


    fun encryptMessage() : ByteArray?{

        if (this.msg_payload == null){
            return null
        }

        if(msg_rID > 0){
            this.msg_size += 8
        }
        this.msg_size += this.msg_payload!!.count()



        var data = byteArrayOf(238.toByte(),238.toByte())
        data.plus(((msg_size shr 16) and 0xFF).toByte())
        data.plus(((msg_size shr 8) and 0xFF).toByte())
        data.plus((msg_size and 0xFF).toByte())

        data.plus(((msg_type shr 8) and 0xFF).toByte())
        data.plus((msg_type and 0xFF).toByte())
        if (this.msg_rID > 0){
            // CO' ID /// ADD ID
            data.plus(2.toByte())

            // 2byte custom id for client
            data.plus(((msg_type shr 8) and 0xFF).toByte())
            data.plus((msg_type and 0xFF).toByte())
            // 6 byte msg ID
            data.plus(((msg_rID shr 40) and 0xFF).toByte())
            data.plus(((msg_rID shr 32) and 0xFF).toByte())
            data.plus(((msg_rID shr 24) and 0xFF).toByte())
            data.plus(((msg_rID shr 16) and 0xFF).toByte())
            data.plus(((msg_rID shr 8) and 0xFF).toByte())
            data.plus((msg_rID and 0xFF).toByte())

        }else{
            // KHONG CO ID
            data.plus(0.toByte())
        }

        /// ADD RAW PAYLOAD
        data.plus(this.msg_payload!!)

        return data
    }

    companion object {



        fun decryptMessage(replyData : ByteArray) : CoreMessage?{
            var index : Int = 0
            if(replyData[index++].toInt() == 238 && replyData[index++].toInt() == 238){
                /// HEADER OK
            }

            var size = replyData[index++].toInt() and 0xFF
            size = size shl 8
            size += replyData[index++].toInt() and 0xFF
            size = size shl 8
            size += replyData[index++].toInt() and 0xFF

            println("MSG SIZE" + size)

            var type = replyData[index++].toInt() and 0xFF
            type = (type shl 8) + (replyData[index++].toInt() and 0xFF)

            var flag = replyData[index++].toInt() and 0xFF


            return null
        }
    }
}

