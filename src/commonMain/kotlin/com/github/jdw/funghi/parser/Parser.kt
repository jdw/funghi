package com.github.jdw.funghi.parser

import com.github.jdw.funghi.fragments.IdlDictionary
import com.github.jdw.funghi.fragments.IdlEnum
import com.github.jdw.funghi.fragments.IdlInterface
import com.github.jdw.funghi.fragments.IdlTypedef
import com.github.jdw.funghi.fragments.builders.IdlDictionaryBuilder
import com.github.jdw.funghi.fragments.builders.IdlEnumBuilder
import com.github.jdw.funghi.fragments.builders.IdlInterfaceBuilder
import com.github.jdw.funghi.fragments.builders.IdlTypedefBuilder
import com.github.jdw.funghi.model.IdlModel
import com.github.jdw.funghi.model.builders.IdlModelBuilder

internal class Parser(val settings: ParserSettings, val filename: String) {
	fun parse(data: String): IdlModel {
		Glob.currentParserSettings = settings
		val data05 = step05_addLineNumbers(data)
		val data10 = step10_removeLineComments(data)
		val data20 = step20_removeBlockComments(data10)
		val data30 = step30_removeAllWhiteSpacesExceptOneSpace(data20)
		val data40 = step40_insertNewlineAtTheRightPlaces(data30)
		var interfaceBuilder: IdlInterfaceBuilder? = null
		var dictionaryBuilder: IdlDictionaryBuilder? = null
		val builder = IdlModelBuilder()
		var extendedAttributeLine = ""

		step50_splitOnNewLine(data40).forEach { lineRaw ->
			val line = lineRaw.trim()

			println(line)
			if (line.startsWith("[") /* && null == interfaceBuilder */) {
				extendedAttributeLine = line
			}
			else if (null != interfaceBuilder) {
				if ("};" == line) {
					builder.interfaces += IdlInterface(interfaceBuilder!!)
					interfaceBuilder = null
				}
				else {
					if ("" != extendedAttributeLine) interfaceBuilder!!.parseLine("$extendedAttributeLine $line")
					else interfaceBuilder!!.parseLine(line)
				}
			}
			else if (null != dictionaryBuilder) {
				if ("};" == line) {
					builder.dictionaries += IdlDictionary(dictionaryBuilder!!)
					dictionaryBuilder = null
				}
				else {
					//dictionaryBuilder!!.parseLine(line)
				}
			}
			else {
				if (line.contains("interface")) {
					interfaceBuilder = IdlInterfaceBuilder.parseDefiningLines(extendedAttributeLine, line, builder)
					//interfaceBuilder!!.parseLine(line)
					extendedAttributeLine = ""
				}
				else if (line.contains("dictionary")) {
					dictionaryBuilder = IdlDictionaryBuilder()
					//dictionaryBuilder!!.parseLine(line)
				}
				else if (line.contains("enum")) {
					val enumBuilder = IdlEnumBuilder()
					//enumBuilder.parseLine(line)
					builder.enums += IdlEnum(enumBuilder)
				}
				else if (line.contains("typedef")) {
					val typedefBuilder = IdlTypedefBuilder()
					//typedefBuilder.parseLine(line)
					builder.typedefs += IdlTypedef(typedefBuilder)
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


	private fun step05_addLineNumbers(data: String): String {
		data
			.split()
	}
}