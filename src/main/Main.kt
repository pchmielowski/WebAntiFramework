package main

import framework.*

fun main(args: Array<String>) {
  val users = mutableListOf<String>()
  Application(
      "/" to HtmlPage(
          HtmlTag("body",
              "bgcolor=gray",
              HtmlTag("h1", "Users:"),
              HtmlTag("ol",
                  {
                    users.map { user -> HtmlTag("li", user) }
                  }),
              HtmlTag("form",
                  "action=add method=get",
                  HtmlTag("input", params = "name=user"),
                  HtmlTag("input", params = "type=submit value=Add")
              )
          )),
      "/add" to Action(
          Param("user") to { value -> users.add(value) },
          response = HtmlPage(
              HtmlTag("body",
                  "bgcolor=gray",
                  HtmlTag("h1", "OK"),
                  HtmlTag("a", params = "href=/", inner = "Back")
              ))
      )
  ).start(8080)
}


