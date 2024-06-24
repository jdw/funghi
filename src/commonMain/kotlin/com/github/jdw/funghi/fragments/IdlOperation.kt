package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlOperationBuilder

class IdlOperation(builder: IdlOperationBuilder): IdlMember() {
	val name = builder.name!!
	//val extendedAttributes: Set<IdlExtendedAttribute>?
	val isVoid = builder.isVoid
	val isUndefined = builder.isUndefined
	val returnTypes = builder.returnTypes
	val arguments = builder.arguments


	override fun toString(): String {
		val void = if (isVoid) "void" else ""
		val undefined = if (isUndefined) "undefined" else ""
		val returns =
			if (returnTypes.size == 1) "${returnTypes[0]}"
			else if (returnTypes.isEmpty()) ""
			else if (returnTypes.all { it.isNullable }) {
				val unionTypes = returnTypes
					.joinToString(" or ", "(", ")")
					.replace("?", "")

				"$unionTypes?"
			}
			else returnTypes.joinToString(" or ", "(", ")")

		return "$void$undefined$returns $name${arguments.joinToString(", ", "(", ")")};"
	}
}