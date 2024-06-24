package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.Scope
import throws

class IdlEnumBuilder: IdlFragmentBuilder() {
	var name: String? = null
	val values = mutableSetOf<String>()

	override fun puzzle(pieces: Pieces) {
		pieces popStartScope Scope.ENUM

		name = pieces pop Glob.parserSettings!!.identifierRegex()

		pieces pop "{"

		var weHaveAnotherValue = true

		while (weHaveAnotherValue) {
			val size = values.size

			values += pieces.pop(1).replace("\"", "")

			if (size == values.size) throws() // Duplicate found
			weHaveAnotherValue = pieces popIfPresent ","
		}

		pieces popEndScope Scope.ENUM
	}

}