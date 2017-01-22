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
      "/add" to Action(
          Param("user") to { name -> users.add(name) },
          response = HtmlPage(
              HtmlTag("html",
                  HtmlTag("body",
                      listOf(
                          HtmlTag("h2", "OK"),
                          HtmlTag("a", params = "href=list", inner = "See all users!")
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


