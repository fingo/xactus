(:*******************************************************:)
(:Test: op-multiply-dayTimeDuration2args-3                :)
(:Written By: Carmelo Montanez                            :)
(:Date: Tue Apr 12 16:29:08 GMT-05:00 2005                :)
(:Purpose: Evaluates The "op:multiply-dayTimeDuration" operator:)
(: with the arguments set as follows:                    :)
(:$arg1 = xs:dayTimeDuration(upper bound)               :)
(:$arg2 = xs:double(lower bound)                         :)
(:*******************************************************:)

xs:dayTimeDuration("P31DT23H59M59S") * xs:double("0")