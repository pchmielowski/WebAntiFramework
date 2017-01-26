package net.chmielowski.framework.view

class BasicHtml private constructor(
    val tag: String,
    val printInner: () -> String,
    val params: String = ""
) : Html {
  override fun src(): String {
    return "<$tag${params.html()}>${printInner()}</$tag>"
  }

  constructor(tag: String) : this(
      tag = tag,
      printInner = { "" })

  constructor(tag: String, inner: Html) : this(
      tag = tag,
      printInner = { inner.src() })

  constructor(tag: String, params: String = "", inner: () -> List<Html>) : this(
      tag = tag,
      params = params,
      printInner = {
        inner()
            .map(Html::src)
            .joinToString(separator = "")
      })

  constructor(tag: String, inner: String, params: String = "") : this(
      tag = tag,
      printInner = { inner },
      params = params)

  constructor(tag: String, params: String = "", vararg inners: Html) : this(
      tag = tag,
      params = params,
      inner = { inners.asList() })
}

private fun String.html() = if (this == "") {
  this
} else {
  " %s".format(this)
}