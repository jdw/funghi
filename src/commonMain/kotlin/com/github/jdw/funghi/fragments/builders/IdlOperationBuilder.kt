package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.pieces.Pieces

class IdlOperationBuilder: IdlMemberBuilder(), IdlFragmentBuilder {
	var extendedAttributesBuilder: IdlExtendedAttributeBuilder? = null
	var isVoid: Boolean = false
	var isUndefined: Boolean = false


	override fun parse(pieces: Pieces) {
		TODO("Not yet implemented")
	}
}