package com.github.jdw.funghi.parser


import Glob
import com.github.jdw.funghi.pieces.Scope
import com.github.jdw.funghi.model.IdlModel
import com.github.jdw.funghi.model.builders.IdlModelBuilder
import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.PiecesBuilder
import doch
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
			.step900CheckScopeSymmetries()
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
			.replace("{", "{\n ")
			.replace(" or ", " ${Scope.UNION_TYPE.nextScopeKeyword()} ")
	}


	private fun String.step50InsertStartOfScopeKeywordAndEndOfScopeKeywordAtTheRightPlaces(): String {
		val ret = mutableListOf<String>()

		var currentScope: Scope? = null
		val lines = this.split("\n")
		for (idx in lines.indices) {
			val lineProto = lines[idx]
				.trim()

			val line = if (lineProto.contains("sequence<") && lineProto.contains(">")) {
					lineProto
						.replace("sequence<", " ${Scope.SEQUENCE.startScopeKeyword()} ")
						.replace(">", " ${Scope.SEQUENCE.endScopeKeyword()} ")
				}
				else lineProto

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

			if (line.contains(" attribute ")) {
				var newLine = if (line.contains(" ${Scope.UNION_TYPE.nextScopeKeyword()} ")) {
						line
							.replace("(", " ${Scope.UNION_TYPE.startScopeKeyword()} ")
							.replace(")", " ${Scope.UNION_TYPE.endScopeKeyword()} ")
					}
					else line
					//.replace("attribute", "")

				if (line.endsWith(";")) {
					newLine = newLine.replace(";", "")
					ret += "${Scope.ATTRIBUTE.startScopeKeyword()} $newLine ${Scope.ATTRIBUTE.endScopeKeyword()}"
				}
				else {
					currentScope = Scope.ATTRIBUTE
					ret += "${Scope.ATTRIBUTE.startScopeKeyword()} $newLine"
				}

				continue
			}

			if (line.contains(" interface ")) {
				currentScope = Scope.INTERFACE
				ret += "${Scope.INTERFACE.startScopeKeyword()} $line"

				continue
			}

			if (line.contains(" dictionary ")) {
				currentScope = Scope.DICTIONARY
				ret += "${Scope.DICTIONARY.startScopeKeyword()} $line"

				continue
			}

			if (line.contains(" enum ")) {
				currentScope = Scope.ENUM

				val newLine = line.replace("enum", "")

				// Enum as a one-liner or defined on multiple lines
				if (line.contains("};")) ret += "${Scope.ENUM.startScopeKeyword()} $newLine ${Scope.ENUM.endScopeKeyword()}"
				else ret += "${Scope.ENUM.startScopeKeyword()} $newLine "

				continue
			}

			if (line.contains(" typedef ")) {
				var newLine = line
					.replace("typedef", "")
					.replace("(", " ( ")
					.replace(")", " ) ")

				newLine.contains(";")
					.doch {
						currentScope = Scope.TYPEDEF
						ret += "${Scope.TYPEDEF.startScopeKeyword()} $newLine "
					}
					.echt {
						newLine = newLine.replace(";", "")
						ret += "${Scope.TYPEDEF.startScopeKeyword()} $newLine ${Scope.TYPEDEF.endScopeKeyword()}"
					}

				continue
			}

			if (line.contains("constructor(")) {
				val newLine = if (line.contains("constructor();"))
					line // No arguments!
						.replace("constructor();", "constructor ${Scope.ARGUMENTS.startScopeKeyword()} ${Scope.ARGUMENTS.endScopeKeyword()} ")
				else line
					.replace("constructor(", "constructor ${Scope.ARGUMENTS.startScopeKeyword()} ${Scope.ARGUMENT.startScopeKeyword()} ")
					.replace(");", " ${Scope.ARGUMENT.endScopeKeyword()} ${Scope.ARGUMENTS.endScopeKeyword()} ")
					.replace(",", " ${Scope.ARGUMENT.nextScopeKeyword()} ")
					.replace("(", " ${Scope.UNION_TYPE.startScopeKeyword()} ")
					.replace(")", " ${Scope.UNION_TYPE.endScopeKeyword()} ")

				ret += "${Scope.OPERATION_CONSTRUCTOR.startScopeKeyword()} $newLine ${Scope.OPERATION_CONSTRUCTOR.endScopeKeyword()}"

				continue
			}

			if (line.contains("};")) {
				if (null == currentScope) throws()
				ret += when (currentScope) {
					Scope.DICTIONARY -> Scope.DICTIONARY.endScopeKeyword()
					Scope.INTERFACE -> Scope.INTERFACE.endScopeKeyword()
					Scope.ENUM -> line
						.replace("};", " ${Scope.ENUM.endScopeKeyword()} ")
						.replace("\",", "\" , ")
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
					var newLine =
						if (line.contains("();")) line.replace("();", " ${Scope.ARGUMENTS.startScopeKeyword()} ${Scope.ARGUMENTS.endScopeKeyword()} ")
						else {
							val newValue = values[0]
								.replaceFirst("(", " ${Scope.ARGUMENTS.startScopeKeyword()} ${Scope.ARGUMENT.startScopeKeyword()} ")
								//.replace(") ", " ${Scope.} ) ")
								.replace(");", " ${Scope.ARGUMENT.endScopeKeyword()} ${Scope.ARGUMENTS.endScopeKeyword()} ")
								.replace(",", " ${Scope.ARGUMENT.nextScopeKeyword()} ")

							line.replace(values[0], newValue)
						}

					newLine = newLine
						.replace("(", " ${Scope.UNION_TYPE.startScopeKeyword()} ")
						.replace(")", " ${Scope.UNION_TYPE.endScopeKeyword()} ")

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

			line.endsWith(";")
				.echt {
					//println("--- $line")
					when (currentScope) {
						Scope.ATTRIBUTE -> {
							ret += "${line.replace(";", "")} ${Scope.ATTRIBUTE.endScopeKeyword()}"
							currentScope = Scope.INTERFACE
						}
						Scope.TYPEDEF -> {
							ret += "${line.replace(";", "")} ${Scope.TYPEDEF.endScopeKeyword()}"
							currentScope = null
						}
						else -> {
							throws()
						}
					}
				}
				.doch { ret += line }
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


	private fun PiecesBuilder.step900CheckScopeSymmetries(): PiecesBuilder {
		val scopes = mutableListOf<Scope>()
		var lineNumber = ""

		pieces.forEach { piece ->
			if (piece.startsWith(Glob.lineNumberKeyword)) lineNumber = piece

			if (piece.startsWith(Glob.startScopeKeyword)) {
				val value = piece.replace(Glob.startScopeKeyword, "")
				val scope = Scope.valueOf(value)

				scopes.addFirst(scope)
			}
			else if (piece.startsWith(Glob.endScopeKeyword)) {
				val value = piece.replace(Glob.endScopeKeyword, "")
				val scope = Scope.valueOf(value)

				if (scopes.first() != scope) {
					println("scopes.first() = ${scopes.first()}")
					println("scope = $scope")
					println("lineNumber = $lineNumber")
					println("---")
					this.printAll()
					throws()
				}

				scopes.removeFirst()
			}
		}

		scopes.isNotEmpty() echt { throws() }

		return this
	}


	private fun PiecesBuilder.step1000PiecesBuilderToPieces(): Pieces = Pieces(this)
}