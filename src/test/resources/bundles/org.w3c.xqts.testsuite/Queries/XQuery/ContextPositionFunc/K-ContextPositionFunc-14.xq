(:*******************************************************:)
(: Test: K-ContextPositionFunc-14                        :)
(: Written by: Frans Englich                             :)
(: Date: 2006-10-05T18:29:41+02:00                       :)
(: Purpose: fn:position() can never return anything less or equal to 0(<=). :)
(:*******************************************************:)
empty((1, 2, 3, current-time(), current-date(), 6, 7, 8)
[position() <= 0])