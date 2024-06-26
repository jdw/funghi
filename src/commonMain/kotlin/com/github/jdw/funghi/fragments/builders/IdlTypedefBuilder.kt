package com.github.jdw.funghi.fragments.builders

import Glob
import com.github.jdw.funghi.fragments.IdlType
import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.Scope

class IdlTypedefBuilder: IdlFragmentBuilder() {
	val types = mutableListOf<IdlType>()
	var identifier: String? = null


	override fun puzzle(pieces: Pieces) {
		//TODO The Type must not be the identifier of the same or another typedef.

		pieces popStartScope Scope.TYPEDEF

		if (pieces.peekIsSingleType()) {
			types += IdlType(IdlTypeBuilder().apply { thus puzzle pieces })
		}
		else {
			pieces pop "("

			var weHaveAnotherType = true
			val unionTypes = mutableListOf<IdlTypeBuilder>()

			while (weHaveAnotherType) {
				unionTypes += IdlTypeBuilder().apply { thus puzzle pieces }
				weHaveAnotherType = pieces popIfPresent Scope.UNION_TYPE.nextScopeKeyword()
			}

			pieces pop ")"

			if (pieces popIfPresent "?") unionTypes.forEach { it.isNullable = true }

			unionTypes.forEach { types += IdlType(it) }
		}

		identifier = pieces pop Glob.parserSettings!!.identifierRegex()

		pieces popEndScope Scope.TYPEDEF
	}
}