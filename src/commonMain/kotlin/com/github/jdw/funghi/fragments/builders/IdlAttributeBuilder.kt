package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.fragments.IdlScope.ATTRIBUTE
import com.github.jdw.funghi.fragments.IdlType
import com.github.jdw.funghi.pieces.Pieces
import doch
import echt

class IdlAttributeBuilder: IdlMemberBuilder() {
	var extendedAttributes: MutableList<IdlExtendedAttributeBuilder>? = null
	var isReadonly = false
	var isUnrestricted = false
	val types = mutableListOf<IdlType>()

	override fun parse(pieces: Pieces) {
		pieces popStartScope ATTRIBUTE
		isReadonly = pieces popIfPresent "readonly"
		isUnrestricted = pieces popIfPresent "unrestricted"

		pieces.peekIsSingleType()
			.echt {
				types += IdlType(IdlTypeBuilder().apply { thus parse pieces })
			}
			.doch {

			}

		pieces popEndScope ATTRIBUTE
	}
}