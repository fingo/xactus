(:*******************************************************:)
(: Test: K-LogicExpr-39                                  :)
(: Written by: Frans Englich                             :)
(: Date: 2006-10-05T18:29:37+02:00                       :)
(: Purpose: An and-test applied on fn:count().           :)
(:*******************************************************:)
count((1, 2, 3, timezone-from-time(current-time()), 4)) and true()