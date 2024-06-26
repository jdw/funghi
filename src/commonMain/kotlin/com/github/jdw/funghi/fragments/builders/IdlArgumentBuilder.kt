package com.github.jdw.funghi.fragments.builders

import Glob
import com.github.jdw.funghi.fragments.IdlType
import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.Scope.UNION_TYPE

class IdlArgumentBuilder: IdlFragmentBuilder() {
	var name: String? = null
	val types = mutableListOf<IdlType>()
	var isOptional = false

	override fun puzzle(pieces: Pieces) {
		isOptional = pieces popIfPresent "optional"

		var weHaveAnotherUnionType = pieces popIfPresent UNION_TYPE.startScopeKeyword()
		if (weHaveAnotherUnionType) {
			val unionTypes = mutableListOf<IdlTypeBuilder>()
			while (weHaveAnotherUnionType) {
				unionTypes += IdlTypeBuilder().apply { thus puzzle pieces }
				weHaveAnotherUnionType = pieces popIfPresent UNION_TYPE.nextScopeKeyword()
			}

			pieces pop UNION_TYPE.stopScopeKeyword()

			if (pieces popIfPresent "?") {
				unionTypes.forEach { it.isNullable = true }
			}

			unionTypes.forEach { types += IdlType(it) }
		}
		else {
			types += IdlType(IdlTypeBuilder().apply { thus puzzle pieces })
		}

		name = pieces pop Glob.parserSettings!!.identifierRegex()
	}
}