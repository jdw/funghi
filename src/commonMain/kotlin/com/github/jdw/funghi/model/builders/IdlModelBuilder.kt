package com.github.jdw.funghi.model.builders

import com.github.jdw.funghi.fragments.IdlDictionary
import com.github.jdw.funghi.fragments.IdlEnum
import com.github.jdw.funghi.fragments.IdlInterface
import com.github.jdw.funghi.fragments.IdlTypedef
import com.github.jdw.funghi.fragments.builders.IdlInterfaceBuilder

class IdlModelBuilder {
	val interfaces = mutableListOf<IdlInterface>()
	val partialInterfaces = mutableMapOf<String, IdlInterfaceBuilder>()
	val dictionaries = mutableListOf<IdlDictionary>()
	val typedefs = mutableListOf<IdlTypedef>()
	val enums = mutableListOf<IdlEnum>()
}