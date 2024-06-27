package com.github.jdw.funghi.fragments.builders

import Glob
import com.github.jdw.funghi.fragments.IdlArgument
import com.github.jdw.funghi.fragments.IdlExtendedAttribute
import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.Scope.ARGUMENT
import com.github.jdw.funghi.pieces.Scope.EXTENDED_ATTRIBUTE
import com.github.jdw.funghi.pieces.Scope.LIST

class IdlExtendedAttributeBuilder: IdlFragmentBuilder() {
	var name: String? = null
	val identifiers = mutableListOf<String>()
	val arguments = mutableListOf<IdlArgument>()
	var type: IdlExtendedAttribute.Type? = null

	override fun puzzle(pieces: Pieces) {
		name = pieces pop Glob.parserSettings!!.identifierRegex()
		type = IdlExtendedAttribute.Type.NO_ARGS

		if (pieces popIfPresent "=") {
			if (pieces popIfPresent LIST.startScopeKeyword()) {
				type = IdlExtendedAttribute.Type.IDENT_LIST
				var weHaveAnotherIdentifier = true
				while (weHaveAnotherIdentifier) {
					identifiers += pieces pop Glob.parserSettings!!.identifierRegex()
					weHaveAnotherIdentifier = pieces popIfPresent ","
				}

				pieces pop LIST.stopScopeKeyword()
			}
			else if (pieces peek Glob.parserSettings!!.identifierRegex()) {
				identifiers += pieces pop Glob.parserSettings!!.identifierRegex()

				if (pieces peek "(") { // Start of operation
					type = IdlExtendedAttribute.Type.NAMED_ARG_LIST
					pieces pop "("

					var weHaveAnotherArgument = pieces popIfPresent ARGUMENT.startScopeKeyword()
					while (weHaveAnotherArgument) {
						arguments += IdlArgument(IdlArgumentBuilder().apply { thus puzzle pieces })

						weHaveAnotherArgument = pieces popIfPresent ","
					}

					pieces popIfPresent ARGUMENT.stopScopeKeyword()
					pieces pop ")"
				}
				else {
					type = IdlExtendedAttribute.Type.IDENT
				}
			}
			else if (pieces peek Glob.extendedAttributeWildcardKeyword) {
				pieces popIfPresent Glob.extendedAttributeWildcardKeyword
				type = IdlExtendedAttribute.Type.WILDCARD
			}
			else thus throwing objection
		}

	}


	companion object {
		infix fun puzzleMultiple(pieces: Pieces): List<IdlExtendedAttribute> {
			pieces pop EXTENDED_ATTRIBUTE.startScopeKeyword()
			var weHaveAnotherExtendedAttribute = true
			val ret = mutableListOf<IdlExtendedAttribute>()

			while (weHaveAnotherExtendedAttribute) {
				ret += IdlExtendedAttribute(IdlExtendedAttributeBuilder().apply { thus puzzle pieces })
				weHaveAnotherExtendedAttribute = pieces popIfPresent ","
			}

			pieces pop EXTENDED_ATTRIBUTE.stopScopeKeyword()

			return ret
		}
	}
}