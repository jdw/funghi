package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlAttributeBuilder

open class IdlAttribute(builder: IdlAttributeBuilder): IdlMember() {
	val isReadonly = builder.isReadonly
	val types = builder.types.toSet().toTypedArray() //TODO What does the standard say about multiple lines in an interface looking the same?
	val name = builder.name!!

	override fun toString(): String {
		val readonly = if (isReadonly) "readonly " else ""

		return if (types.size == 1) "${readonly}attribute ${types[0]} $name;"
			else if (types.all { it.isNullable }) {
				val typesStr = types
					.joinToString(prefix = "(", separator = " or ", postfix = ")")
					.replace("?", "")
				"${readonly}attribute $typesStr? $name;"
			}
			else "${readonly}attribute ${types.joinToString(prefix = "(", separator = " or ", postfix = ")")} $name;"
	}
}