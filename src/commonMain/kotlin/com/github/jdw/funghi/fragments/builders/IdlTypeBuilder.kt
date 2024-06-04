package com.github.jdw.funghi.fragments.builders

/**
 * This section lists the types supported by Web IDL, the set of values or Infra type corresponding
 * to each type, and how constants of that type are represented.
 *
 * See further documentation:
 * * [The WebIDL spec](https://webidl.spec.whatwg.org/#idl-types)
 */
class IdlTypeBuilder: IdlFragmentBuilder {
	override fun supportedKeywords(): Set<String> = setOf("any", )

	override fun parseLine(line: String) {
		TODO("Not yet implemented")
	}

	override fun lineIsRelevant(line: String): Boolean {
		TODO("Not yet implemented")
	}
}