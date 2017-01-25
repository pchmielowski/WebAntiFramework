package net.chmielowski.webapp

class Users {
  val all = mutableListOf<String>()
  fun add(name: String) {
    all.add(name)
  }

}