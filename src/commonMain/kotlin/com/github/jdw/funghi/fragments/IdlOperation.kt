package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlOperationBuilder

class IdlOperation(builder: IdlOperationBuilder): IdlMember() {
	val name = builder.name!!
	//val extendedAttributes: Set<IdlExtendedAttribute>?

	init {

	}
}