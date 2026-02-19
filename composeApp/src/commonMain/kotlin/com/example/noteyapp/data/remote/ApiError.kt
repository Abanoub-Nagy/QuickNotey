package com.example.noteyapp.data.remote


sealed class ApiError : Throwable() {

    class Unauthorized : ApiError()

    class Network : ApiError()

    data class Server(val code: Int) : ApiError()

    data class Unknown(override val message: String?) : ApiError()
}
