package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlConstantAttributeBuilder

class IdlConstantAttribute(builder: IdlConstantAttributeBuilder): IdlFragment {
	val identifier = builder.identifier!!
	val type = builder.type!!
	val value = builder.value!!

	override fun toString(): String = "const $type $identifier = $value;"
}