(:*******************************************************:)
(: Test: K-FunctionProlog-7                              :)
(: Written by: Frans Englich                             :)
(: Date: 2006-10-05T18:29:39+02:00                       :)
(: Purpose: Arguments in functions cannot have default values initialized with '='(or in any other way). :)
(:*******************************************************:)
declare function local:myFunction($arg = 1 as xs:integer)
		{1};
		true()