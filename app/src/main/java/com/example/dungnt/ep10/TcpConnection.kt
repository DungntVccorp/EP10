package com.example.dungnt.ep10

import android.content.Context
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket


interface TcpConnectionDelegate {
    fun socketDidDisconect(error : Exception)
    fun socketDidReadPackage()
}

class TcpConnection(var contect : Context,val hostName : String?,var hostPort : Int,var delegate : TcpConnectionDelegate?) : Thread() {

    private var socket : Socket? = null

    private var address : InetSocketAddress? = null

    private var inputStream : DataInputStream? = null

    private var outputStream : DataOutputStream? = null

    private var isConnected : Boolean = false


    override fun start() {
        super.start()

    }

    override fun run() {
        super.run()

        println(Thread.currentThread().name + Thread.currentThread().priority)

        println("Thread Run")


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

            // Test Send Ping



            while (isConnected){
                if (inputStream?.read() != -1){
                    // read package data form server

                }else{
                    isConnected = false // disconect

                    this.delegate?.socketDidDisconect(Exception("Error Package connect"))

                }



                Thread.sleep(10)
            }


            inputStream?.close()
            outputStream?.close()
            socket?.close()

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



}