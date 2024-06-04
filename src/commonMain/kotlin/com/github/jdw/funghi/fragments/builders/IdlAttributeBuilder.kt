package com.github.jdw.funghi.fragments.builders

import echt
import popIfFirstStartsWith
import toPieces

class IdlAttributeBuilder: IdlMemberBuilder(), IdlFragmentBuilder {
	var extendedAttributes: MutableList<IdlExtendedAttributeBuilder>? = null

	override fun supportedKeywords(): Set<String> {
		TODO("Not yet implemented")
	}

	override fun parseLine(line: String) {
		val pieces = line.toPieces()
		val (found, result) = pieces.popIfFirstStartsWith("[")
		found.echt {
			extendedAttributes = mutableListOf()
			IdlExtendedAttributeBuilder()
				.apply { parseLine(result) }
				.apply { extendedAttributes!! += this }
		}
	}

	override fun lineIsRelevant(line: String): Boolean {
		TODO("Not yet implemented")
	}
}