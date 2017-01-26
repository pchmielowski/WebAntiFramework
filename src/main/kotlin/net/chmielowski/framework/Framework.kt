package net.chmielowski.framework

import net.chmielowski.framework.view.BasicHtml
import net.chmielowski.framework.view.Html
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket

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
    endpoints.toMap()
        .getOrElse(request.endpoint()) {
          HtmlResponse(BasicHtml("body",
              "bgcolor=gray",
              BasicHtml("h1", ":(")
          ), "404")
        }
        .answer(request, socket)
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

class HtmlResponse(val inner: Html, val code: String) : Response {
  override fun answer(request: IRequest, socket: Socket) = socket.outputStream.write(
      "HTTP/1.1 %s\r\n\r\n <html>%s</html>"
          .format(code, inner.src())
          .toByteArray())

  constructor(inner: Html) : this(inner, "200 OK")
}

class Action(
    val maping: Pair<Param, (String) -> Unit>,
    val response: Response) : Response {
  override fun answer(request: IRequest, socket: Socket) {
    val value = maping.first.value(request)
    maping.second.invoke(value)
    response.answer(request, socket)
  }

}

interface Response {

  fun answer(request: IRequest, socket: Socket)
}