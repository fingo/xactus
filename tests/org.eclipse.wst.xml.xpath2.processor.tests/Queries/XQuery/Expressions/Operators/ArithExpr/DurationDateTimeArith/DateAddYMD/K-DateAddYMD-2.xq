(:*******************************************************:)
(: Test: K-DateAddYMD-2                                  :)
(: Written by: Frans Englich                             :)
(: Date: 2006-10-05T18:29:36+02:00                       :)
(: Purpose: Simple testing involving operator '+' between xs:yearMonthDuration and xs:date. :)
(:*******************************************************:)
xs:yearMonthDuration("P3Y7M") + xs:date("1999-08-12")
		                	 eq xs:date("2003-03-12")