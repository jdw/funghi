package com.github.jdw.funghi.fragments.builders

import Glob
import com.github.jdw.funghi.fragments.IdlArgument
import com.github.jdw.funghi.fragments.IdlType
import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.Scope.ARGUMENT
import com.github.jdw.funghi.pieces.Scope.ARGUMENTS
import com.github.jdw.funghi.pieces.Scope.OPERATION
import com.github.jdw.funghi.pieces.Scope.UNION_TYPE
import doch
import throws


class IdlOperationBuilder: IdlMemberBuilder() {
	var isVoid: Boolean = false
	var isUndefined: Boolean = false
	var name: String? = null
	val returnTypes = mutableListOf<IdlType>()
	val arguments = mutableListOf<IdlArgument>()
	var isGetter = false
	var isSetter = false
	var isDeleter = false


	override fun puzzle(pieces: Pieces) {
		pieces pop OPERATION.startScopeKeyword()

		isGetter = pieces popIfPresent "getter"
		isSetter = pieces popIfPresent "setter"
		isDeleter = pieces popIfPresent "deleter"

		if (isGetter && (isSetter || isDeleter)) throws()
		if (isSetter && (isGetter || isDeleter)) throws()
		if (isDeleter && (isGetter || isSetter)) throws()

		isVoid = pieces popIfPresent "void"
		isUndefined = pieces popIfPresent "undefined"

		//if (isGetter && (isVoid || isUndefined)) throws() //TODO Find out

		if (!(isVoid || isUndefined)) {
			if (pieces.peekIsSingleType()) {
				returnTypes += IdlType(IdlTypeBuilder().apply { thus puzzle pieces })
			}
			else {
				val unionTypes = mutableListOf<IdlTypeBuilder>()
				var weHaveAnotherUnionType = pieces popIfPresent UNION_TYPE.startScopeKeyword()
				weHaveAnotherUnionType doch { throws() }

				while (weHaveAnotherUnionType) {
					unionTypes += IdlTypeBuilder().apply { thus puzzle pieces }
					weHaveAnotherUnionType = pieces popIfPresent UNION_TYPE.nextScopeKeyword()
				}

				pieces pop UNION_TYPE.stopScopeKeyword()

				if (pieces popIfPresent "?") {
					unionTypes.forEach { it.isNullable = true }
				}

				unionTypes.forEach { returnTypes += IdlType(it) }
			}
		}

		name = pieces pop Glob.parserSettings!!.identifierRegex()

		pieces pop ARGUMENTS.startScopeKeyword()

		var weHaveAnotherArgument = pieces popIfPresent ARGUMENT.startScopeKeyword()
		while (weHaveAnotherArgument) {
			arguments += IdlArgument(IdlArgumentBuilder().apply { thus puzzle pieces })
			weHaveAnotherArgument = pieces popIfPresent ARGUMENT.nextScopeKeyword()
		}

		pieces popIfPresent ARGUMENT.stopScopeKeyword()
		pieces pop ARGUMENTS.stopScopeKeyword()

		pieces pop OPERATION.stopScopeKeyword()
	}

//	infix fun apply(block: () -> Unit): IdlOperationBuilder {
//		this.apply(block)
//
//		return this
//	}
}