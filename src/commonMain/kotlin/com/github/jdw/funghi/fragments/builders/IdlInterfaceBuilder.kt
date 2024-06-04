package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.model.builders.IdlModelBuilder
import doch
import echt
import popIfPresent
import throws

class IdlInterfaceBuilder() : IdlFragmentBuilder {
	var isMixin = false
	var isPartial = false
	var superTypes: MutableList<String>? = null
	var name: String? = null

	override fun supportedKeywords(): Set<String> = Glob.currentParserSettings!!.interfaceKeywords()


	override fun parseLine(line: String) {

	}


	fun parseAttributeLine(line: String) {

	}


	override fun lineIsRelevant(line: String): Boolean {
		TODO("Not yet implemented")
	}


	companion object {
		fun parseDefiningLines(attributeLine: String, line: String, builder: IdlModelBuilder): IdlInterfaceBuilder {
			val pieces = line.split(" ").toMutableList()

			val isPartial = pieces.popIfPresent("partial")

			pieces.popIfPresent("interface").doch { throws() }

			val isMixin = pieces.popIfPresent("mixin")

			var ret = IdlInterfaceBuilder()
			if (Glob.currentParserSettings!!.complexTypesRegex().matches(pieces.first())) {
				if (!Glob.currentParserSettings?.complexTypesNameIsProhibited(pieces.first())!!) {
					val name = pieces.removeFirst()
					ret.isMixin = isMixin
					ret.name = name
					ret.isPartial = isPartial

					if (isPartial && builder.partialInterfaces.containsKey(name)) {
						ret = builder.partialInterfaces[name]
							?: throws()

						if (ret.isMixin != isMixin) throws()

						return ret
					}
				}
				else {
					throws()
				}
			}
			else {
				throws()
			}

			//TODO Can partial interfaces have different (extended) attributes?
			if ("" != attributeLine) ret.parseAttributeLine(attributeLine)
			//TODO Can partial interfaces have different inheritance?
			pieces.popIfPresent(":").echt { ret.superTypes = mutableListOf() }

			if (null != ret.superTypes) {
				while (Glob.currentParserSettings!!.complexTypesRegex().matches(pieces.first())) {
					ret.superTypes!! += pieces.first()
					pieces.removeFirst()
				}
			}

			if ("{" == pieces.first()) {
				pieces.removeFirst()
			}
			else {
				throws()
			}

			if (pieces.isNotEmpty()) throws()

			return ret
		}
	}
}