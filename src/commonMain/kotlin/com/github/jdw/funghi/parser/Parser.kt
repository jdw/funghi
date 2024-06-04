package com.github.jdw.funghi.parser

import Glob
import com.github.jdw.funghi.model.IdlModel
import com.github.jdw.funghi.model.builders.IdlModelBuilder
import throws
import toPieces

internal class Parser(val settings: ParserSettings, val filename: String) {
	fun parse(data: String): IdlModel {
		Glob.currentParserSettings = settings
		val data05 = step05AddLineNumbers(data)
		val data10 = step10RemoveLineComments(data05)
		val data20 = step20RemoveBlockComments(data10)
		val data25 = step25EmptyArrayAndEmptyDictionaryToKeywords(data20);
		val data30 = step30RemoveAllWhiteSpacesExceptOneSpace(data25)
		val data40 = step40InsertNewlineAtTheRightPlaces(data30)
		val data50 = step50InsertStartOfScopeKeywordAndEndOfScopeKeywordAtTheRightPlaces(data40)
		val data60 = step60TrimPieces(data50)
		val builder = IdlModelBuilder()

		println(data60)


		return IdlModel(builder)
	}


	private fun step60TrimPieces(data: String): String = mutableListOf<String>()
		.apply {
			data
				.toPieces()
				.forEach { piece ->
					if (piece.isNotEmpty() && piece.isNotBlank()) add(piece.trim().replace("\n", " "))} }.joinToString(" ")


	private fun step25EmptyArrayAndEmptyDictionaryToKeywords(data: String): String {
		return data
			.replace("= [],", "= ${Glob.emptyArrayKeyword} ,")
			.replace("= {},", "= ${Glob.emptyDictionaryKeyword} ,")
			.replace("= [];", "= ${Glob.emptyArrayKeyword} ;")
			.replace("= {};", "= ${Glob.emptyDictionaryKeyword} ;")
			.replace("= [])", "= ${Glob.emptyArrayKeyword} )")
			.replace("= {})", "= ${Glob.emptyDictionaryKeyword} )")

	}


	private fun step50InsertStartOfScopeKeywordAndEndOfScopeKeywordAtTheRightPlaces(data: String): String {
		val ret = mutableListOf<String>()

		var currentScope = ""
		val lines = data.split("\n")
		for (idx in lines.indices) {
			val line = lines[idx].trim()
			if (line.contains("[") && line.contains("]")) {
				ret += "${Glob.extendedAttributeStartScopeKeyword} $line ${Glob.extendedAttributeEndScopeKeyword}"
				continue
			}

			if (line.contains("attribute")) {
				if (line.endsWith(";")) ret += "${Glob.attributeStartScopeKeyword} $line ${Glob.attributeEndScopeKeyword}"
				else ret += "${Glob.attributeStartScopeKeyword} $line"

				continue
			}

			if (line.contains("interface")) {
				currentScope = "interface"
				ret += "${Glob.interfaceStartScopeKeyword} $line"
				continue
			}

			if (line.contains("dictionary")) {
				currentScope = "dictionary"
				ret += "${Glob.dictionaryStartScopeKeyword} $line"
				continue
			}

			if (line.contains("enum")) {
				ret += "${Glob.enumStartScopeKeyword} $line ${Glob.enumEndScopeKeyword}"
				continue
			}

			if (line.contains("typedef")) {
				ret += "${Glob.typedefStartScopeKeyword} $line ${Glob.typedefEndScopeKeyword}"
				continue
			}

			if (line.contains("constructor(")) {
				ret += "${Glob.operationConstructorStartScopeKeyword} $line ${Glob.operationConstructorEndScopeKeyword}"
				continue
			}

			if (line.contains("};")) {
				if ("" == currentScope) throws()
				ret += when (currentScope) {
					"dictionary" -> "$line ${Glob.dictionaryEndScopeKeyword}"
					"interface" -> "$line ${Glob.interfaceEndScopeKeyword}"
					else -> throws()
				}

				continue
			}

			if (line.contains("getter") || line.contains("setter") || line.contains("deleter")) {
				ret += "${Glob.operationStartScopeKeyword} $line ${Glob.operationEndScopeKeyword}"
				continue
			}

			if (line.contains("(") && !line.contains("constructor")) {
				val values = Glob
					.currentParserSettings!!
					.operationRegex()
					.find(line)
					?.groupValues
					?: emptyList()

				if (values.isNotEmpty()) {
					ret += "${Glob.operationStartScopeKeyword} $line ${Glob.operationEndScopeKeyword}"
					continue
				}
			}

			if (line.contains("=")) {
				if (line.contains("const")) ret += "${Glob.constAttributeStartScopeKeyword} $line ${Glob.constAttributeEndScopeKeyword}"
				else ret += "${Glob.dictionaryMemberStartScopeKeyword} $line ${Glob.dictionaryMemberEndScopeKeyword}"
				continue
			}

			if (line.contains("includes")) {
				ret += "${Glob.includesStartScopeKeyword} $line ${Glob.includesEndScopeKeyword}"
				continue
			}

			if (line.endsWith(";")) ret += "$line ${Glob.attributeEndScopeKeyword}"
			else ret += line
		}

		return ret.joinToString("\n")
	}


	private fun step40InsertNewlineAtTheRightPlaces(data: String): String {
		return data
			.replace("[", "\n[")
			.replace("]", "]\n")
			.replace(";", ";\n")
			.replace("{ ", "{\n ")
	}


	private fun step10RemoveLineComments(data: String): String {
		val ret = mutableListOf<String>()

		data
			.split("\n")
			.forEach { line ->
				ret += if (line.contains("//")) line.split("//").first()
				else line
			}

		return ret.joinToString(" ")
	}


	private fun step20RemoveBlockComments(data: String): String {
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

	private fun step30RemoveAllWhiteSpacesExceptOneSpace(data: String): String {
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


	private fun step05AddLineNumbers(data: String): String {
		val ret = mutableListOf<String>()
		var lineNumber = 1
		data
			.split("\n")
			.forEach { lineRaw ->
				val line = lineRaw.trim()
				ret += "${Glob.lineNumberKeyword}$lineNumber $line"
				lineNumber++
			}

		return ret.joinToString("\n")
	}
}