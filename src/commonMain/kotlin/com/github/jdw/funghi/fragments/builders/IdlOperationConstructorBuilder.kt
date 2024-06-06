package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.fragments.IdlExtendedAttribute
import com.github.jdw.funghi.fragments.IdlScope.OPERATION_CONSTRUCTOR
import com.github.jdw.funghi.pieces.Pieces

class IdlOperationConstructorBuilder: IdlMemberBuilder() {
	//var extendedAttributes: MutableList<IdlExtendedAttribute> = mutableListOf()


	override fun parse(pieces: Pieces) {
		pieces popStartScope OPERATION_CONSTRUCTOR

		if (pieces peek "constructor();") {
			pieces pop 1
		}
		else if (pieces peek Glob.parserSettings!!.operationRegex()) {
			val value = pieces pop 1
		}
		else thus throwing objection

		pieces popEndScope OPERATION_CONSTRUCTOR
	}
}