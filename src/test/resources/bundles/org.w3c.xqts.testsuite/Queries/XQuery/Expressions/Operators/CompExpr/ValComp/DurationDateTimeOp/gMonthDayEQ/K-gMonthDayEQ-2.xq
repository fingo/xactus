(:*******************************************************:)
(: Test: K-gMonthDayEQ-2                                 :)
(: Written by: Frans Englich                             :)
(: Date: 2006-10-05T18:29:37+02:00                       :)
(: Purpose: Simple test of 'eq' for xs:gMonthDay.        :)
(:*******************************************************:)
not(xs:gMonthDay("--03-03") eq xs:gMonthDay("--04-03"))