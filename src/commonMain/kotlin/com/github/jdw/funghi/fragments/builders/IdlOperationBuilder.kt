package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.pieces.Scope.OPERATION
import com.github.jdw.funghi.fragments.IdlType
import com.github.jdw.funghi.pieces.Pieces

class IdlOperationBuilder: IdlMemberBuilder() {
	var isVoid: Boolean = false
	var isUndefined: Boolean = false
	var name: String? = null
	val returnTypes = mutableListOf<IdlType>()
	override fun puzzle(pieces: Pieces) {
		pieces popStartScope OPERATION

		isVoid = pieces popIfPresent "void"
		isUndefined = pieces popIfPresent "undefined"

		if (!(isVoid || isUndefined)) {
			if (pieces.peekIsSingleType()) {
				returnTypes += IdlType(IdlTypeBuilder().apply { thus puzzle pieces })
			}
			else {
				//TODO multi-type type val value = pieces.pop
			}
		}

		pieces popEndScope OPERATION
	}

	infix fun apply(block: () -> Unit): IdlOperationBuilder {
		this.apply(block)

		return this
	}
}