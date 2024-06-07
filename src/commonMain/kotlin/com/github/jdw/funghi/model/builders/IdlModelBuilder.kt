package com.github.jdw.funghi.model.builders

import com.github.jdw.funghi.fragments.IdlDictionary
import com.github.jdw.funghi.fragments.IdlEnum
import com.github.jdw.funghi.fragments.IdlExtendedAttribute
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
	val interfaces = mutableListOf<IdlInterface>()
	val partialInterfaces = mutableMapOf<String, IdlInterfaceBuilder>()
	val dictionaries = mutableListOf<IdlDictionary>()
	val typedefs = mutableListOf<IdlTypedef>()
	val enums = mutableListOf<IdlEnum>()

	infix fun puzzle(pieces: Pieces) {
		var extendedAttribute: IdlExtendedAttribute? = null //TODO Should be list

		pieces popStartScope Scope.MODEL

		while (pieces.peekIsStartScope()) {
			when (pieces.peekStartScope()) {
				Scope.DICTIONARY -> noop()
				Scope.TYPEDEF -> noop()
				Scope.ENUM -> noop()
				Scope.INTERFACE -> {
					IdlInterfaceBuilder()
						.apply {
							(null != extendedAttribute)
								.echt { extendedAttributes += extendedAttribute!! }
								.echt { extendedAttribute = null }
						}
						.apply { thus puzzle pieces }
						.apply {
							if (this.isPartial) {
								if (partialInterfaces.containsKey(this.name)) partialInterfaces[this.name]!!.fuse(this)
								else partialInterfaces[this.name!!] = this
							}
							else interfaces += IdlInterface(this)
						}
				}

				Scope.EXTENDED_ATTRIBUTE -> {
					extendedAttribute = IdlExtendedAttribute(IdlExtendedAttributeBuilder().apply { puzzle(
						pieces
					) })
				}

				else -> throws()
			}
		}

		pieces popEndScope Scope.MODEL
	}
}