package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlOperationBuilder

class IdlOperation(builder: IdlOperationBuilder): IdlMember() {
	val name = builder.name!!
	//val extendedAttributes: Set<IdlExtendedAttribute>?
	val isVoid = builder.isVoid
	val isUndefined = builder.isUndefined
	val returnTypes = builder.returnTypes

	override fun toString(): String {
		val void = if (isVoid) "void" else ""
		val undefined = if (isUndefined) "undefined" else ""
		val returns =
			if (returnTypes.size == 1) "${returnTypes[0]}"
			else if (returnTypes.isEmpty()) ""
			else returnTypes.joinToString(",", "(", ")")

		return "$void$undefined$returns $name();"
	}
}