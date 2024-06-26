package com.github.jdw.funghi.fragments.builders

import Glob
import com.github.jdw.funghi.fragments.IdlType
import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.Scope.CONSTANT_ATTRIBUTE
import throws


/**
 * A constant is a declaration (matching Const) used to bind a constant value to a name.
 * Constants can appear on interfaces and callback interfaces.
 *
 * See further:
 * * [IDL constants](https://webidl.spec.whatwg.org/#idl-constants)
 */
class IdlConstantAttributeBuilder: IdlFragmentBuilder() {
	/**
	 * The identifier of a constant must not be the same as the identifier of another interface member or callback
	 * interface member defined on the same interface or callback interface. The identifier also must not be "length",
	 * "name" or "prototype".
	 */
	var identifier: String? = null
		set(value) {
			if (setOf("length", "name", "prototype").contains(value)) throws()
			field = value
		}

	/**
	 * The type of constant (matching ConstType) must not be any type other than a primitive type. If an identifier
	 * is used, it must reference a typedef whose type is a primitive type.
	 */
	var type: IdlType? = null
		set(value) {
			field = if (null == value) null
					else if (Glob.parserSettings!!.primitiveTypesKeywords().contains(value.name)) value
					else throws()
		}

	/**
	 * The ConstValue part of a constant declaration gives the value of the constant, which can be one of the two
	 * boolean literal tokens (true and false), an integer token, a decimal token, or one of the three special floating
	 * point constant values (-Infinity, Infinity and NaN).
	 */
	var value: String? = null
		set(value) {
			field = if (null == value) null
					else if (value matches "0x[a-zA-Z0-9]+".toRegex()) value // Hexadecimal
					else if (value matches "[0-9]+\\.[0-9]+".toRegex()) value // Floating point
					else if (value matches "^\\d+\$".toRegex()) value // Integer
					else if (setOf("-Infinity", "Infinity", "NaN", "true", "false").contains(value)) value
					else throws()
		}


	override fun puzzle(pieces: Pieces) {
		pieces pop CONSTANT_ATTRIBUTE.startScopeKeyword()

		type = IdlType(IdlTypeBuilder().apply { thus puzzle pieces })
		identifier = pieces pop 1
		value = pieces pop 1
		pieces pop ";"

		pieces pop CONSTANT_ATTRIBUTE.stopScopeKeyword()
	}
}