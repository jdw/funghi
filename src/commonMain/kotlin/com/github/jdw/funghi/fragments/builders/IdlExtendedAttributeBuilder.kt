package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.pieces.Scope.EXTENDED_ATTRIBUTE
import com.github.jdw.funghi.pieces.Pieces

class IdlExtendedAttributeBuilder: IdlFragmentBuilder() {
	override fun puzzle(pieces: Pieces) {
		pieces popStartScope EXTENDED_ATTRIBUTE

		var value = ""
		while (!pieces.peekEndScope()) {
			value += pieces.pop()
		}

		pieces popEndScope EXTENDED_ATTRIBUTE
	}

}