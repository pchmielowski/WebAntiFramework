#!/bin/bash

function assert {
  curl "$1" 2>/dev/null | grep "$2"; echo $?
}

assert 'localhost:8080/hello?name=Piotrek&sur=chmielowski' 'Hello, Piotrek chmielowski!'
assert 'localhost:8080/goodbye?back=Thursday' 'Good bye! Be back on Thursday!'

assert 'localhost:8080/add?user=First' 'OK'
assert 'localhost:8080/add?user=Second' 'OK'
assert 'localhost:8080/list' '<html><body><ol><li>First</li><li>Second</li></ol></body></html>'
