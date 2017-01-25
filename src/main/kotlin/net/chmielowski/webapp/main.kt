package net.chmielowski.webapp

import net.chmielowski.framework.*

fun main(args: Array<String>) {
  val users = mutableListOf<String>()

  Application(
      "/" to HtmlPage(
          MainPage({
            users.map { user -> User(user) }
          })),
      "/add" to Action(
          Param("user") to { value -> users.add(value) },
          response = HtmlPage(AddedOKPage()))

  ).start(8080)
}
