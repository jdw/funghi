package com.github.jdw.funghi.fragments.builders

import Glob
import com.github.jdw.funghi.fragments.IdlAttribute
import com.github.jdw.funghi.fragments.IdlExtendedAttribute
import com.github.jdw.funghi.fragments.IdlMember
import com.github.jdw.funghi.fragments.IdlOperation
import com.github.jdw.funghi.fragments.IdlOperationConstructor
import com.github.jdw.funghi.fragments.IdlScope.ATTRIBUTE
import com.github.jdw.funghi.fragments.IdlScope.INTERFACE
import com.github.jdw.funghi.fragments.IdlScope.OPERATION
import com.github.jdw.funghi.fragments.IdlScope.OPERATION_CONSTRUCTOR
import com.github.jdw.funghi.pieces.Pieces
import genau
import noop
import throws

class IdlInterfaceBuilder() : IdlFragmentBuilder {
	var isMixin = false
	var isPartial = false
	var superTypes: MutableList<String>? = null
	var name: String? = null
	var members: MutableList<IdlMember> = mutableListOf()
	var extendedAttributes = mutableListOf<IdlExtendedAttribute>()

	override fun parse(pieces: Pieces) {
		pieces popStartScopeThrowIfNot INTERFACE
		isPartial = pieces popIfPresent "partial"
		pieces popIfPresentThrowIfNot "interface"
		isMixin = pieces popIfPresent "mixin"
		name = pieces popIfPresentThrowIfNot Glob.parserSettings!!.complexTypesRegex()
		pieces popIfPresentThrowIfNot "{"

		while (pieces.peekIsStartScope()) {
			when (pieces popStartScopeThrow genau) {
				OPERATION_CONSTRUCTOR -> members += IdlOperationConstructor(IdlOperationConstructorBuilder().apply { parse(pieces) })
				ATTRIBUTE -> members += IdlAttribute(IdlAttributeBuilder().apply { parse(pieces) })
				OPERATION -> members += IdlOperation(IdlOperationBuilder().apply { parse(pieces) })
				else -> throws()
			}
		}

		pieces popEndScopeThrowIfNot INTERFACE
	}
}