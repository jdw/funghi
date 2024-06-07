package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.pieces.Pieces

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

	override infix fun puzzle(pieces: Pieces) {
		name = pieces.popSingleType()
	}
}