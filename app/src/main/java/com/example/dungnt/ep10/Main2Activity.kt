package com.example.dungnt.ep10

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        btn_call_function.text = "CALL FUNCTION"
        btn_call_function.setOnClickListener { println("HELLO") }

    }


    fun testABC(){
        println("Test ABC")
    }


    override fun onStart() {
        super.onStart()




        println("call class")
        println("Call Function")






    }





}
