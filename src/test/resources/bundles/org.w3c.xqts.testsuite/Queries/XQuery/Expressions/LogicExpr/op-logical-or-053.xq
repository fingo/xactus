(:*******************************************************:)
(: Test: op-logical-or-053.xq                            :)
(: Written By: Lalith Kumar                              :)
(: Date: Thu May 12 05:53:51 2005                        :)
(: Purpose: Logical 'or'  using double values            :)
(:*******************************************************:)

   <return>
     { xs:double('NaN') or xs:double(1) }
   </return>