package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlDictionaryMemberBuilder
import throws

class IdlDictionaryMember(builder: IdlDictionaryMemberBuilder): IdlFragment {
	val type = builder.type!!
	val identifier = builder.identifier!!
	val defaultValue = builder.defaultValue!!

	init {
		if (!identifier.matches(Glob.parserSettings!!.identifierRegex())) throws() //TODO Make IdlIdentifierBuilder...
	}
}