package com.app.newsapp.ui.ui.util

sealed class Resource<T>(
    var data: T?=null,
    var message:String?=null
) {

    class success<T>(data: T):Resource<T>(data)
    class error<T>(message: String,data: T?=null):Resource<T>(data,message)
    class loading<T>:Resource<T>()
}