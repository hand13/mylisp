(define x (lambda (a) (if (= a 1) 1 ( + a (x (- a 1))))))
(display (x 6))