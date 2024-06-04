package com.github.jdw.funghi.generator

import com.github.jdw.funghi.generator.builders.GeneratorSettingsBuilder

class GeneratorSettings(builder: GeneratorSettingsBuilder) {
	val explodeOptional = builder.explodeOptional
	val explodeReturnType = builder.explodeReturnType
	val explodeFunctionArguments = builder.explodeFunctionArguments
}