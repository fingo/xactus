(:*******************************************************:)
(: Test: K-SeqMINFunc-34                                 :)
(: Written by: Frans Englich                             :)
(: Date: 2006-10-05T18:29:41+02:00                       :)
(: Purpose: A test whose essence is: `string(min(xs:double("NaN"))) eq "NaN"`. :)
(:*******************************************************:)
string(min(xs:double("NaN"))) eq "NaN"