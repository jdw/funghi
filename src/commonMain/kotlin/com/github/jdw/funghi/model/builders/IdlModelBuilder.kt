package com.github.jdw.funghi.model.builders

import com.github.jdw.funghi.fragments.IdlDictionary
import com.github.jdw.funghi.fragments.IdlEnum
import com.github.jdw.funghi.fragments.IdlExtendedAttribute
import com.github.jdw.funghi.fragments.IdlFragment
import com.github.jdw.funghi.fragments.IdlInterface
import com.github.jdw.funghi.fragments.IdlTypedef
import com.github.jdw.funghi.fragments.builders.IdlDictionaryBuilder
import com.github.jdw.funghi.fragments.builders.IdlEnumBuilder
import com.github.jdw.funghi.fragments.builders.IdlExtendedAttributeBuilder
import com.github.jdw.funghi.fragments.builders.IdlInterfaceBuilder
import com.github.jdw.funghi.fragments.builders.IdlTypedefBuilder
import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.Scope.DICTIONARY
import com.github.jdw.funghi.pieces.Scope.ENUM
import com.github.jdw.funghi.pieces.Scope.EXTENDED_ATTRIBUTE
import com.github.jdw.funghi.pieces.Scope.INTERFACE
import com.github.jdw.funghi.pieces.Scope.MODEL
import com.github.jdw.funghi.pieces.Scope.TYPEDEF
import echt
import throws

class IdlModelBuilder {
	val interfaces = mutableListOf<IdlInterfaceBuilder>()
	val dictionaries = mutableListOf<IdlDictionary>()
	val typedefs = mutableListOf<IdlTypedef>()
	val enums = mutableListOf<IdlEnum>()
	val fragments = mutableListOf<IdlFragment>()

	infix fun puzzle(pieces: Pieces) {
		var extendedAttributes: List<IdlExtendedAttribute> = mutableListOf()

		pieces pop MODEL.startScopeKeyword()

		while (pieces.peekIsStartScope()) {
			when (pieces.peekStartScope()) {
				DICTIONARY -> dictionaries += IdlDictionary(IdlDictionaryBuilder().apply { thus puzzle pieces }).apply { fragments += this }
				TYPEDEF -> typedefs += IdlTypedef(IdlTypedefBuilder().apply { thus puzzle pieces }).apply { fragments += this }
				ENUM -> enums += IdlEnum(IdlEnumBuilder().apply { thus puzzle pieces }).apply { fragments += this }
				INTERFACE -> {
					IdlInterfaceBuilder(extendedAttributes)
						.apply {
							(extendedAttributes.isNotEmpty())
								.echt { extendedAttributes = mutableListOf() }
						}
						.apply { thus puzzle pieces }
						.apply {
							interfaces += this
						}
						.apply { fragments.add(IdlInterface(this)) }
				}

				EXTENDED_ATTRIBUTE -> {
					extendedAttributes = IdlExtendedAttributeBuilder puzzleMultiple pieces
				}

				else -> throws()
			}
		}

		pieces pop MODEL.stopScopeKeyword()
	}
}