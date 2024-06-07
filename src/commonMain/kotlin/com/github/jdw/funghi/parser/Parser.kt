package com.github.jdw.funghi.parser


import Glob
import com.github.jdw.funghi.fragments.IdlScope
import com.github.jdw.funghi.model.IdlModel
import com.github.jdw.funghi.model.builders.IdlModelBuilder
import com.github.jdw.funghi.pieces.Pieces
import throws


internal class Parser(val settings: ParserSettings, val filename: String) {
	infix fun parse(data: String): IdlModel {
		Glob.parserSettings = settings
		Glob.filename = filename

		val data05 = step05AddLineNumbers(data)
		val data10 = step10RemoveLineComments(data05)
		val data20 = step20RemoveBlockComments(data10)
		val data25 = step25EmptyArrayAndEmptyDictionaryToKeywords(data20);
		val data30 = step30RemoveAllWhiteSpacesExceptOneSpace(data25)
		val data40 = step40InsertNewlineAtTheRightPlaces(data30)
		val data50 = step50InsertStartOfScopeKeywordAndEndOfScopeKeywordAtTheRightPlaces(data40)
		val data60 = step60TrimPieces(data50)
		val data65 = step65AddModelStartAndEndScope(data60)
		val pieces = step70ToPieces(data65)

		return IdlModel(IdlModelBuilder().apply { this parse pieces })
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
		data
			.replace("/*", " /* ")
			.replace("*/", " */ ")
			.split(" ").forEach { piece ->
				if (weAreInABlockComment) {
					if (piece.contains("*/")) {
						weAreInABlockComment = false
						//ret += piece.replace("*/", "")
					}
				}
				else {
					if (piece.contains("/*")) {
						weAreInABlockComment = true
						//ret += piece.replace("/*", "")
					}
					else if (piece.contains("*/")) throw IllegalStateException("End of block comment found outside of block comment block!")
					else ret += piece
				}
			}

		return ret.joinToString(" ")
	}


