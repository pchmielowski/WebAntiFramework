package main

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket

fun main(args: Array<String>) {
    Application(
            "/hello" to TextPage(
                    "Hello, %s %s!",
                    Param("name"),
                    Param("sur")
            ),
            "/goodbye" to TextPage(
                    "Good bye! Be back on %s!",
                    Param("back")
            ),
            "/html" to HtmlPage(
                    HtmlTag("html",
                            HtmlTag("body",
                                    HtmlTag("center", "Hello")))
            ),
            "/list" to HtmlPage(
                    HtmlTag("html",
                            HtmlTag("body",
                                    HtmlTag("ol", listOf(
                                            HtmlTag("li", "first"),
                                            HtmlTag("li", "second"),
                                            HtmlTag("li", "third")
                                    ))))
            )).start(8080)
}

class Param(val name: String) {
    fun value(request: Request): String {
        return request.param(name)
    }
}

class Application(vararg val endpoints: Pair<String, Response>) {
    fun start(port: Int) {
        val server = ServerSocket(port)
        while (true) {
            server.accept().use { socket ->
                function(socket)
            }
        }
    }

    private fun function(socket: Socket) {
        val request = Request(socket.inputStream)
        endpoints.toMap()[request.endpoint()]
                ?.answer(request, socket)
    }
}

fun param(url: String, name: String): String {
    val value = (".*[\\?&]$name=(\\w+).*").toRegex()
            .matchEntire(url)?.groups?.get(1)?.value ?: return ""
    return value
}

fun endpoint(url: String): String {
    val value = ("GET\\s([^?^\\s]*)?.*").toRegex()
            .matchEntire(url)?.groups?.get(1)?.value ?: return ""
    return value
}


class Request(stream: InputStream) {
    val content = ctnt(stream)

    private fun ctnt(stream: InputStream): String {
        val reader = BufferedReader(
                InputStreamReader(stream)
        )
        val lines = mutableListOf<String>()
        do {
            val line = reader.readLine()
            lines.add(line)
        } while (line != "")
        return lines.joinToString()
    }

    fun param(key: String): String = param(content, key)
    fun endpoint(): String = endpoint(content)
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
                        )).toByteArray())
    }

}


class HtmlPage(val html: HtmlTag) : Response {
    override fun answer(request: Request, socket: Socket) {
        socket.outputStream.write(
                ("HTTP/1.1 200 OK\r\n\r\n" +
                        html.src()).toByteArray())
    }

}


class HtmlTag(val tag: String, val inner: String) {
    fun src(): String {
        return "<%1\$s>%2\$s</%1\$s>".format(tag, inner)
    }

    constructor(tag: String) : this(tag, "")
    constructor(tag: String, inner: HtmlTag) : this(tag, inner.src())
    constructor(tag: String, inners: List<HtmlTag>) : this(
            tag,
            inners.map { i -> i.src() }.joinToString(separator = "")
    )
}


interface Response {

    fun answer(request: Request, socket: Socket)
}
