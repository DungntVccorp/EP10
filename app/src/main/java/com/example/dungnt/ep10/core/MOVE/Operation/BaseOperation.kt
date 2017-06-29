package com.example.dungnt.ep10.core.MOVE.Operation

import com.example.dungnt.ep10.core.MOVE.Component.ComponentType
import com.example.dungnt.ep10.core.MOVE.Engine
import com.example.dungnt.ep10.core.MOVE.Exception.BaseException

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

    var onSuccess : ((param : Any) -> Unit)? = null
    var onFailure : ((error : BaseException) -> Unit)? = null


    fun getClassName() : String{

        return this.javaClass.name
    }

    override fun run() {
        fire()
    }

    fun enqueue() : BaseOperation{
        val opM = Engine.instance.getComponent(ComponentType.OPERATION)
        if(opM is OperationManager){
            opM.enqueue(this)
        }
        return this
    }

    fun onSuccess(body: (param : Any) -> Unit) : BaseOperation{
        this.onSuccess = body
        return this
    }
    fun onFailure(body : (error : BaseException) -> Unit) : BaseOperation{
        this.onFailure = body
        return this
    }


}


