(:*******************************************************:)
(: Test: K-WhereExpr-9                                   :)
(: Written by: Frans Englich                             :)
(: Date: 2006-10-05T18:29:37+02:00                       :)
(: Purpose: A for/where expression combined with fn:boolean. :)
(:*******************************************************:)
for $i in (1, 2, current-time())[1] where fn:boolean($i treat as xs:integer) return true()