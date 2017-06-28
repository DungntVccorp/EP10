package com.example.dungnt.ep10.core.MOVE.Operation

import com.example.dungnt.ep10.core.MOVE.Component.ComponentType
import com.example.dungnt.ep10.core.MOVE.Connection.HttpComponent
import com.example.dungnt.ep10.core.MOVE.Engine
import com.example.dungnt.ep10.core.MOVE.Exception.HttpException
import com.google.protobuf.ExtensionRegistryLite
import comm.CommProfile
import comm.CommModel
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import java.io.InputStream

/**
 * Created by dung.nt on 6/27/17.
 */
abstract class HttpOperation : BaseOperation(){
    var replyData : ByteArray? = null
    val httpId : Int = 0
    var er : HttpException?  = null


    abstract fun apiId() : Int
    abstract fun buildRequest() : ByteArray?
    abstract fun onReplyRequest()


    fun buildHeader() : ByteArray?{
        return null
    }


    override protected fun fire() {



        println("HTTP FIRE ${Thread.currentThread().name} ")
        if (replyData == null){
            var data = this.buildRequest()
            if(data != null){
                var httpM = Engine.instance.getComponent(ComponentType.HTTP)
                if(httpM is HttpComponent){

                    val parseType = MediaType.parse("application/octet-stream")
                    var requestBody = RequestBody.create(parseType,data)

                    /// BUILD REQUEST


                    var rq = Request.Builder()
                            .url(httpM.baseUrl + this.apiId())
                            .post(requestBody)
                            .build()

                    httpM.sendMessage(rq,this.getClassName())
                }
            }
        }else{
            // REPLY


            this.onReplyRequest()
        }




    }
}


class xlxl : HttpOperation(){

    override fun apiId(): Int {
        return CommProfile.Authenticate.ID_FIELD_NUMBER
    }

    override fun buildRequest(): ByteArray? {

        return CommProfile.Authenticate.Request.newBuilder()
                .setDeviceName("dungnt")
                .setPassword("2")
                .setPlatform(2)
                .setUid("841645352285")
                .setPlatformVersion("10.3.3")
                .build().toByteArray()
    }

    override fun onReplyRequest() {
        ExtensionRegistryLite.newInstance().unmodifiable
        val extension = ExtensionRegistryLite.newInstance()
        extension.add(CommProfile.Authenticate.iD)
        val rep = CommModel.Reply.parseFrom(this.replyData!!,extension)
        var rep2 = rep.getExtension(CommProfile.Authenticate.iD)
        if(rep2 is CommProfile.Authenticate.Reply){
            println(rep2.toString())
        }



    }
}

class xxxxxx : HttpOperation{

    var telNumber: String? = "841645352285"


    constructor() : super() {
        println("INIT")
    }

    override fun buildRequest(): ByteArray? {

        return CommProfile.CheckAccount.Request.newBuilder().setTel(telNumber).build().toByteArray()
    }

    override fun onReplyRequest() {
        println("REPLY")
        ExtensionRegistryLite.newInstance().unmodifiable
        val extension = ExtensionRegistryLite.newInstance()
        extension.add(CommProfile.CheckAccount.iD)
        var repabc = CommModel.Reply.parseFrom(this.replyData!!,extension)
        if (repabc.hasType()){
            println(repabc.type)
            val rep2 = repabc.getExtension(CommProfile.CheckAccount.iD)
            if (rep2 is CommProfile.CheckAccount.Reply){
                println(rep2.state)

            }

        }

        //this.replyData!!,ExtensionRegistry.newInstance().add(CommProfile.CheckAccount.iD)

    }

    override fun apiId(): Int {
        return CommProfile.CheckAccount.ID_FIELD_NUMBER
    }

}
