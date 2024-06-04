package com.github.jdw.funghi.parser

import com.github.jdw.funghi.fragments.builders.IdlDictionaryBuilder
import com.github.jdw.funghi.fragments.builders.IdlEnumBuilder
import com.github.jdw.funghi.fragments.builders.IdlInterfaceBuilder
import com.github.jdw.funghi.fragments.builders.IdlTypedefBuilder
import com.github.jdw.funghi.model.IdlModel
import com.github.jdw.funghi.model.builders.IdlModelBuilder

internal class Parser(val settings: ParserSettings, val filename: String) {
	fun parse(data: String): IdlModel {
		Glob.currentParserSettings = settings
		val data1 = step10_removeLineComments(data)
		val data2 = step20_removeBlockComments(data1)
		val data3 = step30_removeAllWhiteSpacesExceptOneSpace(data2)
		val data4 = step40_insertNewlineAtTheRightPlaces(data3)
		var interfaceBuilder: IdlInterfaceBuilder? = null
		var dictionaryBuilder: IdlDictionaryBuilder? = null
		val builder = IdlModelBuilder()
		var attributeLine = ""

		step50_splitOnNewLine(data4).forEach { lineRaw ->
			val line = lineRaw.trim()

			if (null != interfaceBuilder) {
				if ("};" == line) {
					builder.interfaces += interfaceBuilder!!
					interfaceBuilder = null
				}
				else {
					interfaceBuilder!!.parseLine(line)
				}
			}
			else if (null != dictionaryBuilder) {
				if ("};" == line) {
					builder.dictionaries += dictionaryBuilder!!
					dictionaryBuilder = null
				}
				else {
					//dictionaryBuilder!!.parseLine(line)
				}
			}
			else {
				if (line.startsWith("[") /* && null == interfaceBuilder */) {
					attributeLine = line
				}
				else if (line.contains("interface")) {
					interfaceBuilder = IdlInterfaceBuilder.parseDefiningLines(attributeLine, line, builder)
					interfaceBuilder!!.parseLine(line)
				}
				else if (line.contains("dictionary")) {
					dictionaryBuilder = IdlDictionaryBuilder()
					//dictionaryBuilder!!.parseLine(line)
				}
				else if (line.contains("enum")) {
					val enumBuilder = IdlEnumBuilder()
					//enumBuilder.parseLine(line)
					builder.enums += enumBuilder
				}
				else if (line.contains("typedef")) {
					val typedefBuilder = IdlTypedefBuilder()
					//typedefBuilder.parseLine(line)
					builder.typedefs += typedefBuilder
				}
			}
		}

		return IdlModel(builder)
	}


	private fun step50_splitOnNewLine(data: String): List<String> = data.split("\n")


	private fun step40_insertNewlineAtTheRightPlaces(data: String): String {
		return data
			.replace("]", "]\n")
			.replace(";", ";\n")
			.replace("{", "{\n")
	}


	private fun step10_removeLineComments(data: String): String {
		val ret = mutableListOf<String>()

		data
			.split("\n")
			.forEach { line ->
				ret += if (line.startsWith("//")) ""
				else if (line.contains("//")) line.split("//").first()
				else line
			}

		return ret.joinToString(" ")
	}


	private fun step20_removeBlockComments(data: String): String {
		val ret = mutableListOf<String>()

		var weAreInABlockComment = false
		data.split(" ").forEach { piece ->
			if (weAreInABlockComment) {
				if (piece.contains("*/")) {
					weAreInABlockComment = false
					ret += piece.replace("*/", "")
				}
			}
			else {
				if (piece.contains("/*")) {
					weAreInABlockComment = true
					ret += piece.replace("/*", "")
				}
				else if (piece.contains("*/")) throw IllegalStateException("End of block comment found outside of block comment block!")
				else ret += piece
			}
		}

		return ret.joinToString(" ")
	}

	private fun step30_removeAllWhiteSpacesExceptOneSpace(data: String): String {
		val ret = mutableListOf<String>()

		data
			.split(" ")
			.forEach { line ->
				line
					.trim()
					.apply { if ("" != this) ret += this }
			}

		return ret.joinToString(" ")
	}
}