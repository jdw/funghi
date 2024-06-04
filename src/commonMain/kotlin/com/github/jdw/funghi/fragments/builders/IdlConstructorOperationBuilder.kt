package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.fragments.IdlExtendedAttribute
import echt
import popIfFirstStartsWith
import toPieces

class IdlConstructorOperationBuilder: IdlMemberBuilder(), IdlFragmentBuilder {
	var extendedAttributes: MutableList<IdlExtendedAttribute> = mutableListOf()


	override fun supportedKeywords(): Set<String> {
		TODO("Not yet implemented")
	}


	override fun parseLine(line: String) {
		val pieces = line.toPieces()

		val (found, extendedAttributeRaw) = pieces.popIfFirstStartsWith("[")
		found.echt {
			IdlExtendedAttributeBuilder()
				.apply { parseLine(extendedAttributeRaw) }
				.apply {  }
		}

	}


	override fun lineIsRelevant(line: String): Boolean {
		TODO("Not yet implemented")
	}
}