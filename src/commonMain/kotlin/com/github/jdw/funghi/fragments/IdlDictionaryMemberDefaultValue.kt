package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlDictionaryMemberDefaultValueBuilder
import throws

class IdlDictionaryMemberDefaultValue(builder: IdlDictionaryMemberDefaultValueBuilder): IdlFragment {
	var isSet = builder.isSet!!
	var isEmptyDictionary = builder.isEmptyDictionary
	var isEmptySequence = builder.isEmptySequence
	var isEmptyString = builder.isEmptyString
	var isNull = builder.isNull
	val isBoolean = builder.isBoolean
	val value = builder.value!!


	init {
		if (isSet) {
			var settingsThatIsSet = 0

			if (isEmptyDictionary) settingsThatIsSet++
			if (isEmptySequence)  settingsThatIsSet++
			if (isEmptyString) settingsThatIsSet++
			if (isNull) settingsThatIsSet++
			if (isBoolean) settingsThatIsSet++

			if (settingsThatIsSet > 1) throws()
		}
	}
}