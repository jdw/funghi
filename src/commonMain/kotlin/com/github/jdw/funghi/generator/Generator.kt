package com.github.jdw.funghi.generator

import com.github.jdw.funghi.generator.builders.GeneratorSettingsBuilder
import com.github.jdw.funghi.model.IdlModel

interface Generator {
	fun generate(model: IdlModel, block: GeneratorSettingsBuilder.() -> GeneratorSettings)
}