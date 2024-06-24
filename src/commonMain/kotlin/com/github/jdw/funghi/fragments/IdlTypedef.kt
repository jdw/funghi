package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlTypedefBuilder

class IdlTypedef(builder: IdlTypedefBuilder): IdlFragment {
	val types = builder.types.toList()
	val identifier = builder.identifier!!


	override fun toString(): String {
		return if (types.size == 1) "typedef ${types[0]} $identifier;"
			else if (types.all { it.isNullable }) {
				val typesStr = types
					.joinToString(prefix = "(", separator = " or ", postfix = ")")
					.replace("?", "")
				"typedef $typesStr? $identifier;"
			}
			else {
				"typedef ${types.joinToString(prefix = "(", separator = " or ", postfix = ")")} $identifier;"
			}
	}
}