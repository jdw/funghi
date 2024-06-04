package com.github.jdw.funghi.fragments.builders

class IdlExtendedAttributeBuilder: IdlFragmentBuilder {
	override fun supportedKeywords(): Set<String> {
		TODO("Not yet implemented")
	}

	override fun parseLine(line: String) {
		println("--- $line")
	}

	override fun lineIsRelevant(line: String): Boolean {
		TODO("Not yet implemented")
	}
}