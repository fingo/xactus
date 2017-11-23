(:*******************************************************:)
(:Test: op-numeric-adddec2args-2                          :)
(:Written By: Carmelo Montanez                            :)
(:Date: Thu Dec 16 10:48:15 GMT-05:00 2004                :)
(:Purpose: Evaluates The "op:numeric-add" operator       :)
(: with the arguments set as follows:                    :)
(:$arg1 = xs:decimal(upper bound)                        :)
(:$arg2 = xs:decimal(lower bound)                        :)
(:*******************************************************:)

xs:decimal("999999999999999999") + xs:decimal("-999999999999999999")