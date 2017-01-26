package net.chmielowski.framework.view

import net.chmielowski.framework.view.Html

class BasicHtml private constructor(
    val tag: String,
    val printInner: () -> String,
    val params: String = ""
) : Html {
  override fun src(): String {
    return "<%1\$s%3\$s>%2\$s</%1\$s>".format(
        tag,
        printInner(),
        params.html())
  }

  constructor(tag: String) : this(
      tag = tag,
      printInner = { "" })

  constructor(tag: String, inner: Html) : this(
      tag = tag,
      printInner = { inner.src() })

  constructor(tag: String, inner: () -> List<Html>) : this(
      tag = tag,
      printInner = {
        inner()
            .map(Html::src)
            .joinToString("")
      })

  constructor(tag: String, inner: String, params: String = "") : this(
      tag = tag,
      printInner = { inner },
      params = params)

  constructor(tag: String, params: String = "", vararg inners: BasicHtml) : this(
      tag = tag,
      printInner = {
        inners
            .map(Html::src)
            .joinToString(separator = "")
      },
      params = params)
}

private fun String.html() = if (this == "") {
  this
} else {
  " %s".format(this)
}