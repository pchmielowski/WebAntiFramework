package net.chmielowski.webapp

import net.chmielowski.framework.Action
import net.chmielowski.framework.HtmlPage
import net.chmielowski.framework.HtmlTag
import net.chmielowski.framework.Param
import net.chmielowski.framework.Application

fun main(args: Array<String>) {
  val users = mutableListOf<String>()

  val mainPage = HtmlPage(
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
      ))

  val add = Action(
      Param("user") to { value -> users.add(value) },
      response = HtmlPage(
          HtmlTag("body",
              "bgcolor=gray",
              HtmlTag("h1", "OK"),
              HtmlTag("a", params = "href=/", inner = "Back")
          ))
  )

  Application(
      "/" to mainPage,
      "/add" to add
  ).start(8080)
}
