package com.example.dungnt.ep10.core.MOVE.Operation

import com.example.dungnt.ep10.core.MOVE.Component.ComponentType
import com.example.dungnt.ep10.core.MOVE.Engine

/**
 * Created by dung.nt on 6/27/17.
 */
//interface BaseOperation : Runnable{
//    fun buildRequest() : ByteArray?
//    fun onReplyRequest(replyData : ByteArray)
//}

abstract class BaseOperation : Runnable{
    constructor()

    abstract protected fun fire()

    fun getClassName() : String{
        return this.javaClass.name
    }

    override fun run() {
        fire()
    }

    fun enqueue(){
        val opM = Engine.instance.getComponent(ComponentType.OPERATION)
        if(opM is OperationManager){
            opM.enqueue(this)
        }


    }


}


