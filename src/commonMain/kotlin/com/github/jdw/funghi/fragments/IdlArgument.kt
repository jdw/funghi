package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlArgumentBuilder

class IdlArgument(builder: IdlArgumentBuilder): IdlFragment {
	val isOptional = builder.isOptional
	val name = builder.name!!
	val types = builder.types.toSet().toTypedArray()

	override fun toString(): String {
		val optional = if (isOptional) "optional " else ""

		return "${optional}${types[0]} $name"
	}
}