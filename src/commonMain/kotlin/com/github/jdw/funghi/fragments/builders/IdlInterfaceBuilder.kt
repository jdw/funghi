package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.pieces.Pieces

class IdlInterfaceBuilder() : IdlFragmentBuilder {
	var isMixin = false
	var isPartial = false
	var superTypes: MutableList<String>? = null
	var name: String? = null
	var members: MutableList<IdlMemberBuilder>? = null


	override fun parse(pieces: Pieces) {
		TODO("Not yet implemented")
	}
}