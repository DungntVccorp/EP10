package com.example.dungnt.ep10.core.MOVE.Component

/**
 * Created by dung.nt on 6/27/17.
 */

public enum class ComponentType{
    HTTP,
    TCP,
    OPERATION,
    EVENT,
    LOG,
}

interface BaseComponent{
    fun priority() : Int
    fun loadConfig()
    fun start()
    fun reset()
    fun componentType() : ComponentType
}




