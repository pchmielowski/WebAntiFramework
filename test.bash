curl 'localhost:8080/hello?name=Piotrek&sur=chmielowski' 2>/dev/null | grep 'Hello, Piotrek chmielowski!'; echo $?
curl 'localhost:8080/goodbye?back=Thursday' 2>/dev/null | grep 'Good bye! Be back on Thursday!'; echo $?
