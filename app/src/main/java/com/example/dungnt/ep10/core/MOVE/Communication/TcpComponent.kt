package com.example.dungnt.ep10.core.MOVE.Communication

import android.content.Context
import android.os.Bundle
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






class TcpComponent : BaseComponent , TcpConnectionDelegate{

    var hostName : String? = ""

    var hostPort : Int = 0

    var sid      : ByteString? = null

    var uid      : ByteString? = null

    var client : TcpConnection? = null

    private var listReceiveOperation = java.util.Collections.synchronizedList(ArrayList<String>())

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

    fun addReceiveOperation(receiveName : String){
        this.listReceiveOperation.add(receiveName)
    }



    fun Connect(){
        if(this.hostName == null || this.hostPort == null){
            println("Config Failure")
        }else{
            client = TcpConnection(this.hostName,this.hostPort,this)
            client?.start()



        }
    }

    fun sendMessage(operation : TcpOperation,msg : CoreMessage) {
        var msg = msg.encryptMessage()
        if (msg != null) {
            this.client?.newCall(msg!!)
        }
    }

    override fun socketDidDisconect(error: Exception) {

    }

    override fun socketDidReadPackage() {

    }

    override fun socketDidConected() {
        LoginOperation().enqueue()
    }
}


interface TcpConnectionDelegate {
    fun socketDidDisconect(error : Exception)
    fun socketDidReadPackage()
    fun socketDidConected()

}


class TcpConnection(val hostName : String?, var hostPort : Int, var delegate : TcpConnectionDelegate?) : Thread() {

    private var socket : Socket? = null

    private var address : InetSocketAddress? = null

    private var inputStream : DataInputStream? = null

    private var outputStream : DataOutputStream? = null

    var isConnected : Boolean = false



    override fun run() {
        super.run()

        try {

            val inetbyName = InetAddress.getByName(this.hostName)

            address = InetSocketAddress(inetbyName,this.hostPort)

            socket = Socket()

            socket?.connect(address)

            println("Socket Connect success")


            inputStream = DataInputStream(socket?.getInputStream())

            outputStream = DataOutputStream(socket?.getOutputStream())

            if(inputStream != null && outputStream != null){
                isConnected = true
            }


            if(delegate != null){
                this.delegate?.socketDidConected()
            }


            PingOperation().enqueue()

            while (isConnected){
                if (inputStream?.read() != -1){
                    // read package data form server
                    val data = inputStream?.readBytes(DEFAULT_BUFFER_SIZE)
                    println("Data size " + data?.count())

                }else{
                    isConnected = false // disconect
                    this.delegate?.socketDidDisconect(Exception("Error Package connect"))
                }

                //Thread.sleep(10)
            }

        }catch (e : Exception){
            println(e.toString())
            this.isConnected = false
            this.delegate?.socketDidDisconect(e)
        }

    }


    override fun interrupt() {
        super.interrupt()
        println("Thread Interrupt")
    }


    fun tcpStop(){

    }
    fun tcpReconect(){

    }

    fun newCall(data : ByteArray){
        this.outputStream?.write(data)
    }

}