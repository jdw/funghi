package com.github.jdw.funghi.model.builders

import com.github.jdw.funghi.fragments.builders.IdlDictionaryBuilder
import com.github.jdw.funghi.fragments.builders.IdlEnumBuilder
import com.github.jdw.funghi.fragments.builders.IdlInterfaceBuilder
import com.github.jdw.funghi.fragments.builders.IdlTypedefBuilder

class IdlModelBuilder {
	val interfaces = mutableListOf<IdlInterfaceBuilder>()
	val partialInterfaces = mutableMapOf<String, IdlInterfaceBuilder>()
	val dictionaries = mutableListOf<IdlDictionaryBuilder>()
	val typedefs = mutableListOf<IdlTypedefBuilder>()
	val enums = mutableListOf<IdlEnumBuilder>()
}