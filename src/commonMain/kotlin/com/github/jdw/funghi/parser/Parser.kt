package com.github.jdw.funghi.parser


import Glob
import com.github.jdw.funghi.pieces.Scope
import com.github.jdw.funghi.model.IdlModel
import com.github.jdw.funghi.model.builders.IdlModelBuilder
import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.PiecesBuilder
import echt
import throws


internal class Parser(val settings: ParserSettings, private val filename: String) {
	infix fun parse(data: String): IdlModel {
		Glob.parserSettings = settings
		Glob.filename = filename

		val pieces = data
			.step05AddLineNumbers()
			.step10RemoveLineComments()
			.step20RemoveBlockComments()
			.step25EmptyArrayAndEmptyDictionaryToKeywords()
			.step30RemoveAllWhitespacesExceptSpaceChar()
			.step40InsertNewLineAtTheRightPlaces()
			.step50InsertStartOfScopeKeywordAndEndOfScopeKeywordAtTheRightPlaces()
			.step60TrimPieces()
			.step65AddModelStartAndEndScope()
			.step70ToPiecesBuilder()
			.step300EnhanceExtendedAttributes()
			//.step900CheckScopeSymmetries()
			.step1000PiecesBuilderToPieces()

		return IdlModel(IdlModelBuilder().apply { this puzzle pieces })
	}


	private fun String.step05AddLineNumbers(): String {
		val ret = mutableListOf<String>()
		var lineNumber = 1
		this
			.split("\n")
			.forEach { lineRaw ->
				val line = lineRaw.trim()
				ret += "${Glob.lineNumberKeyword}$lineNumber $line"
				lineNumber++
			}

		return ret.joinToString("\n")
	}


	private fun String.step10RemoveLineComments(): String {
		val ret = mutableListOf<String>()

		this
			.split("\n")
			.forEach { line ->
				ret += if (line.contains("//")) line.split("//").first()
				else line
			}

		return ret.joinToString(" ")
	}


