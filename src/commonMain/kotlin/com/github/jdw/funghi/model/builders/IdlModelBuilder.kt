package com.github.jdw.funghi.model.builders

import com.github.jdw.funghi.fragments.IdlDictionary
import com.github.jdw.funghi.fragments.IdlEnum
import com.github.jdw.funghi.fragments.IdlExtendedAttribute
import com.github.jdw.funghi.fragments.IdlFragment
import com.github.jdw.funghi.fragments.IdlInterface
import com.github.jdw.funghi.pieces.Scope
import com.github.jdw.funghi.fragments.IdlTypedef
import com.github.jdw.funghi.fragments.builders.IdlExtendedAttributeBuilder
import com.github.jdw.funghi.fragments.builders.IdlInterfaceBuilder
import com.github.jdw.funghi.pieces.Pieces
import echt
import noop
import throws

class IdlModelBuilder {
	val interfaces = mutableListOf<IdlInterfaceBuilder>()
	val dictionaries = mutableListOf<IdlDictionary>()
	val typedefs = mutableListOf<IdlTypedef>()
	val enums = mutableListOf<IdlEnum>()
	val fragments = mutableListOf<IdlFragment>()

	infix fun puzzle(pieces: Pieces) {
		var extendedAttributes: List<IdlExtendedAttribute> = mutableListOf()

		pieces popStartScope Scope.MODEL

		while (pieces.peekIsStartScope()) {
			when (pieces.peekStartScope()) {
				Scope.DICTIONARY -> noop()
				Scope.TYPEDEF -> noop()
				Scope.ENUM -> noop()
				Scope.INTERFACE -> {
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

				Scope.EXTENDED_ATTRIBUTE -> {
					extendedAttributes = IdlExtendedAttributeBuilder puzzleMultiple pieces
				}

				else -> throws()
			}
		}

		pieces popEndScope Scope.MODEL
	}
}