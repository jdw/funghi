package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlSpecialOperationBuilder
import throws

/**
 * Getters and setters come in two varieties: ones that take a DOMString as a property name, known as named property
 * getters and named property setters, and ones that take an unsigned long as a property index, known as indexed
 * property getters and indexed property setters. There is only one variety of deleter: named property deleters.
 * See § 2.5.6.1 Indexed properties and § 2.5.6.2 Named properties for details.
 *
 * On a given interface, there must exist at most one named property deleter, and at most one of each variety of
 * getter and setter.
 *
 * If an interface has a setter of a given variety, then it must also have a getter of that variety. If it has a named
 * property deleter, then it must also have a named property getter.
 *
 * Special operations declared using operations must not be variadic nor have any optional arguments.
 *
 * If an object implements more than one interface that defines a given special operation, then it is undefined which
 * (if any) special operation is invoked for that operation.
 *
 * See further:
 * * [Web IDL standard special operations](https://webidl.spec.whatwg.org/#idl-special-operations)
 */
class IdlSpecialOperation(builder: IdlSpecialOperationBuilder): IdlFragment {
	val isGetter = builder.isGetter
	val isSetter = builder.isSetter
	val isDeleter = builder.isDeleter
	val returnType = builder.returnType!!
	val arguments = builder.arguments.toSet().toTypedArray()


	init {
		if (!(isGetter || isSetter || isDeleter)) throws()
	}


	override fun toString(): String {
		val getter = if (isGetter) "getter" else ""
		val setter = if (isSetter) "setter" else ""
		val deleter = if (isDeleter) "deleter" else ""

		return "$getter$setter$deleter $returnType ${arguments.joinToString(prefix = "(", separator = ", ", postfix = ")")};"
	}
}