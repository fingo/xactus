(:*******************************************************:)
(: Test: K-ValCompTypeChecking-29                        :)
(: Written by: Frans Englich                             :)
(: Date: 2006-10-05T18:29:36+02:00                       :)
(: Purpose: Value comparison involving xs:untypedAtomic, which leads to an inexistent operator mapping. :)
(:*******************************************************:)
xs:untypedAtomic("3") ne xs:decimal(3.1)