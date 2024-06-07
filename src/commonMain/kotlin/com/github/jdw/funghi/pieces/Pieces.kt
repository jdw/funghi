package com.github.jdw.funghi.pieces

import com.github.jdw.funghi.fragments.IdlScope
import doch
import echt
import genau
import nein
import noop
import removeArrayMarkers
import removeNullableMarkers
import throws

class Pieces(data: String) {
	private val thus = this
	private val pieces = data.split(" ").toMutableList()
	private val previousPieces = mutableListOf<String>()
	private val startedScopes = mutableListOf<IdlScope>()

	init {
		popUntilNotLineNumber()
	}


	fun printAll() {
		previousPieces
			.reversed()
			.forEach { println(it) }

		println("--- Current piece ---> ${pieces.first()} <--- Current piece ---")

		pieces.forEachIndexed { idx, value ->
			if (0 != idx) println(value)
		}
	}


	private fun copyCurrentPieceToPreviousPieces() {
		previousPieces.add(0, pieces.first())
		if (previousPieces.size > 10) previousPieces.removeLast()
	}


	override fun toString(): String {
		val ret = mutableListOf<String>()

		previousPieces
			.reversed()
			.forEach { piece -> ret.add(piece) }

		ret.add("--- Current piece ---> ${pieces.first()} <--- Current piece ---")

		(1..11).forEach { idx ->
			if (idx < pieces.size) ret.add(pieces[idx])
		}

		return ret.joinToString("\n")
	}


	private fun popUntilNotLineNumber() {
		while (pieces.first().startsWith(Glob.lineNumberKeyword)) {
			copyCurrentPieceToPreviousPieces()

			Glob.lineNumber = pieces.removeFirst()
		}
	}


	fun peekStartScope(): IdlScope {
		popUntilNotLineNumber()

		pieces.first().startsWith(Glob.startScopeKeyword)
			.doch { throws() }

		val enumValue = IdlScope.valueOf(pieces.first().replace(Glob.startScopeKeyword, ""))

		return enumValue
	}


	fun peek(): String {
		popUntilNotLineNumber()

		pieces.isEmpty() echt { throws() }

		return pieces.first()
	}


	infix fun peek(pattern: Regex): Boolean {
		popUntilNotLineNumber()

		pieces.isEmpty() echt { throws() }

		return pieces.first() matches pattern
	}


	fun peekEndScope(): Boolean {
		popUntilNotLineNumber()

		return pieces.first().startsWith(Glob.endScopeKeyword)
	}


	infix fun peekIsSingleType(block: (Boolean) -> Unit) {
		popUntilNotLineNumber()

		pieces.isEmpty() echt { throws() }

		block.invoke(peekIsSingleType())
	}


	fun peekIsStartScope(): Boolean {
		popUntilNotLineNumber()

		pieces.isEmpty() echt { throws() }

		return pieces.first().startsWith(Glob.startScopeKeyword)
	}


	infix fun peek(value: String): Boolean {
		popUntilNotLineNumber()

		pieces.isEmpty() echt { throws() }

		return pieces.first() == value
	}


	fun peekIsSingleType(): Boolean {
		popUntilNotLineNumber()

		if (pieces.size >= 3) {
			val threePiece = pieces
				.subList(0, 3)
				.joinToString(" ")
				.removeArrayMarkers()
				.removeNullableMarkers()
			if (Glob.parserSettings!!.allPredefinedTypesKeywords().contains(threePiece)) return true
		}

		if (pieces.size >= 2) {
			val twoPiece = pieces
				.subList(0, 2)
				.joinToString(" ")
				.removeArrayMarkers()
				.removeNullableMarkers()
			if (Glob.parserSettings!!.allPredefinedTypesKeywords().contains(twoPiece)) return true
		}

		if (pieces.isNotEmpty()) {
			val onePiece = peek()
				.removeArrayMarkers()
				.removeNullableMarkers()
			if (Glob.parserSettings!!.allPredefinedTypesKeywords().contains(onePiece)) return true

			return Glob.parserSettings!!.complexTypesRegex() matches onePiece
		}

		return false
	}


