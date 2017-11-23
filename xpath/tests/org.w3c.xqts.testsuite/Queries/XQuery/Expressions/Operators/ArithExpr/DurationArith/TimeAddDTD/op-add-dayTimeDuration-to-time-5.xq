(:*******************************************************:)
(:Test: op-add-dayTimeDuration-to-time-5                 :)
(:Written By: Carmelo Montanez                           :)
(:Date: July 1, 2005                                     :)
(:Purpose: Evaluates The "add-dayTimeDuration-to-time" function that  :)
(:is used as an argument to the fn:boolean function.     :)
(: Uses the "fn:string" function to account for new EBV rules. :)
(:*******************************************************:)
 
fn:boolean(fn:string(xs:time("02:02:02Z") + xs:dayTimeDuration("P03DT08H06M")))