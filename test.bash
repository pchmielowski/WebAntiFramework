#!/bin/bash

innerGenerator assert {
  curl "$1" 2>/dev/null | grep "$2"; echo $?
}

assert 'localhost:8080/hello?name=Piotrek&sur=chmielowski' 'Hello, Piotrek chmielowski!'
assert 'localhost:8080/goodbye?back=Thursday' 'Good bye! Be back on Thursday!'
assert 'localhost:8080/html' '<html><body><center>Hello</center></body></html>'
