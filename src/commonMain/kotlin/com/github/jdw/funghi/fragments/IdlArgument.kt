package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlArgumentBuilder

class IdlArgument(builder: IdlArgumentBuilder): IdlFragment {
	val name = builder.name!!
	val types = builder.types.toSet().toTypedArray()
}