package framework

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test
import java.net.Socket

class Tests {
  @Test fun endpoint_test() {
    Assert.assertThat(endpoint("GET /?code=1234"), `is`("/"))
  }


  @Test fun param() {
    Assert.assertThat(param("wp.pl?code=1234", "code"), `is`("1234"))
    Assert.assertThat(param("wp.pl?code=abc", "code"), `is`("abc"))
    Assert.assertThat(param("wp.pl?code=abc&another=xyz", "code"), `is`("abc"))
    Assert.assertThat(param("127.0.0.1:8080?name=Piotrek", "name"), `is`("Piotrek"))
  }

  @Test fun html() {
    Assert.assertThat(HtmlTag("inner").src(), `is`("<inner></inner>"))
    Assert.assertThat(HtmlTag("inner").src(), `is`("<inner></inner>"))
    Assert.assertThat(HtmlTag("inner", HtmlTag("inner")).src(), `is`("<inner><inner></inner></inner>"))
    Assert.assertThat(
        HtmlTag("inner",
            HtmlTag("inner",
                HtmlTag("center", "Hello"))
        ).src(),
        `is`("<inner><inner><center>Hello</center></inner></inner>"))
    Assert.assertThat(
        HtmlTag("inner", "",
            HtmlTag("center", "Hello"),
            HtmlTag("left", "World")
        ).src(),
        `is`("<inner><center>Hello</center><left>World</left></inner>"))
    Assert.assertThat(HtmlTag("a", "link", params = "href='url'").src(), `is`("<a href='url'>link</a>"))
  }

  @Ignore @Test fun action() {
    Action(
        Param("user") to { p -> println(p) },
        HtmlPage(
            HtmlTag("inner",
                HtmlTag("inner", "",
                    HtmlTag("center", "OK"),
                    HtmlTag("a", "click")
                ))
        )
    ).answer(MockedRequest(), Socket())
  }
}

class MockedRequest : IRequest {
  override fun param(name: String): String = "Piotrek"

}

