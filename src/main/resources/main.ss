(define map (lambda (f n ) (if (not (nullcar? n )) (begin (f (car n) ) (map f (cdr n ))) '())))
(map display '(12 21 12))
