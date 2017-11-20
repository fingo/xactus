(:*******************************************************:)
(:Test: op-numeric-less-thandbl2args-3                    :)
(:Written By: Carmelo Montanez                            :)
(:Date: Thu Dec 16 10:48:16 GMT-05:00 2004                :)
(:Purpose: Evaluates The "op:numeric-less-than" operator :)
(: with the arguments set as follows:                    :)
(:$arg1 = xs:double(upper bound)                         :)
(:$arg2 = xs:double(lower bound)                         :)
(:*******************************************************:)

xs:double("1.7976931348623157E308") lt xs:double("-1.7976931348623157E308")