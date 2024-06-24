package com.github.jdw.funghi.fragments.builders

import Glob
import com.github.jdw.funghi.fragments.IdlType
import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.Scope
import com.github.jdw.funghi.pieces.Scope.ATTRIBUTE

class IdlAttributeBuilder: IdlMemberBuilder() {
	var extendedAttributes: MutableList<IdlExtendedAttributeBuilder>? = null
	var isReadonly = false
	var isUnrestricted = false
	val types = mutableListOf<IdlType>()
	var name: String? = null

	override fun puzzle(pieces: Pieces) {
		pieces popStartScope ATTRIBUTE
		pieces pop "attribute"
		isReadonly = pieces popIfPresent "readonly"
		isUnrestricted = pieces popIfPresent "unrestricted"

		if (pieces.peekIsSingleType()) {
			types += IdlType(IdlTypeBuilder().apply { thus puzzle pieces })
		}
		else {
			pieces popStartScope Scope.UNION_TYPE

			var weHaveAnotherType = true
			val unionTypes = mutableListOf<IdlTypeBuilder>()

			while (weHaveAnotherType) {
				unionTypes += IdlTypeBuilder().apply { thus puzzle pieces }
				weHaveAnotherType = pieces popIfPresent Scope.UNION_TYPE.nextScopeKeyword()
			}

			pieces popEndScope Scope.UNION_TYPE

			if (pieces popIfPresent "?") unionTypes.forEach { it.isNullable = true }

			unionTypes.forEach { types += IdlType(it) }
		}

		name = pieces pop Glob.parserSettings!!.identifierRegex()

		pieces popEndScope ATTRIBUTE
	}
}