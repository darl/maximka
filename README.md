Maximka
===

*Maximka* - java interface for the Maxima computer algebra system.

ABCL (http://common-lisp.net/project/armedbear/) is used to run Maxima.


Notes
====

1. -XX:MaxPermSize=128m is recomended to sucsessfully load Maxima
2. Loading Maxima may take some time (but happens only once)
3. Sometimes can unexpectedly fail with "Unhandled lisp condition" 
   (try to run ABCLMaximaEvaluatorTest.testPerformance() multiple times)

