package com.example.dungnt.ep10.core.MOVE.Operation

import com.example.dungnt.ep10.core.MOVE.Component.BaseComponent
import com.example.dungnt.ep10.core.MOVE.Component.ComponentType
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by dung.nt on 6/27/17.
 */
class OperationManager : BaseComponent {

    private var executorService : ExecutorService? = null


    override fun priority(): Int {
        return 1
    }

    override fun loadConfig() {
        this.executorService = Executors.newFixedThreadPool(10)
    }

    override fun start() {

    }

    override fun reset() {
        this.executorService?.shutdown()

    }

    override fun componentType(): ComponentType {
        return ComponentType.OPERATION
    }



    fun enqueue(op : BaseOperation){
        this.executorService?.execute(op)
    }





}