package net.chmielowski.webapp

import net.chmielowski.framework.*
import net.chmielowski.framework.view.BasicHtml
import net.chmielowski.framework.view.Html
import net.chmielowski.webapp.UserHtml
import java.net.Socket

class MainPageHtml(val showUsersFunction: () -> List<UserHtml>) : Html {
  override fun src() = BasicHtml("body",
      "bgcolor=gray",
      BasicHtml("h1", "Users:"),
      BasicHtml("ol", showUsersFunction),
      BasicHtml("form",
          "action=add method=get",
          BasicHtml("input", params = "name=user"),
          BasicHtml("input", params = "type=submit value=Add")
      )
  ).src()
}

class UserHtml(val name: String) : Html {
  override fun src() = BasicHtml("li", name).src()
}

class AddedPageHtml : Html {
  override fun src(): String {
    return BasicHtml("body",
        "bgcolor=gray",
        BasicHtml("h1", "OK"),
        BasicHtml("a", params = "href=/", inner = "Back")
    ).src()
  }
}