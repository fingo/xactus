(:*******************************************************:)
(: Test: K-SeqExprCast-597                               :)
(: Written by: Frans Englich                             :)
(: Date: 2006-10-05T18:29:38+02:00                       :)
(: Purpose: 'castable as' involving xs:integer as source type and xs:untypedAtomic as target type should always evaluate to true. :)
(:*******************************************************:)
xs:integer("6789") castable as xs:untypedAtomic