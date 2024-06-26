package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.Scope.SEQUENCE
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
		if (pieces popIfPresent SEQUENCE.startScopeKeyword()) {
			isSequence = true
			name = pieces.popType()
			pieces pop SEQUENCE.stopScopeKeyword()
		}
		else {
			name = pieces.popType()
		}

		name!!.contains("?")
			.echt {
				isNullable = true
				name = name!!.replace("?", "")
			}
	}
}