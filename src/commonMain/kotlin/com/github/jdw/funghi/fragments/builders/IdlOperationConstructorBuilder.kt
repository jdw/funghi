package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.fragments.IdlArgument
import com.github.jdw.funghi.pieces.Scope.OPERATION_CONSTRUCTOR
import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.Scope

class IdlOperationConstructorBuilder: IdlMemberBuilder() {
	//var extendedAttributes: MutableList<IdlExtendedAttribute> = mutableListOf()
	val arguments = mutableListOf<IdlArgument>()

	override fun puzzle(pieces: Pieces) {
		pieces popStartScope OPERATION_CONSTRUCTOR

		pieces pop "constructor"
		pieces popStartScope  Scope.ARGUMENTS

		var weHaveAnotherArgument = pieces popIfPresentStartScope Scope.ARGUMENT
		while (weHaveAnotherArgument) {
			arguments += IdlArgument(IdlArgumentBuilder().apply { thus puzzle pieces })
			weHaveAnotherArgument = pieces popIfPresentNextScope Scope.ARGUMENT
		}

		pieces popIfPresentEndScope Scope.ARGUMENT
		pieces popEndScope Scope.ARGUMENTS

		pieces popEndScope OPERATION_CONSTRUCTOR
	}
}