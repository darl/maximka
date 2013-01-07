(defun maxima-ast-from-string (input)
  (maxima::macsyma-read-string input))

(defmacro with-2d-output (&body body)
  `(let ((maxima::$display2d nil))
     ,@body))

(defun maxima-eval (text)
  (let ((result (make-array '(0) :element-type 'base-char :fill-pointer 0 :adjustable t)))
    (with-output-to-string (*standard-output* result)
      (with-2d-output
        (maxima::displa
         (maxima::mfuncall 'maxima::$ev (maxima-ast-from-string text)))))
    result))