	private fun String.step20RemoveBlockComments(): String {
		val ret = mutableListOf<String>()

		var weAreInABlockComment = false
		this
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


	private fun String.step25EmptyArrayAndEmptyDictionaryToKeywords(): String {
		return this
			.replace("= [],", "= ${Glob.emptyArrayKeyword} ,")
			.replace("= {},", "= ${Glob.emptyDictionaryKeyword} ,")
			.replace("= [];", "= ${Glob.emptyArrayKeyword} ;")
			.replace("= {};", "= ${Glob.emptyDictionaryKeyword} ;")
			.replace("= [])", "= ${Glob.emptyArrayKeyword} )")
			.replace("= {})", "= ${Glob.emptyDictionaryKeyword} )")

	}


	private fun String.step30RemoveAllWhitespacesExceptSpaceChar(): String {
		val ret = mutableListOf<String>()

		this
			.split(" ")
			.forEach { line ->
				line
					.trim()
					.apply { if ("" != this) ret += this }
			}

		return ret.joinToString(" ")
	}


	private fun String.step40InsertNewLineAtTheRightPlaces(): String {
		return this
			.replace("[", "\n[")
			.replace("]", "]\n")
			.replace(";", ";\n")
			.replace("{ ", "{\n ")
	}


	private fun String.step50InsertStartOfScopeKeywordAndEndOfScopeKeywordAtTheRightPlaces(): String {
		val ret = mutableListOf<String>()

		var currentScope: Scope? = null
		val lines = this.split("\n")
		for (idx in lines.indices) {
			val line = lines[idx].trim()
			if (line.contains("[") && line.contains("]")) {
				var newLine = line

				Glob.parserSettings!!.extendedAttributesWildcardMarkers().forEach { marker ->
					(newLine.contains("=$marker"))
						.echt { newLine = newLine.replace("=$marker", " = ${Glob.extendedAttributeWildcardKeyword} ") }

					(newLine.contains("= $marker"))
						.echt { newLine = newLine.replace("= $marker", " = ${Glob.extendedAttributeWildcardKeyword} ") }


				}

				ret += newLine
					.replace(")", " ) ")
					.replace("(", " ( ")
					.replace(",", " , ") //TODO Remove comma for multiple EAs
					.replace("=", " = ")
					.replace("[", " ${Scope.EXTENDED_ATTRIBUTE.startScopeKeyword()} ")
					.replace("]", " ${Scope.EXTENDED_ATTRIBUTE.endScopeKeyword()} ")

				continue
			}

			if (line.contains("attribute")) {
				val newLine = line
					//.replace("attribute", "")
					.replace(";", " ;")
				if (line.endsWith(";")) ret += "${Scope.ATTRIBUTE.startScopeKeyword()} $newLine ${Scope.ATTRIBUTE.endScopeKeyword()}"
				else ret += "${Scope.ATTRIBUTE.startScopeKeyword()} $line"

				continue
			}

			if (line.contains("interface")) {
				currentScope = Scope.INTERFACE
				ret += "${Scope.INTERFACE.startScopeKeyword()} $line"

				continue
			}

			if (line.contains("dictionary")) {
				currentScope = Scope.DICTIONARY
				ret += "${Scope.DICTIONARY.startScopeKeyword()} $line"

				continue
			}

			if (line.contains("enum")) {
				ret += "${Scope.ENUM.startScopeKeyword()} $line ${Scope.ENUM.endScopeKeyword()}"

				continue
			}

			if (line.contains("typedef")) {
				ret += "${Scope.TYPEDEF.startScopeKeyword()} $line ${Scope.TYPEDEF.endScopeKeyword()}"

				continue
			}

			if (line.contains("constructor(")) {
				val newLine = if (line.contains("constructor();")) line // No arguments!
					.replace("constructor();", "constructor ( );")
				else line
					.replace("constructor(", "constructor ( ${Scope.ARGUMENT.startScopeKeyword()} ")
					.replace(");", " ${Scope.ARGUMENT.endScopeKeyword()} );")
					.replace(",", " ${Scope.ARGUMENT.endScopeKeyword()} , ${Scope.ARGUMENT.startScopeKeyword()} ")

				ret += "${Scope.OPERATION_CONSTRUCTOR.startScopeKeyword()} $newLine ${Scope.OPERATION_CONSTRUCTOR.endScopeKeyword()}"

				continue
			}

			if (line.contains("};")) {
				if (null == currentScope) throws()
				ret += when (currentScope) {
					Scope.DICTIONARY -> Scope.DICTIONARY.endScopeKeyword()
					Scope.INTERFACE -> Scope.INTERFACE.endScopeKeyword()
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
				ret += "${Scope.OPERATION.startScopeKeyword()} $newLine ${Scope.OPERATION.endScopeKeyword()}"

				continue
			}

			if (line.contains("(") &&
				!line.contains("constructor") &&
				currentScope == Scope.INTERFACE) {
				val values = Glob
					.parserSettings!!
					.operationRegex()
					.find(line)
					?.groupValues
					?: emptyList()

				if (values.isNotEmpty()) {
					val newLine =
						if (line.contains("();")) line.replace("();", " ( );")
						else line
								.replace("(", " ( ${Scope.ARGUMENT.startScopeKeyword()} ")
								//.replace(")", " )")
								.replace(");", " ${Scope.ARGUMENT.endScopeKeyword()} );")
								.replace(",", " ${Scope.ARGUMENT.endScopeKeyword()} , ${Scope.ARGUMENT.startScopeKeyword()} ")
					ret += "${Scope.OPERATION.startScopeKeyword()} $newLine ${Scope.OPERATION.endScopeKeyword()}"

					continue
				}
			}

			if (line.contains("=")) {
				if (line.contains("const")) ret += "${Scope.CONST_ATTRIBUTE.startScopeKeyword()} $line ${Scope.CONST_ATTRIBUTE.endScopeKeyword()}"
				else ret += "${Scope.DICTIONARY_MEMBER.startScopeKeyword()} $line ${Scope.DICTIONARY_MEMBER.endScopeKeyword()}"

				continue
			}

			if (line.contains("includes")) {
				ret += "${Scope.INCLUDES.startScopeKeyword()} $line ${Scope.INCLUDES.endScopeKeyword()}"

				continue
			}

			if (line.endsWith(";")) ret += "$line ${Scope.ATTRIBUTE.endScopeKeyword()}"
			else ret += line
		}

		return ret.joinToString("\n")
	}


	private fun String.step60TrimPieces(): String {
		val data = this
		return mutableListOf<String>().apply { data.split(" ").forEach { piece -> if (piece.isNotEmpty() && piece.isNotBlank()) add(piece.trim().replace("\n", " "))} }.joinToString(" ")
	}


	private fun String.step65AddModelStartAndEndScope(): String = "${Scope.MODEL.startScopeKeyword()} $this ${Scope.MODEL.endScopeKeyword()}"


	private fun String.step70ToPiecesBuilder(): PiecesBuilder = PiecesBuilder(this)


	private fun PiecesBuilder.step300EnhanceExtendedAttributes(): PiecesBuilder {
		val thus = this

		while (currentIdx < pieces.size - 1) {
			thus forward 1

			if (!peek(Scope.EXTENDED_ATTRIBUTE.startScopeKeyword())) continue

			thus forward 2

			if (!peek("=")) continue

			thus forward 1

			if (peek(Glob.parserSettings!!.identifierRegex())) {
				thus forward 1

				if (!peek("(")) continue

				thus forward 1

				push(Scope.ARGUMENT.startScopeKeyword())

				thus forward 3

				thus push Scope.ARGUMENT.endScopeKeyword()

				//TODO peek(",")
			}
			else if (peek("(")) {
				thus replace Scope.EXTENDED_ATTRIBUTE_LIST.startScopeKeyword()

				thus forwardUntil(")")

				thus replace Scope.EXTENDED_ATTRIBUTE_LIST.endScopeKeyword()
			}
		}

		return thus
	}


	private fun PiecesBuilder.step1000PiecesBuilderToPieces(): Pieces = Pieces(this)
}