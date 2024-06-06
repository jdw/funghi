package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.fragments.IdlType
import com.github.jdw.funghi.pieces.Pieces
import echt

class IdlAttributeBuilder: IdlMemberBuilder() {
	var extendedAttributes: MutableList<IdlExtendedAttributeBuilder>? = null
	var isReadonly = false
	var isUnrestricted = false
	val types = mutableListOf<IdlType>()

	override fun parse(pieces: Pieces) {
		isReadonly = pieces popIfPresent "readonly"
		isUnrestricted = pieces popIfPresent "unrestricted"
		(pieces.peekIsPresentSingleType()) echt { }


	}
}