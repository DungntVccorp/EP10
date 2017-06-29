package com.example.dungnt.ep10.core.MOVE.Communication

import com.example.dungnt.ep10.core.MOVE.Component.BaseComponent
import com.example.dungnt.ep10.core.MOVE.Component.ComponentType
import com.example.dungnt.ep10.core.MOVE.loadConfigTcp
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.InetSocketAddress
import java.net.Socket

/**
 * Created by dung.nt on 6/27/17.
 */
class TcpComponent : BaseComponent{

    var hostName : String? = ""

    var hostPort : Int = 0

    private var socket : Socket? = null

    private var address : InetSocketAddress? = null

    private var inputStream : DataInputStream? = null

    private var outputStream : DataOutputStream? = null

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


}