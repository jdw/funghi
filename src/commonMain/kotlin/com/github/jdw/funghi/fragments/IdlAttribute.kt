package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlAttributeBuilder

open class IdlAttribute(builder: IdlAttributeBuilder): IdlMember() {
	val isReadonly = builder.isReadonly
	val isUnrestricted = builder.isUnrestricted
	val types = builder.types.toSet().toTypedArray()
}