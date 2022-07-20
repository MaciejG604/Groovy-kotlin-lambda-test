# Groovy-kotlin-lambda-test

This repository shows how easy-to-miss bugs can be introduced with lambdas in gradle build scripts.</br>
As gradle build scripts put all the contents of Project object into the namespace, you have to be cautious </br>
as not to capture any part of rootProject if the lambda is supposed to be used in subprojects configuration. </br>
