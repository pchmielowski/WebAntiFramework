package net.chmielowski.webapp

import net.chmielowski.framework.*

/*
 * The Equivalent of Controller in MVC
 */
fun main(args: Array<String>) {
  val users = Users()
  Application(
      "/" to HtmlResponse(
          MainPageHtml({
            users.all.map(::UserHtml)
          })),
      "/add" to Action(
          Param("user") to { name -> users.add(name) },
          response = HtmlResponse(AddedPageHtml())))
      .start(8080)
}
