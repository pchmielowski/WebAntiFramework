package main

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket

fun main(args: Array<String>) {
  val users = mutableListOf<String>()
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
      "/add" to Action(
          Param("user"), { name -> users.add(name) },
          response = HtmlPage(
              HtmlTag("html",
                  HtmlTag("body",
                      listOf(
                          HtmlTag("h2", "OK"),
                          HtmlTag("a", "See all users!", params = "href=list")
                      ))))
      ),
      "/list" to HtmlPage(
          HtmlTag("html",
              HtmlTag("body",
                  HtmlTag("ol",
                      {
                        users.map { HtmlTag("li", it) }
                      })))
      )).start(8080)
}

class Param(val name: String) {
  fun value(request: IRequest): String {
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


class Request(stream: InputStream) : IRequest {
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

  override fun param(name: String): String = param(content, name)
  fun endpoint(): String = endpoint(content)
}

interface IRequest {
  fun param(name: String): String
}

class TextPage(val msg: String, vararg val param: Param) : Response {
  override fun answer(request: IRequest, socket: Socket) {
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
  override fun answer(request: IRequest, socket: Socket) {
    socket.outputStream.write(
        ("HTTP/1.1 200 OK\r\n\r\n" +
            html.src()).toByteArray())
  }

}


class Action(val param: Param,
             val function: (String) -> Unit,
             val response: Response) : Response {
  override fun answer(request: IRequest, socket: Socket) {
    val value = param.value(request)
    function.invoke(value)
    response.answer(request, socket)
  }

}


class HtmlTag private constructor(
    val tag: String,
    val innerGenerator: () -> String,
    val params: String = "",
    i: Int = 0 // Dirty trick to distinct two function taking ctors
) {
  fun src(): String {
    return "<%1\$s%3\$s>%2\$s</%1\$s>".format(
        tag,
        innerGenerator(),
        if (params == "") "" else " " + params)
  }

  constructor(tag: String) : this(tag, innerGenerator = { "" })
  constructor(tag: String, inner: String, params: String = "") : this(
      tag,
      innerGenerator = { inner },
      params = params)

  constructor(tag: String, inner: HtmlTag) : this(tag, innerGenerator = { inner.src() })
  constructor(tag: String, inners: List<HtmlTag>) : this(
      tag,
      innerGenerator = { inners.map(HtmlTag::src).joinToString(separator = "") }
  )

  constructor(tag: String, f: () -> List<HtmlTag>) : this(
      tag,
      innerGenerator = { f().map { it.src() }.joinToString("") }
  )


}


interface Response {

  fun answer(request: IRequest, socket: Socket)
}
