package com.example.dungnt.ep10.core.MOVE.Connection

import com.example.dungnt.ep10.core.MOVE.Component.BaseComponent
import com.example.dungnt.ep10.core.MOVE.Component.ComponentType
import com.example.dungnt.ep10.core.MOVE.loadConfigTcp

/**
 * Created by dung.nt on 6/27/17.
 */
class TcpComponent : BaseComponent{


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