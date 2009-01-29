(:*******************************************************:)
(: Test: K-DurationEQ-55                                 :)
(: Written by: Frans Englich                             :)
(: Date: 2006-10-05T18:29:37+02:00                       :)
(: Purpose: The 'lt' operator is not available between xs:yearMonthDuration and xs:duration(reversed operand order). :)
(:*******************************************************:)
xs:yearMonthDuration("P1999Y10M") lt
			   xs:duration("P3DT08H34M12.143S")