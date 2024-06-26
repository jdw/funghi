package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.pieces.Pieces

class IdlDictionaryMemberDefaultValueBuilder: IdlFragmentBuilder() {
	var isSet: Boolean? = null
	var isEmptyDictionary = false
	var isEmptySequence = false
	var isEmptyString = false
	var isNull = false
	var isBoolean = false
	var value: String? = null


	override fun puzzle(pieces: Pieces) {
		isSet = if (pieces popIfPresent "=") {
			//isEmptyDictionary =
			isEmptyString = pieces popIfPresent "\"\""

			true
		}
		else {
			value = ""
			false
		}
	}

}