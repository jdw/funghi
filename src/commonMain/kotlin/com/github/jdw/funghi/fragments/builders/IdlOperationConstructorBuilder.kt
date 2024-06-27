package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.fragments.IdlArgument
import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.Scope.ARGUMENT
import com.github.jdw.funghi.pieces.Scope.ARGUMENTS
import com.github.jdw.funghi.pieces.Scope.CONSTRUCTOR

class IdlOperationConstructorBuilder: IdlMemberBuilder() {
	//var extendedAttributes: MutableList<IdlExtendedAttribute> = mutableListOf()
	val arguments = mutableListOf<IdlArgument>()

	override fun puzzle(pieces: Pieces) {
		pieces pop CONSTRUCTOR.startScopeKeyword()

		pieces pop "constructor"
		pieces pop ARGUMENTS.startScopeKeyword()

		var weHaveAnotherArgument = pieces popIfPresent ARGUMENT.startScopeKeyword()
		while (weHaveAnotherArgument) {
			arguments += IdlArgument(IdlArgumentBuilder().apply { thus puzzle pieces })
			weHaveAnotherArgument = pieces popIfPresent ARGUMENT.nextScopeKeyword()
		}

		pieces popIfPresent ARGUMENT.stopScopeKeyword()
		pieces pop ARGUMENTS.stopScopeKeyword()

		pieces pop CONSTRUCTOR.stopScopeKeyword()
	}
}