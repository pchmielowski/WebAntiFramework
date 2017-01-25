package net.chmielowski.webapp

import net.chmielowski.framework.*

/*
 * The Equivalent of Controller in MVC
 */
fun main(args: Array<String>) {
  val users = Users()
  Application(
      "/" to HtmlPage(
          MainPage({
            users.all.map(::User)
          })),
      "/add" to Action(
          Param("user") to { name -> users.add(name) },
          response = HtmlPage(AddedOKPage())))
      .start(8080)
}
