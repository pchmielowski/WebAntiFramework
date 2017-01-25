package net.chmielowski.webapp

import net.chmielowski.framework.*
import net.chmielowski.webapp.User
import java.net.Socket

class MainPage(val showUsersFunction: () -> List<User>) : Tag {
  override fun src() = HtmlTag("body",
      "bgcolor=gray",
      HtmlTag("h1", "Users:"),
      HtmlTag("ol", showUsersFunction),
      HtmlTag("form",
          "action=add method=get",
          HtmlTag("input", params = "name=user"),
          HtmlTag("input", params = "type=submit value=Add")
      )
  ).src()
}

class User(val name: String) : Tag {
  override fun src() = HtmlTag("li", name).src()
}

class AddedOKPage : Tag {
  override fun src(): String {
    return HtmlTag("body",
        "bgcolor=gray",
        HtmlTag("h1", "OK"),
        HtmlTag("a", params = "href=/", inner = "Back")
    ).src()
  }
}