package com.example.dungnt.ep10

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.dungnt.ep10.VietTalk.Operation.Profile.AuthenticateOperation
import com.example.dungnt.ep10.core.MOVE.Communication.TcpComponent
import com.example.dungnt.ep10.core.MOVE.Component.ComponentType
import com.example.dungnt.ep10.core.MOVE.Engine
import comm.CommProfile
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Engine.instance.start()
        setContentView(R.layout.activity_main)
        AuthenticateOperation().onFailure({
            println("onFailure")
        }).onSuccess({param ->
            if(param is CommProfile.Authenticate.Reply){
                println(param.host)
                println(param.port)

                var tcp = Engine.instance.getComponent(ComponentType.TCP)
                if(tcp is TcpComponent){
                    tcp.hostName = param.host
                    tcp.hostPort = param.port
                    tcp.sid      = param.sessionId
                    tcp.uid      = param.userId
                    tcp.Connect()
                }

            }
        }).onFailure { error ->
            println(error.toString())
        }.enqueue()
    }

    override fun onStart() {
        super.onStart()

    }










}

