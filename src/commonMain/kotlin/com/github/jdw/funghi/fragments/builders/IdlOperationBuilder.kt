package com.github.jdw.funghi.fragments.builders

import echt
import popIfFirstStartsWith
import popIfPresent
import toPieces

class IdlOperationBuilder: IdlMemberBuilder(), IdlFragmentBuilder {
	var extendedAttributesBuilder: IdlExtendedAttributeBuilder? = null
	var isVoid: Boolean = false
	var isUndefined: Boolean = false

	override fun supportedKeywords(): Set<String> {
		TODO("Not yet implemented")
	}

	override fun parseLine(line: String) {
		val pieces = line.toPieces()

		val (found, extendedAttributeRaw) = pieces.popIfFirstStartsWith("[")
		found.echt {
			IdlExtendedAttributeBuilder()
				.apply { parseLine(extendedAttributeRaw) }
				.apply { extendedAttributesBuilder = this }
		}

		isVoid = pieces.popIfPresent("void")
		isUndefined = pieces.popIfPresent("undefined")

	}

	override fun lineIsRelevant(line: String): Boolean {
		TODO("Not yet implemented")
	}
}