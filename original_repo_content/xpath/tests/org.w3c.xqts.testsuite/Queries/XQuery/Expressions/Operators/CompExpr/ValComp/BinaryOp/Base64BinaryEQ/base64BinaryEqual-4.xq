(:Test: base64BinaryEqual-4                                       :)
(:Description: Simple operation with xs:base64binary values as part of a   :)
(:logical expression.  Use "and" and "eq" operators with "fn:true" function.:)

(xs:base64Binary("cmxjZ3R4c3JidnllcmVuZG91aWpsbXV5Z2NhamxpcmJkaWFhbmFob2VsYXVwZmJ1Z2dmanl2eHlzYmhheXFtZXR0anV2dG1q") eq xs:base64Binary("d2J1bnB0Y3lucWtvYXdpb2xoZWNwZXlkdG90eHB3ZXJqcnliZXFubmJjZXBmbGx3aGN3cmNndG9xb2hvdHdlY2pzZ3h5bnlp")) and fn:true()