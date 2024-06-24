package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlTypedefBuilder

class IdlTypedef(builder: IdlTypedefBuilder): IdlFragment {
	val type = builder.type!!
	val identifier = builder.identifier!!


	override fun toString(): String {
		return "typedef $type $identifier;"
	}
}