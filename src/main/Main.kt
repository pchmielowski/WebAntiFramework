package main

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket

fun main(args: Array<String>) {
    Application(
            EndPoint(
                    location = "/",
                    response = TextPage(
                            "Hello, %s %s!",
                            Param("name"),
                            Param("sur")
                    ))
    ).start(8080)
}

class Param(val name: String) {
    fun value(request: Request): String {
        return request.param(name)
    }
}

class EndPoint(val location: String, val response: TextPage) {
    fun page(): Response = response
    fun isValid(actualLocation: String) = actualLocation == location
}

class Application(val endpoint: EndPoint) {
    fun start(port: Int) {
        val server = ServerSocket(port)
        while (true) {
            println("loop")
            server.accept().use { socket ->
                function(socket)
            }
        }
    }

    private fun function(socket: Socket) {
        println("accepted")
        val request = Request(socket.getInputStream())
        if (endpoint.isValid(request.endpoint())) {
            println(request.endpoint() + " is OK")
            endpoint.page()
                    .answer(request, socket)
        } else {
            println(request.endpoint() + " is not OK")
        }
    }
}

fun param(url: String, name: String): String {
    val value = (".*[\\?&]$name=(\\w+).*").toRegex()
            .matchEntire(url)?.groups?.get(1)?.value ?: return ""
    return value
}

fun endpoint(url: String): String {
    val value = ("GET\\s([^?]*)?.*").toRegex()
            .matchEntire(url)?.groups?.get(1)?.value ?: return ""
    return value
}


class Request(stream: InputStream) {
    var S = ""

    init {
        val bufferedReader = BufferedReader(
                InputStreamReader(stream)
        )
        var line: String?
        while (true) {
            line = bufferedReader.readLine()
            if (line == null) break
            if (line == "") break
            S += line
        }
    }

    fun param(key: String): String = param(S, key)
    fun endpoint(): String = endpoint(S)
}

class TextPage(val msg: String, vararg val param: Param) : Response {
    override fun answer(request: Request, socket: Socket) {
        socket.outputStream.write(
                ("HTTP/1.1 200 OK\r\n\r\n" +
                        msg.format(
                                *param.map {
                                    p ->
                                    p.value(request)
                                }.toTypedArray()
                        )
                        )
                        .toByteArray())
    }

}

interface Response {

    fun answer(request: Request, socket: Socket)
}
