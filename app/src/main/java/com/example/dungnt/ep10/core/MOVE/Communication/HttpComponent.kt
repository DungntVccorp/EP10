package com.example.dungnt.ep10.core.MOVE.Communication

import com.example.dungnt.ep10.core.MOVE.Component.BaseComponent
import com.example.dungnt.ep10.core.MOVE.Component.ComponentType
import com.example.dungnt.ep10.core.MOVE.Exception.BaseException
import com.example.dungnt.ep10.core.MOVE.Exception.HttpException
import com.example.dungnt.ep10.core.MOVE.HTTP_ERROR
import com.example.dungnt.ep10.core.MOVE.Operation.BaseOperation
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

    data class mCall(val className : String,var onSuccess : ((param : Any) -> Unit)?,var onFailure : ((error : BaseException) -> Unit)?)

    private var callList = Collections.synchronizedMap(LinkedHashMap<Call, mCall>())

    private var client : OkHttpClient? = null
    var baseUrl : String? = null




    override fun priority(): Int {
        return 1
    }

    override fun loadConfig() {
        this.loadConfigHttp()


        this.client = OkHttpClient.Builder()
                .connectTimeout(5,TimeUnit.SECONDS)
                .readTimeout(5,TimeUnit.SECONDS)
                .writeTimeout(5,TimeUnit.SECONDS)
                .build()
    }

    override fun start() {
        println("did start ${this}")
    }

    override fun reset() {
    }

    override fun componentType(): ComponentType {
        return ComponentType.HTTP
    }


    fun sendMessage(operation : HttpOperation){
        if(this.client == null || this.baseUrl == null || operation.request == null){
            val cb = Class.forName(operation.getClassName()).newInstance()
            if(cb is HttpOperation){
                cb.er = HttpException("Lỗi khởi tạo hoặc url bị bỏ trống",HTTP_ERROR.CONFIG_FAILURE)
                cb.onFailure = operation.onFailure
                cb.enqueue()
            }

        }else{

            val call = this.client?.newCall(operation.request)
            if(call != null){

                this.callList.put(call,mCall(operation.getClassName(),operation.onSuccess,operation.onFailure))

                call.enqueue(object : Callback{
                    override fun onFailure(call: Call?, e: IOException?) {

                        var cName = callList.remove(call)
                        if(cName != null){
                            val cb = Class.forName(cName.className).newInstance()
                            if(cb is HttpOperation){
                                cb.er = HttpException(e.toString(),HTTP_ERROR.NONE)
                                cb.onFailure = cName.onFailure
                                cb.enqueue()
                            }
                        }
                    }
                    override fun onResponse(call: Call?, response: Response?) {

                        var cName = callList.remove(call)
                        if(cName != null){
                            val cb = Class.forName(cName.className).newInstance()
                            if(cb is HttpOperation){
                                cb.replyData = response?.body()?.bytes()
                                cb.onSuccess = cName.onSuccess
                                cb.enqueue()
                            }
                        }
                    }
                })
            }else{
                val cb = Class.forName(operation.getClassName()).newInstance()
                if(cb is HttpOperation){
                    cb.er = HttpException("Lỗi khởi tạo hoặc url bị bỏ trống",HTTP_ERROR.REQUEST_BUILD_FAILURE)
                    cb.onFailure = operation.onFailure
                    cb.enqueue()
                }
            }





        }

    }




}