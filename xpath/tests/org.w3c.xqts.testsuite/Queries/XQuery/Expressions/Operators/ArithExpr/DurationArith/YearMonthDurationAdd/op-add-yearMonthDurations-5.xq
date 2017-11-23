(:*******************************************************:)
(:Test: op-add-yearMonthDurations-5                      :)
(:Written By: Carmelo Montanez                           :)
(:Date: June 29 2005                                     :)
(:Purpose: Evaluates The "add-yearMonthDurations" function that  :)
(:is used as an argument to the fn:boolean function.     :)
(: Apply "fn:string" function to account for new EBV.     :)
(:*******************************************************:)
 
fn:boolean(fn:string(xs:yearMonthDuration("P05Y08M") + xs:yearMonthDuration("P03Y06M")))