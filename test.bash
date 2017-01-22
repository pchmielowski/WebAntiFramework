#!/bin/bash

function assert {
  curl "$1" 2>/dev/null | grep "$2"; echo $?
}

assert 'localhost:8080/hello?name=Piotrek&sur=chmielowski' 'Hello, Piotrek chmielowski!'

assert 'localhost:8080/add?user=First' 'OK'
assert 'localhost:8080/add?user=Second' 'OK'
assert 'localhost:8080/list' '<inner><inner><ol><li>First</li><li>Second</li></ol></inner></inner>'
