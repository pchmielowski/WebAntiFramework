package main

import framework.*

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


