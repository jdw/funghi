package com.github.jdw.funghi.parser

import Glob
import com.github.jdw.funghi.fragments.IdlScopes
import com.github.jdw.funghi.model.IdlModel
import com.github.jdw.funghi.model.builders.IdlModelBuilder
import com.github.jdw.funghi.pieces.Pieces
import noop
import throws

internal class Parser(val settings: ParserSettings, val filename: String) {
	fun parse(data: String): IdlModel {
		Glob.currentParserSettings = settings
		Glob.filename = filename

		val data05 = step05AddLineNumbers(data)
		val data10 = step10RemoveLineComments(data05)
		val data20 = step20RemoveBlockComments(data10)
		val data25 = step25EmptyArrayAndEmptyDictionaryToKeywords(data20);
		val data30 = step30RemoveAllWhiteSpacesExceptOneSpace(data25)
		val data40 = step40InsertNewlineAtTheRightPlaces(data30)
		val data50 = step50InsertStartOfScopeKeywordAndEndOfScopeKeywordAtTheRightPlaces(data40)
		val data60 = step60TrimPieces(data50)
		val pieces = step70ToPieces(data60)
		val builder = IdlModelBuilder()

		when (pieces.popStartScopeThrowIfNot()) {
			IdlScopes.DICTIONARY -> noop()
			IdlScopes.TYPEDEF -> noop()
			IdlScopes.ENUM -> noop()
			IdlScopes.INTERFACE -> noop()
			IdlScopes.EXTENDED_ATTRIBUTE -> noop()
			else -> throws()
		}

		return IdlModel(builder)
	}


	private fun step70ToPieces(data: String): Pieces = Pieces(data).apply { Glob.pieces = this }


	private fun step60TrimPieces(data: String): String = mutableListOf<String>().apply { data.split(" ").forEach { piece -> if (piece.isNotEmpty() && piece.isNotBlank()) add(piece.trim().replace("\n", " "))} }.joinToString(" ")


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

		var currentScope: IdlScopes? = null
		val lines = data.split("\n")
		for (idx in lines.indices) {
			val line = lines[idx].trim()
			if (line.contains("[") && line.contains("]")) {
				ret += "${IdlScopes.EXTENDED_ATTRIBUTE.startScopeKeyword()} $line ${IdlScopes.EXTENDED_ATTRIBUTE.endScopeKeyword()}"
				continue
			}

			if (line.contains("attribute")) {
				if (line.endsWith(";")) ret += "${IdlScopes.ATTRIBUTE.startScopeKeyword()} ${line.replace("attribute", "")} ${IdlScopes.ATTRIBUTE.endScopeKeyword()}"
				else ret += "${IdlScopes.ATTRIBUTE.startScopeKeyword()} $line"

				continue
			}

			if (line.contains("interface")) {
				currentScope = IdlScopes.INTERFACE
				ret += "${IdlScopes.INTERFACE.startScopeKeyword()} $line"
				continue
			}

			if (line.contains("dictionary")) {
				currentScope = IdlScopes.DICTIONARY
				ret += "${IdlScopes.DICTIONARY.startScopeKeyword()} $line"
				continue
			}

			if (line.contains("enum")) {
				ret += "${IdlScopes.ENUM.startScopeKeyword()} $line ${IdlScopes.ENUM.endScopeKeyword()}"
				continue
			}

			if (line.contains("typedef")) {
				ret += "${IdlScopes.TYPEDEF.startScopeKeyword()} $line ${IdlScopes.TYPEDEF.endScopeKeyword()}"
				continue
			}

			if (line.contains("constructor(")) {
				ret += "${IdlScopes.OPERATION_CONSTRUCTOR.startScopeKeyword()} $line ${IdlScopes.OPERATION_CONSTRUCTOR.endScopeKeyword()}"
				continue
			}

			if (line.contains("};")) {
				if (null == currentScope) throws()
				ret += when (currentScope) {
					IdlScopes.DICTIONARY -> IdlScopes.DICTIONARY.endScopeKeyword()
					IdlScopes.INTERFACE -> IdlScopes.INTERFACE.endScopeKeyword()
					else -> throws()
				}

				continue
			}

			if (line.contains("getter") || line.contains("setter") || line.contains("deleter")) {
				ret += "${IdlScopes.OPERATION.startScopeKeyword()} $line ${IdlScopes.OPERATION.endScopeKeyword()}"
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
					ret += "${IdlScopes.OPERATION.startScopeKeyword()} $line ${IdlScopes.OPERATION.endScopeKeyword()}"
					continue
				}
			}

			if (line.contains("=")) {
				if (line.contains("const")) ret += "${IdlScopes.CONST_ATTRIBUTE.startScopeKeyword()} $line ${IdlScopes.CONST_ATTRIBUTE.endScopeKeyword()}"
				else ret += "${IdlScopes.DICTIONARY_MEMBER.startScopeKeyword()} $line ${IdlScopes.DICTIONARY_MEMBER.endScopeKeyword()}"
				continue
			}

			if (line.contains("includes")) {
				ret += "${IdlScopes.INCLUDES.startScopeKeyword()} $line ${IdlScopes.INCLUDES.endScopeKeyword()}"
				continue
			}

			if (line.endsWith(";")) ret += "$line ${IdlScopes.ATTRIBUTE.endScopeKeyword()}"
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