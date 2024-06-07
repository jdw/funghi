package com.github.jdw.funghi.model

import com.github.jdw.funghi.fragments.IdlInterface
import com.github.jdw.funghi.model.builders.IdlModelBuilder

class IdlModel(builder: IdlModelBuilder) {
	val errors = mutableListOf<String>()
	fun isErrorFree(): Boolean = errors.isEmpty()

	val interfaces =
		if (builder.partialInterfaces.isNotEmpty() || builder.interfaces.isNotEmpty()) {
			val ret = mutableListOf<IdlInterface>()
			ret.addAll(builder.interfaces)
			builder
				.partialInterfaces
				.forEach { (_, interfaze) ->
					ret += IdlInterface(interfaze)
				}
			ret.toSet().toTypedArray()
		}
		else {
			emptyArray()
		}
}