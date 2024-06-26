package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.fragments.IdlArgument
import com.github.jdw.funghi.fragments.IdlType
import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.Scope.ARGUMENT
import com.github.jdw.funghi.pieces.Scope.ARGUMENTS
import com.github.jdw.funghi.pieces.Scope.SPECIAL_OPERATION
import throws


class IdlSpecialOperationBuilder: IdlFragmentBuilder() {
	var isGetter = false
	var isSetter = false
	var isDeleter = false
	var returnType: IdlType? = null
	val arguments = mutableSetOf<IdlArgument>()


	override fun puzzle(pieces: Pieces) {
		pieces pop SPECIAL_OPERATION.startScopeKeyword()

		isGetter = pieces popIfPresent "getter"
		isSetter = pieces popIfPresent "setter"
		isDeleter = pieces popIfPresent "deleter"

		if (!(isGetter || isSetter || isDeleter)) throws()

		returnType = IdlType(IdlTypeBuilder().apply { thus puzzle pieces })

		pieces pop ARGUMENTS.startScopeKeyword()
		pieces pop ARGUMENT.startScopeKeyword()

		var weHaveAnotherArgument = true // SpecOps must have at least one arg
		while (weHaveAnotherArgument) {
			arguments += IdlArgument(IdlArgumentBuilder().apply { thus puzzle pieces })
			weHaveAnotherArgument = pieces popIfPresent ARGUMENT.nextScopeKeyword()
		}

		pieces pop ARGUMENT.stopScopeKeyword()
		pieces pop ARGUMENTS.stopScopeKeyword()
		pieces pop SPECIAL_OPERATION.stopScopeKeyword()
	}
}