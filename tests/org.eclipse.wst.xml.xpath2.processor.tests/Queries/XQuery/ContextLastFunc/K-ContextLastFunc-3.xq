(:*******************************************************:)
(: Test: K-ContextLastFunc-3                             :)
(: Written by: Frans Englich                             :)
(: Date: 2006-10-05T18:29:41+02:00                       :)
(: Purpose: A test whose essence is: `deep-equal((1, 2, 3)[count((last(), last())) eq 2], (1, 2, 3))`. :)
(:*******************************************************:)
deep-equal((1, 2, 3)[count((last(), last())) eq 2],
						 (1, 2, 3))