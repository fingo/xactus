(:*******************************************************:)
(: Test: K-gMonthEQ-5                                    :)
(: Written by: Frans Englich                             :)
(: Date: 2006-10-05T18:29:37+02:00                       :)
(: Purpose: Test that zone offset -00:00 is equal to Z, in xs:gMonth. :)
(:*******************************************************:)
xs:gMonth("--01-00:00") eq xs:gMonth("--01Z")