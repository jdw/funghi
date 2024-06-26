package com.github.jdw.funghi.fragments.builders

import Glob
import com.github.jdw.funghi.fragments.IdlType
import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.Scope.ATTRIBUTE
import com.github.jdw.funghi.pieces.Scope.UNION_TYPE

class IdlAttributeBuilder: IdlMemberBuilder() {
	var extendedAttributes: MutableList<IdlExtendedAttributeBuilder>? = null
	var isReadonly = false
	var isUnrestricted = false
	val types = mutableListOf<IdlType>()
	var name: String? = null


	override fun puzzle(pieces: Pieces) {
		pieces pop ATTRIBUTE.startScopeKeyword()
		isReadonly = pieces popIfPresent "readonly"
		pieces pop "attribute"
		isUnrestricted = pieces popIfPresent "unrestricted"

		if (pieces.peekIsSingleType()) {
			types += IdlType(IdlTypeBuilder().apply { thus puzzle pieces })
		}
		else {
			pieces pop UNION_TYPE.startScopeKeyword()

			var weHaveAnotherType = true
			val unionTypes = mutableListOf<IdlTypeBuilder>()

			while (weHaveAnotherType) {
				unionTypes += IdlTypeBuilder().apply { thus puzzle pieces }
				weHaveAnotherType = pieces popIfPresent UNION_TYPE.nextScopeKeyword()
			}

			pieces pop UNION_TYPE.stopScopeKeyword()

			if (pieces popIfPresent "?") unionTypes.forEach { it.isNullable = true }

			unionTypes.forEach { types += IdlType(it) }
		}

		name = pieces pop Glob.parserSettings!!.identifierRegex()

		pieces pop ATTRIBUTE.stopScopeKeyword()
	}
}