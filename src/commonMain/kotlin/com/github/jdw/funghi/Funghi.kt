package com.github.jdw.funghi

import com.github.jdw.funghi.model.IdlModel
import com.github.jdw.funghi.parser.Parser
import com.github.jdw.funghi.parser.ParserSettings


class Funghi {
	fun parse(data: String, filename: String): IdlModel {
		val results = mutableMapOf<ParserSettings, IdlModel>()

		ParserSettings.entries.forEach { version ->
			val result = Parser(version, filename).parse(data)
			if (result.isErrorFree()) results[version] = result
		}

		return results.entries.sortedBy { it.key }.first().value
	}
}