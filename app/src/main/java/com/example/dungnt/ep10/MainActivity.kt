package com.example.dungnt.ep10

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.dungnt.ep10.core.MOVE.Engine
import com.example.dungnt.ep10.core.MOVE.Operation.xlxl
import com.example.dungnt.ep10.core.MOVE.Operation.xxxxxx
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Engine.instance.start()
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        btn_start.setOnClickListener {

            xxxxxx().enqueue()
            xlxl().enqueue()
            //TcpConnection(applicationContext,"103.31.127.14",5000,this).start()
        }
    }










}

