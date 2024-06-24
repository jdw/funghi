package com.github.jdw.funghi.model

import com.github.jdw.funghi.fragments.IdlInterface
import com.github.jdw.funghi.fragments.builders.IdlInterfaceBuilder
import com.github.jdw.funghi.model.builders.IdlModelBuilder

class IdlModel(builder: IdlModelBuilder) {
	val errors = mutableListOf<String>()
	fun isErrorFree(): Boolean = errors.isEmpty()

	val interfaces =
		if (builder.interfaces.isNotEmpty()) {
			val tmp = mutableMapOf<String, IdlInterfaceBuilder>()

			builder.interfaces.forEach { if (!it.isPartial) tmp[it.name!!] = it }
			builder.interfaces.forEach { if (it.isPartial) tmp[it.name!!]!!.fuse(it) }

			tmp.values.map { IdlInterface(it) }.toTypedArray()
		}
		else {
			emptyArray()
		}

	private val fragments = builder.fragments.toTypedArray()

	val enums = builder.enums.toList()
	val typedefs = builder.typedefs.toList()


	override fun toString(): String = fragments.joinToString("\n\n")
}