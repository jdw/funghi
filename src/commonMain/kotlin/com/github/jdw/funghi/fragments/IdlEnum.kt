package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlEnumBuilder

class IdlEnum(builder: IdlEnumBuilder): IdlFragment {
	val name = builder.name!!
	val values = builder.values.toSet()


	override fun toString(): String {
		return "enum $name ${values.joinToString(prefix = "{ \"", separator = "\", \"", postfix = "\" };")}"
	}
}