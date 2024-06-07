package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.fragments.IdlArgument
import com.github.jdw.funghi.pieces.Scope.OPERATION_CONSTRUCTOR
import com.github.jdw.funghi.pieces.Pieces

class IdlOperationConstructorBuilder: IdlMemberBuilder() {
	//var extendedAttributes: MutableList<IdlExtendedAttribute> = mutableListOf()
	val arguments = mutableListOf<IdlArgument>()

	override fun puzzle(pieces: Pieces) {
		pieces popStartScope OPERATION_CONSTRUCTOR

		pieces pop "constructor"
		pieces pop "("

		while (pieces.peekIsStartScope()) {
			arguments += IdlArgument(IdlArgumentBuilder().apply { thus puzzle pieces })
			pieces popIfPresent ","
		}

		pieces pop ");"

		pieces popEndScope OPERATION_CONSTRUCTOR
	}
}