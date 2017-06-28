package com.example.dungnt.ep10.core.MOVE.Connection

import com.example.dungnt.ep10.core.MOVE.Component.BaseComponent
import com.example.dungnt.ep10.core.MOVE.Component.ComponentType
import com.example.dungnt.ep10.core.MOVE.Exception.HttpException
import com.example.dungnt.ep10.core.MOVE.HTTP_ERROR
import com.example.dungnt.ep10.core.MOVE.Operation.HttpOperation
import com.example.dungnt.ep10.core.MOVE.loadConfigHttp
import okhttp3.*
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by dung.nt on 6/27/17.
 */
class HttpComponent : BaseComponent {


    private var callList = Collections.synchronizedMap(LinkedHashMap<Call, String>())


    private var client : OkHttpClient? = null
    var baseUrl : String? = null
    var boundary : String = "--WebKitFormBoundary${String.format("%08x%08x",Random().nextInt(),Random().nextInt())}"



    override fun priority(): Int {
        return 1
    }

    override fun loadConfig() {
        this.loadConfigHttp()

        println(boundary)

        this.client = OkHttpClient.Builder()
                .connectTimeout(30,TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .build()
    }

    override fun start() {
        println("did start ${this.javaClass.name}")
    }

    override fun reset() {
    }

    override fun componentType(): ComponentType {
        return ComponentType.HTTP
    }


    fun sendMessage(reg : Request,className : String){
        if(this.client == null || this.baseUrl == null){
            val cb = Class.forName(className).newInstance()
            if(cb is HttpOperation){
                cb.er = HttpException("Lỗi khởi tạo hoặc url bị bỏ trống",HTTP_ERROR.CONFIG_FAILURE)
                cb.enqueue()
            }

        }else{

            val call = this.client?.newCall(reg)

            if(call != null){
                this.callList.put(call,className)
                call.enqueue(object : Callback{
                    override fun onFailure(call: Call?, e: IOException?) {

                        println("onFailure")
                        var cName = callList.remove(call)
                        if(cName != null){
                            val cb = Class.forName(cName).javaClass.newInstance()
                            if(cb is HttpOperation){
                                cb.er = HttpException(e.toString(),HTTP_ERROR.NONE)
                                cb.enqueue()
                            }
                        }
                    }
                    override fun onResponse(call: Call?, response: Response?) {

                        var cName = callList.remove(call)
                        if(cName != null){
                            val cb = Class.forName(cName).newInstance()
                            if(cb is HttpOperation){
                                cb.replyData = response?.body()?.bytes()
                                cb.enqueue()
                            }
                        }
                    }
                })
            }else{
                val cb = Class.forName(className).newInstance()
                if(cb is HttpOperation){
                    cb.er = HttpException("Lỗi khởi tạo hoặc url bị bỏ trống",HTTP_ERROR.REQUEST_BUILD_FAILURE)
                    cb.enqueue()
                }
            }





        }

    }




}