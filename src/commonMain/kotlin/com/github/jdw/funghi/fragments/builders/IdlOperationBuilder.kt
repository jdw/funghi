package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.fragments.IdlArgument
import com.github.jdw.funghi.pieces.Scope.OPERATION
import com.github.jdw.funghi.fragments.IdlType
import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.Scope
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
		pieces popStartScope OPERATION

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
				var weHaveAnotherUnionType = pieces popIfPresentStartScope Scope.UNION_TYPE
				weHaveAnotherUnionType doch { throws() }

				while (weHaveAnotherUnionType) {
					unionTypes += IdlTypeBuilder().apply { thus puzzle pieces }
					weHaveAnotherUnionType = pieces popIfPresentNextScope Scope.UNION_TYPE
				}

				pieces popEndScope Scope.UNION_TYPE

				if (pieces popIfPresent "?") {
					unionTypes.forEach { it.isNullable = true }
				}

				unionTypes.forEach { returnTypes += IdlType(it) }
			}
		}

		name = pieces pop Glob.parserSettings!!.identifierRegex()

		pieces popStartScope Scope.ARGUMENTS

		var weHaveAnotherArgument = pieces popIfPresentStartScope Scope.ARGUMENT
		while (weHaveAnotherArgument) {
			arguments += IdlArgument(IdlArgumentBuilder().apply { thus puzzle pieces })
			weHaveAnotherArgument = pieces popIfPresentNextScope Scope.ARGUMENT
		}

		pieces popIfPresentEndScope Scope.ARGUMENT
		pieces popEndScope Scope.ARGUMENTS

		pieces popEndScope OPERATION
	}

//	infix fun apply(block: () -> Unit): IdlOperationBuilder {
//		this.apply(block)
//
//		return this
//	}
}