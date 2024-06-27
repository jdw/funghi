package com.github.jdw.funghi.fragments.builders

import Glob
import com.github.jdw.funghi.fragments.IdlDictionaryMember
import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.Scope.DICTIONARY
import com.github.jdw.funghi.pieces.Scope.MEMBER

class IdlDictionaryBuilder: IdlFragmentBuilder() {
	var identifier: String? = null
	var superTypes = mutableSetOf<String>()
	val dictionaryMembers = mutableSetOf<IdlDictionaryMember>()


	override fun puzzle(pieces: Pieces) {
		pieces pop DICTIONARY.startScopeKeyword()

		pieces pop "dictionary"
		identifier = pieces pop Glob.parserSettings!!.identifierRegex()

		if (pieces popIfPresent ":") { // We have inheritance //TODO Can dictionaries inherit multiple supertypes per inheritance level?
			superTypes += pieces pop 1
		}

		pieces pop "{"

		if (pieces.peekStartScope() == MEMBER) { //TODO Can dictionaries be empty?
			var weHaveAnotherMember = true

			while (weHaveAnotherMember) {
				dictionaryMembers += IdlDictionaryMember(IdlDictionaryMemberBuilder().apply { thus puzzle pieces })

				weHaveAnotherMember = pieces.peekStartScope() == MEMBER
			}
		}


		pieces pop DICTIONARY.stopScopeKeyword()
	}

}