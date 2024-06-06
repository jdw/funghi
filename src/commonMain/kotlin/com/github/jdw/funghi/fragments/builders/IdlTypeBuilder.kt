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
	var type: String? = null
	var name: String? = null


	override infix fun parse(pieces: Pieces) {
		type = pieces.popSingleType()
		name = pieces pop Glob.parserSettings!!.identifierRegex()
	}
}