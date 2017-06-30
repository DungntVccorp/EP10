package com.example.dungnt.ep10.core.MOVE.Message

/**
 * Created by dung.nt on 6/28/17.
 */


/**
Cấu trúc header của message như sau
0xEE 0xEE (2)|Size (3)|Type (2)|Flag(1)|Message Id (8)|Data (Protobuf message)
 */
class CoreMessage {

    var msg_size: Int = 8
    var msg_type: Int = 0
    var msg_rID: Long? = 0
    var msg_payload: ByteArray? = null

    constructor(msg_type: Int, msg_rID: Long?, msg_payload: ByteArray?) {

        this.msg_size = msg_size
        this.msg_type = msg_type
        this.msg_rID = msg_rID
        this.msg_payload = msg_payload
    }

    constructor(msg_size: Int, msg_type: Int, msg_rID: Long?, msg_payload: ByteArray?) {
        this.msg_size = msg_size
        this.msg_type = msg_type
        this.msg_rID = msg_rID
        this.msg_payload = msg_payload
    }


    fun encryptMessage(): ByteArray? {

        if (this.msg_payload == null && msg_rID == null) {
            return null
        }

        if (msg_rID!! > 0) {
            this.msg_size += 8
        }
        this.msg_size += this.msg_payload!!.count()


        val b: Byte = 0xEE.toByte()

        var data = byteArrayOf(b, b)
        println(data[0].toInt())
        data = data.plus(((msg_size shr 16) and 0xFF).toByte())
        data = data.plus(((msg_size shr 8) and 0xFF).toByte())
        data = data.plus((msg_size and 0xFF).toByte())

        data = data.plus(((msg_type shr 8) and 0xFF).toByte())
        data = data.plus((msg_type and 0xFF).toByte())
        if (this.msg_rID!! > 0) {
            // CO' ID /// ADD ID
            data = data.plus((2 and 0xFF).toByte())

            // 2byte custom id for client
            data = data.plus(((msg_type shr 8) and 0xFF).toByte())
            data = data.plus((msg_type and 0xFF).toByte())
            // 6 byte msg ID
            data = data.plus(((msg_rID!! shr 40) and 0xFF).toByte())
            data = data.plus(((msg_rID!! shr 32) and 0xFF).toByte())
            data = data.plus(((msg_rID!! shr 24) and 0xFF).toByte())
            data = data.plus(((msg_rID!! shr 16) and 0xFF).toByte())
            data = data.plus(((msg_rID!! shr 8) and 0xFF).toByte())
            data = data.plus((msg_rID!! and 0xFF).toByte())

        } else {
            // KHONG CO ID
            data = data.plus(0.toByte())
        }

        /// ADD RAW PAYLOAD
        data = data.plus(this.msg_payload!!)

        return data
    }

    companion object {


        fun decryptMessage(rawData: ByteArray): CoreMessage? {
            var index: Int = 0
            if ((rawData[index++].toInt() and 0xFF) == 0xEE && (rawData[index++].toInt() and 0xFF) == 0xEE) {
                // READ SIZE
                var size = rawData[index++].toInt() and 0xFF
                size = size shl 8
                size += rawData[index++].toInt() and 0xFF
                size = size shl 8
                size += rawData[index++].toInt() and 0xFF

                if (size <= rawData.count()) {

                    // READ TYPE
                    var type = rawData[index++].toInt() and 0xFF
                    type = (type shl 8) + (rawData[index++].toInt() and 0xFF)

                    // READ FLAG
                    var flag = rawData[index++].toInt() and 0xFF

                    if (flag == 2) {
                        /// READ MSG ID

                        /// READ CUSTOM ID
                        var cutomId = rawData[index++].toInt() and 0xFF
                        cutomId = (type shl 8) + (rawData[index++].toInt() and 0xFF)


                        /// READ ID
                        var rqId = rawData[index++].toLong() and 0xFF // 40
                        rqId = rqId shl 8
                        rqId += rawData[index++].toLong() and 0xFF // 32
                        rqId = rqId shl 8
                        rqId += rawData[index++].toLong() and 0xFF // 24
                        rqId = rqId shl 8
                        rqId += rawData[index++].toLong() and 0xFF // 16
                        rqId = rqId shl 8
                        rqId += rawData[index++].toLong() and 0xFF // 8
                        rqId = rqId shl 8
                        rqId += rawData[index++].toLong() and 0xFF

                        /// GET PAYLOAD

                        var payload = rawData.copyOfRange(index, size)


                        return CoreMessage(size, cutomId, rqId, payload)

                    } else {
                        var payload = rawData.copyOfRange(index, size)
                        var rep = CoreMessage(size, type, null, payload)

                        return rep
                    }
                }

            }
            return null
        }
    }
}

