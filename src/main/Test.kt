package main

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Test

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
        Assert.assertThat(HtmlTag("html").src(), `is`("<html></html>"))
        Assert.assertThat(HtmlTag("body").src(), `is`("<body></body>"))
        Assert.assertThat(HtmlTag("html", HtmlTag("body")).src(), `is`("<html><body></body></html>"))
        Assert.assertThat(
                HtmlTag("html",
                        HtmlTag("body",
                                HtmlTag("center", "Hello"))
                ).src(),
                `is`("<html><body><center>Hello</center></body></html>"))
        Assert.assertThat(
                HtmlTag("body",
                        listOf(
                                HtmlTag("center", "Hello"),
                                HtmlTag("left", "World"))
                ).src(),
                `is`("<body><center>Hello</center><left>World</left></body>"))

    }
}
