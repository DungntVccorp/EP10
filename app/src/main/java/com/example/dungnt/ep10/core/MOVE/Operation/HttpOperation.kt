package com.example.dungnt.ep10.core.MOVE.Operation

import com.example.dungnt.ep10.core.MOVE.Component.ComponentType
import com.example.dungnt.ep10.core.MOVE.Communication.HttpComponent
import com.example.dungnt.ep10.core.MOVE.Engine
import com.example.dungnt.ep10.core.MOVE.Exception.HttpException
import com.google.protobuf.ExtensionRegistryLite
import comm.CommProfile
import comm.CommModel
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody

/**
 * Created by dung.nt on 6/27/17.
 */
abstract class HttpOperation : BaseOperation(){
    var replyData : ByteArray? = null
    var er : HttpException?  = null
    var request : Request? = null

    abstract fun apiId() : Int
    abstract fun buildRequest() : ByteArray?
    abstract fun onReplyRequest()


    fun buildHeader() : ByteArray?{
        return null
    }


    override protected fun fire() {

        println("HTTP FIRE ${Thread.currentThread().name} ${this.toString()} ")
        if (replyData == null){
            var data = this.buildRequest()
            if(data != null){
                var httpM = Engine.instance.getComponent(ComponentType.HTTP)
                if(httpM is HttpComponent){

                    val parseType = MediaType.parse("application/octet-stream")
                    var requestBody = RequestBody.create(parseType,data)

                    /// BUILD REQUEST


                    request = Request.Builder()
                            .url(httpM.baseUrl + this.apiId())
                            .post(requestBody)
                            .build()

                    httpM.sendMessage(this)
                }
            }
        }else if(er != null){
            // REPLY
            if(er != null){
                if(this.onFailure != null){
                    this.onFailure!!(er!!)
                }
            }
        }else if(replyData != null){
            this.onReplyRequest()
        }

    }
}

