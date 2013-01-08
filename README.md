Maximka
===

**Maximka** - java interface for the Maxima computer algebra system.

ABCL (http://common-lisp.net/project/armedbear/) is used to run Maxima.

How to use
====
1. add repo
<pre><code>&lt;repositories>
      &lt;repository>
        &lt;id>maximka-repo&lt;/id>
        &lt;url>http://dl.dropbox.com/u/11987354/repo &lt;/url>
      &lt;/repository>
&lt;/repositories>
</code></pre>
2. add dependency
<pre><code>&lt;dependency>
      &lt;groupId>com.github.darl&lt;/groupId>
      &lt;artifactId>maximka&lt;/artifactId>
      &lt;version>1.0&lt;/version>
&lt;/dependency></code></pre>
3. use it!
<pre><code>public class App {
        public static void main(String[] args) throws MaximaException {
            MaximaEvaluator ev = new ABCLMaximaEvaluator();
            System.out.println(ev.eval("integrate(1/x, x, 2, 7)")); //should print log(7)-log(2)
        }
}</code></pre>

Provided dependencies
====
1. *abcl (version 1.1.0)* - to load and run Maxima (for some reason 1.0.1 is last version in maven)
2. *maxima.jar* - packed and slightly patched sources of Maxima


Notes
====

1. -XX:MaxPermSize=128m is recomended to sucsessfully load Maxima
2. Loading Maxima may take some time (but happens only once)
3. Sometimes can unexpectedly fail with "Unhandled lisp condition" 
   (try to run ABCLMaximaEvaluatorTest.testPerformance() multiple times)

How to build maxima.jar
====
1. download maxima sources from http://sourceforge.net/projects/maxima/files/Maxima-source/
2. unpack sources to %maxima-sources%
3. cd %maxima-sources%/src
4. edit maxima.asd 
<pre>--- src/maxima.asd   2 Jan 2011 06:11:33 -0000	1.18
+++ src/maxima.asd	27 Feb 2011 09:28:47 -0000
@@ -9,9 +9,9 @@
     ;; Don't try to optimize so much in ECL.
     ;; Therefore functions can be redefined (essential for share libraries).
     #+ecl (declaim (optimize (debug 2)))
-
+#+nil
     (defvar *binary-output-dir* "binary-ecl")
-
+#+nil
     (defmethod output-files :around ((operation compile-op) (c source-file))
       (let* ((source (component-pathname c))
             (source-dir (pathname-directory source))</pre>
4. run abcl repl: *java -XX:MaxPermSize=128m -jar .../abcl.jar*
5. execute commands
<pre>
(require :abcl-contrib)
(require :asdf-jar)
(asdf-jar:package :maxima)</pre>
6. you will see location of your file in temp directory

p.s. if you cannot load *asdf-jar* try to replace *abcl-contrib.jar* with file from older release (for example 1.0.1)
