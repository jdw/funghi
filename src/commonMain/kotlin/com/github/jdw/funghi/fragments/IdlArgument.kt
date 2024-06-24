package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlArgumentBuilder

class IdlArgument(builder: IdlArgumentBuilder): IdlFragment {
	val isOptional = builder.isOptional
	val name = builder.name!!
	val types = builder.types.toSet().toTypedArray()

	override fun toString(): String {
		val optional = if (isOptional) "optional " else ""

		return if (types.size == 1) "${optional}${types[0]} $name"
			else if (types.all { it.isNullable }) {
				val unionTypes = types
					.joinToString(prefix = "(", separator = " or ", postfix = ")")
					.replace("?", "")
				"${optional}$unionTypes? $name"
			}
			else "${optional}${types.joinToString(prefix = "(", separator = " or ", postfix = ")")} $name"
	}
}