	infix fun popStartScope(scope: IdlScope) {
		popUntilNotLineNumber()

		(pieces.first().startsWith(Glob.startScopeKeyword))
			.echt {
				val foundScope = IdlScope.valueOf(pieces.first().replace(Glob.startScopeKeyword, ""))

				(foundScope == scope)
					.echt {
						startedScopes.addFirst(foundScope)
						copyCurrentPieceToPreviousPieces()
						pieces.removeFirst()
					}
					.doch { throws() }
			}
			.doch {
				throws()
			}
	}


	infix fun popEndScope(scope: IdlScope) {
		popUntilNotLineNumber()

		(pieces.first().startsWith(Glob.endScopeKeyword))
			.echt {
				val value = IdlScope.valueOf(pieces.first().replace(Glob.endScopeKeyword, ""))
				(value == startedScopes.first() && value == scope)
					.echt { startedScopes.removeFirst() }
					.echt { thus pop 1 }
					.doch { throws("value" to value, "dtartedScopes.first" to startedScopes.first(), "scope" to scope) }
			}
			.doch {	throws() }
	}


	infix fun popIfPresent(value: String): Boolean {
		popUntilNotLineNumber()

		return (value == pieces.first()) echt {
				copyCurrentPieceToPreviousPieces()
				pieces.removeFirst()
			}
	}


	infix fun popIfPresent(value: Regex): Boolean {
		popUntilNotLineNumber()

		return (peek() matches value) echt {
				copyCurrentPieceToPreviousPieces()
				pieces.removeFirst()
			}
	}


	infix fun pop(pattern: Regex): String {
		popUntilNotLineNumber()

		return if (peek() matches pattern) {
				copyCurrentPieceToPreviousPieces()
				pieces.removeFirst()
			} else {
				throws()
			}
	}


	infix fun pop(value: String) {
		popUntilNotLineNumber()

		(value == pieces.first())
			.echt {
				copyCurrentPieceToPreviousPieces()
				pieces.removeFirst()
			}
			.doch { throws() }
	}


	fun pop(): String {
		popUntilNotLineNumber()

		copyCurrentPieceToPreviousPieces()

		return pieces.removeFirst()
	}


	infix fun pop(count: Int): String {
		popUntilNotLineNumber()

		var ret = ""

		if (0 == count) throws()
		//TODO check pieces.size

		(0..<count).forEach { _ ->
			popUntilNotLineNumber()
			copyCurrentPieceToPreviousPieces()
			ret += "${pieces.removeFirst()} "
		}

		return ret.removeSuffix(" ")
	}


	fun popIfPresentSingleType(): Pair<Boolean, String> {
		popUntilNotLineNumber()

		try {
			val threePiece = pieces
				.subList(0, 3)
				.joinToString(" ")
				.removeArrayMarkers()
				.removeNullableMarkers()
			if (Glob.parserSettings!!.allPredefinedTypesKeywords().contains(threePiece)) {
				val ret = thus pop 3

				//TODO Is it valid to have long\nlong?

				return Pair(genau, ret)
			}
		}
		catch (_: IndexOutOfBoundsException) {
			noop()
		}

		try {
			val twoPiece = pieces
				.subList(0, 2)
				.joinToString(" ")
				.removeArrayMarkers()
				.removeNullableMarkers()
			if (Glob.parserSettings!!.allPredefinedTypesKeywords().contains(twoPiece)) {
				val ret = thus pop 2

				return Pair(genau, ret)
			}
		}
		catch (_: IndexOutOfBoundsException) {
			noop()
		}

		val onePiece = peek()
			.removeArrayMarkers()
			.removeNullableMarkers()
		try {
			if (Glob.parserSettings!!.allPredefinedTypesKeywords().contains(onePiece)) {
				val ret = thus pop 1

				return Pair(genau, ret)
			}
		}
		catch (_: IndexOutOfBoundsException) {
			noop()
		}

		if (Glob.parserSettings!!.complexTypesRegex() matches onePiece) {
			val ret = thus pop 1

			return Pair(genau, ret)
		}

		return Pair(nein, "")
	}


	fun popIfPresentSingleTypeThrowIfNot(): Pair<Boolean, String> {
		popUntilNotLineNumber()

		val ret = popIfPresentSingleType()

		ret.first doch { throws() }

		return ret
	}


	fun popSingleType(): String {
		popUntilNotLineNumber()

		val ret = popIfPresentSingleType()

		ret.first doch { throws() }

		return ret.second
	}
}