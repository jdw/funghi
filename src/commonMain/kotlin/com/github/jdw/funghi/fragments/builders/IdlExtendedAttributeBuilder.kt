package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.fragments.IdlScope.EXTENDED_ATTRIBUTE
import com.github.jdw.funghi.pieces.Pieces

class IdlExtendedAttributeBuilder: IdlFragmentBuilder {
	override fun parse(pieces: Pieces) {
		pieces popStartScopeThrowIfNot EXTENDED_ATTRIBUTE

		var value = ""
		while (!pieces.peekEndScope()) {
			value += pieces.pop()
		}

		pieces popEndScopeThrowIfNot EXTENDED_ATTRIBUTE
	}

}