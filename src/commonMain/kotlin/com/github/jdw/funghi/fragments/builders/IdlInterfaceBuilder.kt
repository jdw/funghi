package com.github.jdw.funghi.fragments.builders

import Glob
import com.github.jdw.funghi.fragments.IdlAttribute
import com.github.jdw.funghi.fragments.IdlExtendedAttribute
import com.github.jdw.funghi.fragments.IdlOperation
import com.github.jdw.funghi.fragments.IdlOperationConstructor
import com.github.jdw.funghi.pieces.Scope.ATTRIBUTE
import com.github.jdw.funghi.pieces.Scope.INTERFACE
import com.github.jdw.funghi.pieces.Scope.OPERATION
import com.github.jdw.funghi.pieces.Scope.OPERATION_CONSTRUCTOR
import com.github.jdw.funghi.pieces.Pieces
import throws

class IdlInterfaceBuilder(extendAttributes: List<IdlExtendedAttribute>) : IdlFragmentBuilder() {
	var isMixin = false
	var isPartial = false
	var superTypes: MutableList<String>? = null
	var name: String? = null
	val operationConstructors: MutableSet<IdlOperationConstructor> = mutableSetOf()
	val operations: MutableSet<IdlOperation> = mutableSetOf() //TODO Test uniqueness of names
	val attributes: MutableSet<IdlAttribute> = mutableSetOf() //TODO Test uniqueness of names
	val extendedAttributes = extendAttributes.toMutableList()

	override fun puzzle(pieces: Pieces) {
		pieces popStartScope INTERFACE
		isPartial = pieces popIfPresent "partial"
		pieces pop "interface"
		isMixin = pieces popIfPresent "mixin"
		name = pieces pop Glob.parserSettings!!.complexTypesRegex()
		pieces pop "{"

		while (pieces.peekIsStartScope()) {
			when (pieces.peekStartScope()) {
				OPERATION_CONSTRUCTOR -> operationConstructors += IdlOperationConstructor(IdlOperationConstructorBuilder().apply { thus puzzle pieces })
				ATTRIBUTE -> attributes += IdlAttribute(IdlAttributeBuilder().apply { thus puzzle pieces })
				OPERATION -> operations += IdlOperation(IdlOperationBuilder().apply { thus puzzle pieces })
				else -> thus throwing objection
			}
		}

		//TODO pieces pop "};"?

		pieces popEndScope INTERFACE
	}


	fun fuse(other: IdlInterfaceBuilder) {
		if (isMixin != other.isMixin) throws()
		if (!other.isPartial) throws()
		if (name != other.name) throws("this.name" to name!!
			, "other.name" to other.name!!)

		extendedAttributes.addAll(other.extendedAttributes)
		operations.addAll(other.operations)
		operationConstructors.addAll(other.operationConstructors)
		attributes.addAll(other.attributes)
	}
}