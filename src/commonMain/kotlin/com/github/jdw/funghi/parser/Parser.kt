package com.github.jdw.funghi.parser


import Glob
import com.github.jdw.funghi.model.IdlModel
import com.github.jdw.funghi.model.builders.IdlModelBuilder
import com.github.jdw.funghi.pieces.Pieces
import com.github.jdw.funghi.pieces.PiecesBuilder
import com.github.jdw.funghi.pieces.Scope
import doch
import echt
import noop
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
			.step51InsertStartOfScopeKeywordAndEndOfScopeKeywordAtTheRightPlacesForDictionaries()
			.step60TrimPieces()
			.step65AddModelStartAndStopScope()
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
			.replace("= [ ] ,", "= ${Glob.emptyArrayKeyword} ,")
			.replace("= {},", "= ${Glob.emptyDictionaryKeyword} ,")
			.replace("= [];", "= ${Glob.emptyArrayKeyword} ;")
			.replace("= {};", "= ${Glob.emptyDictionaryKeyword} ;")
			.replace("= [])", "= ${Glob.emptyArrayKeyword} )")
			.replace("= {})", "= ${Glob.emptyDictionaryKeyword} )")
			.replace("= \"\",", "= ${Glob.emptyStringKeyword} , ")
			.replace("= \"\")", "= ${Glob.emptyStringKeyword} ) ")
			.replace("= \"\";", "= ${Glob.emptyStringKeyword} ; ")
			.replace("= null,", "= ${Glob.nullKeyword} , ")
			.replace("= null)", "= ${Glob.nullKeyword} ) ")
			.replace("= null;", "= ${Glob.nullKeyword} ; ")

		//TODO Check if this contains any of [], {} or null
		//TODO Use regexps instead?
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
						.replace(">", " ${Scope.SEQUENCE.stopScopeKeyword()} ")
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
					.replace("]", " ${Scope.EXTENDED_ATTRIBUTE.stopScopeKeyword()} ")

				continue
			}

			if (line.contains(" attribute ")) {
				var newLine = if (line.contains(" ${Scope.UNION_TYPE.nextScopeKeyword()} ")) {
						line
							.replace("(", " ${Scope.UNION_TYPE.startScopeKeyword()} ")
							.replace(")", " ${Scope.UNION_TYPE.stopScopeKeyword()} ")
					}
					else line
					//.replace("attribute", "")

				if (line.endsWith(";")) {
					newLine = newLine.replace(";", "")
					ret += "${Scope.ATTRIBUTE.startScopeKeyword()} $newLine ${Scope.ATTRIBUTE.stopScopeKeyword()}"
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
				ret += " ${Scope.DICTIONARY.startScopeKeyword()} $line "

				continue
			}

			if (line.contains(" enum ")) {
				currentScope = Scope.ENUM

				val newLine = line.replace("enum", "")

				// Enum as a one-liner or defined on multiple lines
				if (line.contains("};")) ret += "${Scope.ENUM.startScopeKeyword()} $newLine ${Scope.ENUM.stopScopeKeyword()}"
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
						ret += "${Scope.TYPEDEF.startScopeKeyword()} $newLine ${Scope.TYPEDEF.stopScopeKeyword()}"
					}

				continue
			}

			if (line.contains("constructor(")) {
				if (currentScope != Scope.INTERFACE) throws()

				val newLine = if (line.contains("constructor();"))
					line // No arguments!
						.replace("constructor();", "constructor ${Scope.ARGUMENTS.startScopeKeyword()} ${Scope.ARGUMENTS.stopScopeKeyword()} ")
				else line
					.replace("constructor(", "constructor ${Scope.ARGUMENTS.startScopeKeyword()} ${Scope.ARGUMENT.startScopeKeyword()} ")
					.replace(");", " ${Scope.ARGUMENT.stopScopeKeyword()} ${Scope.ARGUMENTS.stopScopeKeyword()} ")
					.replace(",", " ${Scope.ARGUMENT.nextScopeKeyword()} ")
					.replace("(", " ${Scope.UNION_TYPE.startScopeKeyword()} ")
					.replace(")", " ${Scope.UNION_TYPE.stopScopeKeyword()} ")

				ret += "${Scope.CONSTRUCTOR.startScopeKeyword()} $newLine ${Scope.CONSTRUCTOR.stopScopeKeyword()}"

				continue
			}


			// Special operations
			val operationsValues = Glob
				.parserSettings!!
				.operationRegex()
				.find(line)
				?.groupValues
				?: emptyList()
			if ((line.contains("getter") || line.contains("setter") || line.contains("deleter")) &&
				operationsValues.isEmpty()
				//!line.matches(Glob.parserSettings!!.operationRegex())
				//line.contains(" (")
				) {
				if (currentScope != Scope.INTERFACE) throws()
				if (line.contains("...")) throws() // SpecOps can not be variadic
				if (line.contains("optional")) throws() // SpecOps can have optional arguments
				if (line.split("(").size != 2) throws() // SpecOps can not have union types //TODO Find out if true

				val newLine = line
					.replace(";", "")
					.replace("(", " ${Scope.ARGUMENTS.startScopeKeyword()} ${Scope.ARGUMENT.startScopeKeyword()} ")
					.replace(")", " ${Scope.ARGUMENT.stopScopeKeyword()} ${Scope.ARGUMENTS.stopScopeKeyword()} ")
					.replace(",", " ${Scope.ARGUMENT.nextScopeKeyword()} ")
				ret += "${Scope.SPECIAL_OPERATION.startScopeKeyword()} $newLine ${Scope.SPECIAL_OPERATION.stopScopeKeyword()}"

				continue
			}

			if (line.contains("};")) { //TODO should be == "};"
				if (null == currentScope) throws()

				ret += when (currentScope) {
					Scope.DICTIONARY -> Scope.DICTIONARY.stopScopeKeyword()
					Scope.INTERFACE -> Scope.INTERFACE.stopScopeKeyword()
					Scope.ENUM -> line
						.replace("};", " ${Scope.ENUM.stopScopeKeyword()} ")
						.replace("\",", "\" , ")
					else -> throws()
				}

				currentScope = null

				continue
			}

			if (line.contains("(") && //TODO matches operationRegex
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
						if (line.contains("();")) line.replace("();", " ${Scope.ARGUMENTS.startScopeKeyword()} ${Scope.ARGUMENTS.stopScopeKeyword()} ")
						else {
							val newValue = values[0]
								.replaceFirst("(", " ${Scope.ARGUMENTS.startScopeKeyword()} ${Scope.ARGUMENT.startScopeKeyword()} ")
								//.replace(") ", " ${Scope.} ) ")
								.replace(");", " ${Scope.ARGUMENT.stopScopeKeyword()} ${Scope.ARGUMENTS.stopScopeKeyword()} ")
								.replace(",", " ${Scope.ARGUMENT.nextScopeKeyword()} ")

							line.replace(values[0], newValue)
						}

					newLine = newLine
						.replace("(", " ${Scope.UNION_TYPE.startScopeKeyword()} ")
						.replace(")", " ${Scope.UNION_TYPE.stopScopeKeyword()} ")

					ret += "${Scope.OPERATION.startScopeKeyword()} $newLine ${Scope.OPERATION.stopScopeKeyword()}"

					continue
				}
			}

			if (line.contains("const") && line.contains("=") && currentScope == Scope.INTERFACE) {
				val newLine = line
					.replace("const", "")
					.replace("=", "")
					.replace(";", " ;")

				ret += "${Scope.CONSTANT.startScopeKeyword()} $newLine ${Scope.CONSTANT.stopScopeKeyword()}"

				continue
			}

			if (line.contains("includes")) {
				ret += "${Scope.INCLUDES.startScopeKeyword()} $line ${Scope.INCLUDES.stopScopeKeyword()}"

				continue
			}

			line.endsWith(";")
				.echt {
					when (currentScope) {
						Scope.ATTRIBUTE -> {
							ret += "${line.replace(";", "")} ${Scope.ATTRIBUTE.stopScopeKeyword()}"
							currentScope = Scope.INTERFACE
						}
						Scope.TYPEDEF -> {
							ret += "${line.replace(";", "")} ${Scope.TYPEDEF.stopScopeKeyword()}"
							currentScope = null
						}
						Scope.DICTIONARY -> noop()
						else -> {
							println("--- scope = $currentScope")
							throws()
						}
					}
				}
				.doch { ret += line }
		}

		return ret.joinToString("\n")
	}


	private fun String.step51InsertStartOfScopeKeywordAndEndOfScopeKeywordAtTheRightPlacesForDictionaries(): String {
		val ret = mutableListOf<String>()

		var currentScope: Scope? = null
		val lines = this.split("\n")
		for (idx in lines.indices) {
			val line = lines[idx].trim()

			if (line.isEmpty()) continue

			if (currentScope != Scope.DICTIONARY) {
				ret += line

				continue
			}

			if (line.contains(Scope.DICTIONARY.startScopeKeyword())) {
				currentScope = Scope.DICTIONARY
				ret += line

				continue
			}

			if (line == Scope.DICTIONARY.stopScopeKeyword()) {
				when (currentScope) {
					Scope.DICTIONARY -> noop()
					else -> throws()
				}

				ret += line

				continue
			}

			ret += " ${Scope.MEMBER.startScopeKeyword()} $line ${Scope.MEMBER.stopScopeKeyword()}"
		}

		return ret.joinToString("\n")
	}


	private fun String.step60TrimPieces(): String {
		val data = this
		return mutableListOf<String>().apply { data.split(" ").forEach { piece -> if (piece.isNotEmpty() && piece.isNotBlank()) add(piece.trim().replace("\n", " "))} }.joinToString(" ")
	}


	private fun String.step65AddModelStartAndStopScope(): String = "${Scope.MODEL.startScopeKeyword()} $this ${Scope.MODEL.stopScopeKeyword()}"


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

				thus push Scope.ARGUMENT.stopScopeKeyword()

				//TODO peek(",")
			}
			else if (peek("(")) {
				thus replace Scope.LIST.startScopeKeyword()

				thus forwardUntil(")")

				thus replace Scope.LIST.stopScopeKeyword()
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
			else if (piece.startsWith(Glob.stopScopeKeyword)) {
				val value = piece.replace(Glob.stopScopeKeyword, "")
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