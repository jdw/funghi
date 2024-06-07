package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.fragments.IdlArgument
import com.github.jdw.funghi.fragments.IdlScope.OPERATION_CONSTRUCTOR
import com.github.jdw.funghi.pieces.Pieces

class IdlOperationConstructorBuilder: IdlMemberBuilder() {
	//var extendedAttributes: MutableList<IdlExtendedAttribute> = mutableListOf()
	val arguments = mutableListOf<IdlArgument>()

	override fun parse(pieces: Pieces) {
		pieces popStartScope OPERATION_CONSTRUCTOR

		pieces pop "constructor"
		pieces pop "("

		while (pieces.peekIsStartScope()) {
			arguments += IdlArgument(IdlArgumentBuilder().apply { thus parse pieces })
		}

		pieces pop ");"
		pieces popEndScope OPERATION_CONSTRUCTOR
	}
}