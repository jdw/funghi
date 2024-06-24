package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.Scope
import echt

/**
 * This section lists the types supported by Web IDL, the set of values or Infra type corresponding
 * to each type, and how constants of that type are represented.
 *
 * See further documentation:
 * * [The WebIDL spec](https://webidl.spec.whatwg.org/#idl-types)
 */
class IdlTypeBuilder: IdlFragmentBuilder() {
	var name: String? = null
	var isArray = false
	var isNullable = false
	var isSequence = false


	override infix fun puzzle(pieces: Pieces) {
		if (pieces popIfPresentStartScope Scope.SEQUENCE) {
			isSequence = true
			name = pieces.popSingleType()
			pieces popEndScope Scope.SEQUENCE
		}
		else {
			name = pieces.popSingleType()
		}

		name!!.contains("?")
			.echt {
				isNullable = true
				name = name!!.replace("?", "")
			}
	}
}