	private fun step25EmptyArrayAndEmptyDictionaryToKeywords(data: String): String {
		return data
			.replace("= [],", "= ${Glob.emptyArrayKeyword} ,")
			.replace("= {},", "= ${Glob.emptyDictionaryKeyword} ,")
			.replace("= [];", "= ${Glob.emptyArrayKeyword} ;")
			.replace("= {};", "= ${Glob.emptyDictionaryKeyword} ;")
			.replace("= [])", "= ${Glob.emptyArrayKeyword} )")
			.replace("= {})", "= ${Glob.emptyDictionaryKeyword} )")

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


	private fun step40InsertNewlineAtTheRightPlaces(data: String): String {
		return data
			.replace("[", "\n[")
			.replace("]", "]\n")
			.replace(";", ";\n")
			.replace("{ ", "{\n ")
	}


	private fun step50InsertStartOfScopeKeywordAndEndOfScopeKeywordAtTheRightPlaces(data: String): String {
		val ret = mutableListOf<String>()

		var currentScope: IdlScope? = null
		val lines = data.split("\n")
		for (idx in lines.indices) {
			val line = lines[idx].trim()
			if (line.contains("[") && line.contains("]")) {
				ret += "${IdlScope.EXTENDED_ATTRIBUTE.startScopeKeyword()} $line ${IdlScope.EXTENDED_ATTRIBUTE.endScopeKeyword()}"
				continue
			}

			if (line.contains("attribute")) {
				val newLine = line
					//.replace("attribute", "")
					.replace(";", " ;")
				if (line.endsWith(";")) ret += "${IdlScope.ATTRIBUTE.startScopeKeyword()} $newLine ${IdlScope.ATTRIBUTE.endScopeKeyword()}"
				else ret += "${IdlScope.ATTRIBUTE.startScopeKeyword()} $line"

				continue
			}

			if (line.contains("interface")) {
				currentScope = IdlScope.INTERFACE
				ret += "${IdlScope.INTERFACE.startScopeKeyword()} $line"
				continue
			}

			if (line.contains("dictionary")) {
				currentScope = IdlScope.DICTIONARY
				ret += "${IdlScope.DICTIONARY.startScopeKeyword()} $line"
				continue
			}

			if (line.contains("enum")) {
				ret += "${IdlScope.ENUM.startScopeKeyword()} $line ${IdlScope.ENUM.endScopeKeyword()}"
				continue
			}

			if (line.contains("typedef")) {
				ret += "${IdlScope.TYPEDEF.startScopeKeyword()} $line ${IdlScope.TYPEDEF.endScopeKeyword()}"
				continue
			}

			if (line.contains("constructor(")) {
				val newLine = if (line.contains("constructor();")) line // No arguments!
					.replace("constructor();", "constructor ( );")
				else line
					.replace("constructor(", "constructor ( ${IdlScope.ARGUMENT.startScopeKeyword()} ")
					.replace(");", " ${IdlScope.ARGUMENT.endScopeKeyword()} );")
					.replace(",", " ${IdlScope.ARGUMENT.endScopeKeyword()} , ${IdlScope.ARGUMENT.startScopeKeyword()} ")

				val newValue = "${IdlScope.OPERATION_CONSTRUCTOR.startScopeKeyword()} $newLine ${IdlScope.OPERATION_CONSTRUCTOR.endScopeKeyword()}"

				ret += newValue
				continue
			}

			if (line.contains("};")) {
				if (null == currentScope) throws()
				ret += when (currentScope) {
					IdlScope.DICTIONARY -> IdlScope.DICTIONARY.endScopeKeyword()
					IdlScope.INTERFACE -> IdlScope.INTERFACE.endScopeKeyword()
					else -> throws()
				}

				continue
			}

			if (line.contains("getter") || line.contains("setter") || line.contains("deleter")) {
				val newLine = line
				//.replace("(", "( ")
				//.replace(")", " )")
				//.replace(");", " );")
				//.replace(",", " ,")
				ret += "${IdlScope.OPERATION.startScopeKeyword()} $newLine ${IdlScope.OPERATION.endScopeKeyword()}"
				continue
			}

			if (line.contains("(") && !line.contains("constructor")) {
				val values = Glob
					.parserSettings!!
					.operationRegex()
					.find(line)
					?.groupValues
					?: emptyList()

				if (values.isNotEmpty()) {
					val newLine = line
					//.replace("(", "( ")
					//.replace(")", " )")
					//.replace(");", " );")
					//.replace(",", " ,")
					ret += "${IdlScope.OPERATION.startScopeKeyword()} $newLine ${IdlScope.OPERATION.endScopeKeyword()}"
					continue
				}
			}

			if (line.contains("=")) {
				if (line.contains("const")) ret += "${IdlScope.CONST_ATTRIBUTE.startScopeKeyword()} $line ${IdlScope.CONST_ATTRIBUTE.endScopeKeyword()}"
				else ret += "${IdlScope.DICTIONARY_MEMBER.startScopeKeyword()} $line ${IdlScope.DICTIONARY_MEMBER.endScopeKeyword()}"
				continue
			}

			if (line.contains("includes")) {
				ret += "${IdlScope.INCLUDES.startScopeKeyword()} $line ${IdlScope.INCLUDES.endScopeKeyword()}"
				continue
			}

			if (line.endsWith(";")) ret += "$line ${IdlScope.ATTRIBUTE.endScopeKeyword()}"
			else ret += line
		}

		return ret.joinToString("\n")
	}


	private fun step60TrimPieces(data: String): String = mutableListOf<String>().apply { data.split(" ").forEach { piece -> if (piece.isNotEmpty() && piece.isNotBlank()) add(piece.trim().replace("\n", " "))} }.joinToString(" ")


	private fun step65AddModelStartAndEndScope(data: String): String = "${IdlScope.MODEL.startScopeKeyword()} $data ${IdlScope.MODEL.endScopeKeyword()}"


	private fun step70ToPieces(data: String): Pieces = Pieces(data).apply { Glob.pieces = this }
}