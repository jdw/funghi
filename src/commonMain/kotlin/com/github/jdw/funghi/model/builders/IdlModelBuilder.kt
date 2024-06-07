package com.github.jdw.funghi.model.builders

import com.github.jdw.funghi.fragments.IdlDictionary
import com.github.jdw.funghi.fragments.IdlEnum
import com.github.jdw.funghi.fragments.IdlExtendedAttribute
import com.github.jdw.funghi.fragments.IdlInterface
import com.github.jdw.funghi.fragments.IdlScope
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

	infix fun parse(pieces: Pieces) {
		var extendedAttribute: IdlExtendedAttribute? = null //TODO Should be list

		pieces popStartScope IdlScope.MODEL

		while (pieces.peekIsStartScope()) {
			when (pieces.peekStartScope()) {
				IdlScope.DICTIONARY -> noop()
				IdlScope.TYPEDEF -> noop()
				IdlScope.ENUM -> noop()
				IdlScope.INTERFACE -> {
					IdlInterfaceBuilder()
						.apply {
							(null != extendedAttribute)
								.echt { extendedAttributes += extendedAttribute!! }
								.echt { extendedAttribute = null }
						}
						.apply { thus parse pieces }
						.apply {
							if (this.isPartial) {
								if (partialInterfaces.containsKey(this.name)) partialInterfaces[this.name]!!.fuse(this)
								else partialInterfaces[this.name!!] = this
							}
							else interfaces += IdlInterface(this)
						}
				}

				IdlScope.EXTENDED_ATTRIBUTE -> {
					extendedAttribute = IdlExtendedAttribute(IdlExtendedAttributeBuilder().apply { parse(pieces) })
				}

				else -> throws()
			}
		}

		pieces popEndScope IdlScope.MODEL
	}
}