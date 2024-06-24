package com.github.jdw.funghi.fragments.builders

import Glob
import com.github.jdw.funghi.fragments.IdlType
import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.Scope
import noop

class IdlTypedefBuilder: IdlFragmentBuilder() {
	var type: IdlType? = null
	var identifier: String? = null


	override fun puzzle(pieces: Pieces) {
		//TODO The Type must not be the identifier of the same or another typedef.

		pieces popStartScope Scope.TYPEDEF

		if (pieces.peekIsSingleType()) {
			type = IdlType(IdlTypeBuilder().apply { thus puzzle pieces })
		}
		else {
			noop()
		}

		identifier = pieces pop Glob.parserSettings!!.identifierRegex()

		pieces popEndScope Scope.TYPEDEF
	}
}