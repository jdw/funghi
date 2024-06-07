package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlAttributeBuilder

open class IdlAttribute(builder: IdlAttributeBuilder): IdlMember() {
	val isReadonly = builder.isReadonly
	val types = builder.types.toSet().toTypedArray() //TODO What does the standard say about multiple lines in an interface looking the same?
	val name = builder.name!!

	override fun toString(): String {
		val readonly = if (isReadonly) "readonly " else ""

		return "${readonly}attribute ${types.joinToString(", ")} $name;"
	}
}