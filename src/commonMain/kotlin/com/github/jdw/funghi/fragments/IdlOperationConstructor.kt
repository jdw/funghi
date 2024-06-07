package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlOperationConstructorBuilder

class IdlOperationConstructor(builder: IdlOperationConstructorBuilder): IdlMember() {
	val arguments = builder.arguments.toSet().toTypedArray()

	override fun toString(): String = "constructor(${arguments.joinToString(", ")});"
}