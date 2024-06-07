package com.github.jdw.funghi.fragments.builders

import Glob
import com.github.jdw.funghi.fragments.IdlAttribute
import com.github.jdw.funghi.fragments.IdlExtendedAttribute
import com.github.jdw.funghi.fragments.IdlOperation
import com.github.jdw.funghi.fragments.IdlOperationConstructor
import com.github.jdw.funghi.fragments.IdlScope.ATTRIBUTE
import com.github.jdw.funghi.fragments.IdlScope.INTERFACE
import com.github.jdw.funghi.fragments.IdlScope.OPERATION
import com.github.jdw.funghi.fragments.IdlScope.OPERATION_CONSTRUCTOR
import com.github.jdw.funghi.pieces.Pieces
import throws

class IdlInterfaceBuilder() : IdlFragmentBuilder() {
	var isMixin = false
	var isPartial = false
	var superTypes: MutableList<String>? = null
	var name: String? = null
	val operationConstructors: MutableSet<IdlOperationConstructor> = mutableSetOf()
	val operations: MutableSet<IdlOperation> = mutableSetOf() //TODO Test uniqueness of names
	val attributes: MutableSet<IdlAttribute> = mutableSetOf() //TODO Test uniqueness of names
	var extendedAttributes = mutableListOf<IdlExtendedAttribute>()

	override fun parse(pieces: Pieces) {
		pieces popStartScope INTERFACE
		isPartial = pieces popIfPresent "partial"
		pieces pop "interface"
		isMixin = pieces popIfPresent "mixin"
		name = pieces pop Glob.parserSettings!!.complexTypesRegex()
		pieces pop "{"

		while (pieces.peekIsStartScope()) {
			when (pieces.peekStartScope()) {
				OPERATION_CONSTRUCTOR -> operationConstructors += IdlOperationConstructor(IdlOperationConstructorBuilder().apply { thus parse pieces })
				ATTRIBUTE -> attributes += IdlAttribute(IdlAttributeBuilder().apply { thus parse pieces })
				OPERATION -> operations += IdlOperation(IdlOperationBuilder() apply { thus parse pieces })
				else -> thus throwing objection
			}
		}

		//TODO pieces pop "};"?

		pieces popEndScope INTERFACE
	}


	fun fuse(other: IdlInterfaceBuilder) {
		if (isMixin != other.isMixin) throws()
		if (isPartial != other.isPartial && isPartial) throws()
		if (name != other.name) throws("this.name" to name!!
			, "other.name" to other.name!!)

		extendedAttributes.addAll(other.extendedAttributes)
		operations.addAll(other.operations)
		operationConstructors.addAll(other.operationConstructors)
		attributes.addAll(other.attributes)
	}
}