package com.example.dungnt.ep10.core.MOVE.Communication


import com.example.dungnt.ep10.VietTalk.Operation.Profile.LoginOperation
import com.example.dungnt.ep10.VietTalk.Operation.Profile.PingOperation
import com.example.dungnt.ep10.core.MOVE.Component.BaseComponent
import com.example.dungnt.ep10.core.MOVE.Component.ComponentType
import com.example.dungnt.ep10.core.MOVE.Message.CoreMessage
import com.example.dungnt.ep10.core.MOVE.Operation.TcpOperation
import com.example.dungnt.ep10.core.MOVE.loadConfigTcp
import com.google.protobuf.ByteString
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Created by dung.nt on 6/27/17.
 */


class TcpComponent : BaseComponent, TcpConnectionDelegate {

    var hostName: String? = ""

    var hostPort: Int = 0

    var sid: ByteString? = null

    var uid: ByteString? = null

    var client: TcpConnection? = null

    private var listReceiveOperation =java.util.Collections.synchronizedMap(HashMap<Int, String>())
    private var listSendingOperation = java.util.Collections.synchronizedMap(HashMap<Int, String>())

    override fun priority(): Int {
        return 1
    }

    override fun loadConfig() {
        this.loadConfigTcp()
    }

    override fun start() {


    }

    override fun reset() {

    }

    override fun componentType(): ComponentType {
        return ComponentType.TCP
    }

    fun addReceiveOperation(receiveName: String,receiveId : Int) {
        this.listReceiveOperation[receiveId] = receiveName
    }


    fun Connect() {
        if (this.hostName == null || this.hostPort == null) {
            println("Config Failure")
        } else {
            client = TcpConnection(this.hostName, this.hostPort, this)
            client?.start()


        }
    }

    fun sendMessage(operation: TcpOperation, msg: CoreMessage) {
        var msgData = msg.encryptMessage()
        if (msgData != null) {
            listSendingOperation[operation.apiId()] = operation.getClassName()
            this.client?.newCall(msgData!!)
        }
    }

    override fun socketDidDisconect(error: Exception) {

    }

    override fun socketDidReceiveMessage(msg: CoreMessage) {
        println(msg.msg_type)
        var opName = this.listSendingOperation.remove(msg.msg_type)
        if (opName == null){
            opName = this.listReceiveOperation.remove(msg.msg_type)
        }
        if (opName != null) {
            val cb = Class.forName(opName).newInstance()
            if (cb is TcpOperation) {
                cb.replyData = msg.msg_payload
                cb.enqueue()
            }
        }

    }

    override fun socketDidConected() {
        LoginOperation().enqueue()
        PingOperation().enqueue()
    }
}


interface TcpConnectionDelegate {
    fun socketDidDisconect(error: Exception)
    fun socketDidReceiveMessage(msg: CoreMessage)
    fun socketDidConected()

}


class TcpConnection(val hostName: String?, var hostPort: Int, var delegate: TcpConnectionDelegate?) : Thread() {

    private var socket: Socket? = null

    private var address: InetSocketAddress? = null

    private var inputStream: DataInputStream? = null

    private var outputStream: DataOutputStream? = null

    var isConnected: Boolean = false


    override fun run() {
        super.run()

        try {

            val inetbyName = InetAddress.getByName(this.hostName)

            address = InetSocketAddress(inetbyName, this.hostPort)

            socket = Socket()

            socket?.connect(address)

            println("Socket Connect success")


            inputStream = DataInputStream(socket?.getInputStream())

            outputStream = DataOutputStream(socket?.getOutputStream())

            if (inputStream != null && outputStream != null) {
                isConnected = true
            }

            if (delegate != null) {
                this.delegate?.socketDidConected()
            }
            var buffer: ByteArray? = null

            while (isConnected) {
                if (inputStream!!.available() > 0) {
                    var b: ByteArray = ByteArray(DEFAULT_BUFFER_SIZE)
                    var length: Int = 0

                    length = inputStream!!.read(b)


                    if (buffer == null) {
                        buffer = b.copyOfRange(0, length)
                    } else {
                        buffer.plus(b.copyOfRange(0, length))
                    }
                    var parse: Boolean = true
                    while (parse) {
                        var decryptMessage = CoreMessage.decryptMessage(buffer!!)

                        if (decryptMessage != null) {
                            if (decryptMessage.msg_size < buffer.count()) {
                                /// Bị dư data
                                buffer = buffer!!.copyOfRange(decryptMessage.msg_size, length)
                            } else {
                                /// done
                                parse = false
                                buffer = null
                            }
                            if (delegate != null) {
                                this.delegate?.socketDidReceiveMessage(decryptMessage!!)
                            }
                        } else {
                            /// bi thieu data
                            parse = false
                        }
                    }

                }
                sleep(10)
            }
        } catch (e: Exception) {
            println(e.toString())
            this.isConnected = false
            this.delegate?.socketDidDisconect(e)
        }

    }


    override fun interrupt() {
        super.interrupt()
        println("Thread Interrupt")
    }


    fun tcpStop() {

    }

    fun tcpReconect() {

    }

    fun newCall(data: ByteArray) {
        this.outputStream?.write(data)
    }

}