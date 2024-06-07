package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.fragments.IdlScope.ARGUMENT
import com.github.jdw.funghi.fragments.IdlType
import com.github.jdw.funghi.pieces.Pieces

class IdlArgumentBuilder: IdlFragmentBuilder() {
	var name: String? = null
	val types = mutableListOf<IdlType>()
	var isOptional = false

	override fun parse(pieces: Pieces) {
		pieces popStartScope ARGUMENT

		isOptional = pieces popIfPresent "optional"

		types += IdlType(IdlTypeBuilder().apply { thus parse pieces })

		name = ""//pieces pop Glob.parserSettings!!.identifierRegex()
		pieces pop 1

		pieces popEndScope ARGUMENT
	}
}