package com.github.jdw.funghi.model

import com.github.jdw.funghi.fragments.IdlInterface
import com.github.jdw.funghi.model.builders.IdlModelBuilder

class IdlModel(builder: IdlModelBuilder) {
	val errors = mutableListOf<String>()
	fun isErrorFree(): Boolean = errors.isEmpty()

	val interfaces: List<IdlInterface> =
		if (builder.partialInterfaces.isNotEmpty() || builder.interfaces.isNotEmpty()) {
			val ret = mutableListOf<IdlInterface>()
			builder
				.interfaces
				.forEach {
					try {
						ret += IdlInterface(it)
					}
					catch (e: Exception) {
						errors += e.message
							?: "An error occurred while building interface '${it.name}'!"
					}
				}

			builder
				.partialInterfaces
				.values
				.forEach {
					try {
						ret += IdlInterface(it)
					}
					catch (e: Exception) {
						errors += e.message
							?: "An error occurred while building partial interface '${it.name}'!"
					}
				}
			ret.toList()
		}
		else {
			emptyList()
		}
}