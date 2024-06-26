package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.fragments.IdlDictionaryMemberDefaultValue
import com.github.jdw.funghi.fragments.IdlType
import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.Scope.DICTIONARY_MEMBER

class IdlDictionaryMemberBuilder: IdlFragmentBuilder() {
	//TODO Extended attributes
	var type: IdlType? = null //TODO Can members have union types?
	var identifier: String? = null
	var defaultValue: IdlDictionaryMemberDefaultValue? = null // Heads up! The empty string is a valid default value


	override fun puzzle(pieces: Pieces) {
		pieces pop DICTIONARY_MEMBER.startScopeKeyword()

		type = IdlType(IdlTypeBuilder().apply { thus puzzle pieces })
		identifier = pieces pop 1
		defaultValue = IdlDictionaryMemberDefaultValue(IdlDictionaryMemberDefaultValueBuilder().apply { thus puzzle pieces })

		pieces pop DICTIONARY_MEMBER.stopScopeKeyword()
	}
}