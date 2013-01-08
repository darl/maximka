Maximka
===

**Maximka** - java interface for the Maxima computer algebra system.

ABCL (http://common-lisp.net/project/armedbear/) is used to run Maxima.

How to use
====
*todo*

Dependencies
====
1. *abcl (version 1.1.0)* - to load and run Maxima (for some reason 1.0.1 is last version in maven)
2. *maxima.jar* - packed and slightly patched sources of Maxima (can be found in *lib* directory)


Notes
====

1. -XX:MaxPermSize=128m is recomended to sucsessfully load Maxima
2. Loading Maxima may take some time (but happens only once)
3. Sometimes can unexpectedly fail with "Unhandled lisp condition" 
   (try to run ABCLMaximaEvaluatorTest.testPerformance() multiple times)

