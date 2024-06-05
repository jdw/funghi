package com.github.jdw.funghi.pieces

import com.github.jdw.funghi.fragments.IdlScope
import doch
import echt
import genau
import noop
import removeArrayMarkers
import removeNullableMarkers
import throws

class Pieces(data: String) {
	private val pieces = data.split(" ").toMutableList()
	private val previousPieces = mutableListOf<String>()
	private val startedScopes = mutableListOf<IdlScope>()

	init {
		popUntilNotLineNumber()
	}


	override fun toString(): String {
		val ret = mutableListOf<String>()

		previousPieces
			.reversed()
			.forEach { piece -> ret.add(piece) }
		ret.add("\n--- Current piece --->")
		ret.add(pieces.first())
		ret.add("<--- Current piece ---\n")
		(1..11).forEach { idx ->
			ret.add(pieces[idx])
		}

		return ret.joinToString(" ")
	}


	private fun popUntilNotLineNumber() {
		while (pieces.first().startsWith(Glob.lineNumberKeyword)) {
			Glob.lineNumber = pieces.first()
			previousPieces.add(0, pieces.removeFirst())

			if (previousPieces.size > 10) previousPieces.removeLast()
		}
	}


	fun popStartScopeThrowIfNot(): IdlScope {
		popUntilNotLineNumber()

		val (_, value) = popIfFirstStartsWithThrowIfNot(Glob.startScopeKeyword)
		val enumValue = IdlScope.valueOf(value.replace(Glob.startScopeKeyword, ""))
		startedScopes.add(0, enumValue)

		return enumValue
	}


	infix fun popStartScopeThrow(doThrow: Boolean): IdlScope {
		popUntilNotLineNumber()

		val (result, value) = popIfFirstStartsWith(Glob.startScopeKeyword)

		result doch { doThrow echt { throws() } }

		val enumValue = IdlScope.valueOf(value.replace(Glob.startScopeKeyword, ""))
		startedScopes.add(0, enumValue)

		return enumValue
	}


	infix fun peekStartScopeThrow(doThrow: Boolean): IdlScope {
		popUntilNotLineNumber()

		val (result, value) = peekStartsWith(Glob.startScopeKeyword)

		result doch { doThrow echt { throws() } }

		val enumValue = IdlScope.valueOf(value.replace(Glob.startScopeKeyword, ""))

		return enumValue
	}


	infix fun peekStartsWith(value: String): Pair<Boolean, String> {
		popUntilNotLineNumber()

		val first = peek()

		return if (first.startsWith(value)) Pair(true, first)
			else Pair(false, first)
	}

	fun peek(): String {
		popUntilNotLineNumber()

		(pieces.isEmpty()) echt { throws() }

		return pieces.first()
	}


	infix fun popStartScopeThrowIfNot(scope: IdlScope) {
		popUntilNotLineNumber()

		val (_, value) = popIfFirstStartsWithThrowIfNot(Glob.startScopeKeyword)
		val foundScope = IdlScope.valueOf(value.replace(Glob.startScopeKeyword, ""))

		(foundScope == scope)
			.echt { startedScopes.add(0, foundScope) }
			.doch { throws() }
	}


	fun popEndScopeThrowIfNot(): IdlScope {
		popUntilNotLineNumber()

		val (_, value) = popIfFirstStartsWithThrowIfNot(Glob.endScopeKeyword)
		val enumValue = IdlScope.valueOf(value.replace(Glob.endScopeKeyword, ""))

		(enumValue == startedScopes.first())
			.echt { startedScopes.removeFirst() }
			.doch { throws() }

		return enumValue
	}


	infix fun popEndScopeThrowIfNot(scope: IdlScope) {
		popUntilNotLineNumber()

		val (_, value) = popIfFirstStartsWithThrowIfNot(Glob.endScopeKeyword)
		val enumValue = IdlScope.valueOf(value.replace(Glob.endScopeKeyword, ""))

		(enumValue == startedScopes.first() && enumValue == scope)
			.echt { startedScopes.removeFirst() }
			.doch { throws() }
	}


	infix fun popIfPresent(value: String): Boolean {
		popUntilNotLineNumber()

		return if (value == pieces.first()) {
			previousPieces.add(0, pieces.removeFirst())
			if (previousPieces.size > 10) previousPieces.removeLast()

			true
		}
		else {
			false
		}
	}


	infix fun popIfPresent(value: Regex): Boolean {
		popUntilNotLineNumber()

		return if (peek() matches value) {
			pop()
			true
		}
		else {
			false
		}
	}


	infix fun popIfPresentThrowIfNot(value: Regex): String {
		popUntilNotLineNumber()

		return if (peek() matches value) {
			pop()
		}
		else {
			throws()
		}
	}


	fun popIfFirstStartsWith(value: String): Pair<Boolean, String> {
		popUntilNotLineNumber()

		if (pieces.first().startsWith(value)) {
			val ret = Pair(true, pieces.first())
			previousPieces.add(0, pieces.removeFirst())

			if (previousPieces.size > 10) previousPieces.removeLast()

			return ret
		}

		return Pair(false, "")
	}


	fun popIfFirstStartsWithThrowIfNot(value: String): Pair<Boolean, String> {
		popUntilNotLineNumber()

		val ret = popIfFirstStartsWith(value)

		ret.first.doch { throws() }

		return ret
	}


	infix fun popIfPresentThrowIfNot(value: String) {
		popUntilNotLineNumber()

		popIfPresent(value)
			.doch { throws("First was not '$value'!") }
	}


	fun peekEndScope(): Boolean {
		popUntilNotLineNumber()

		return pieces.first().startsWith(Glob.endScopeKeyword)
	}


	fun peekIsStartScope(): Boolean {
		popUntilNotLineNumber()

		return pieces.first().startsWith(Glob.startScopeKeyword)
	}


	fun pop(): String {
		popUntilNotLineNumber()

		val ret = pieces.removeFirst()
		previousPieces.add(0, ret)
		if (previousPieces.size > 10) previousPieces.removeLast()

		return ret
	}


	infix fun pop(count: Int): String {
		popUntilNotLineNumber()

		var ret = ""

		if (0 == count) throws()

		(0..<count).forEach { _ ->
			ret += "${pieces.removeFirst()} "
		}

		return ret.removeSuffix(" ")
	}


	fun peekIsPresentSingleType(): Boolean {
		popUntilNotLineNumber()

		val threePiece = pieces
			.subList(0, 2)
			.joinToString(" ")
			.removeArrayMarkers()
			.removeNullableMarkers()
		if (Glob.parserSettings!!.allPredefinedTypesKeywords().contains(threePiece)) return true

		val twoPiece = pieces
			.subList(0, 1)
			.joinToString(" ")
			.removeArrayMarkers()
			.removeNullableMarkers()
		if (Glob.parserSettings!!.allPredefinedTypesKeywords().contains(twoPiece)) return true

		val onePiece = peek()
			.removeArrayMarkers()
			.removeNullableMarkers()
		if (Glob.parserSettings!!.allPredefinedTypesKeywords().contains(onePiece)) return true

		return Glob.parserSettings!!.complexTypesRegex() matches onePiece
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
				val ret = this pop 3

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
				val ret = this pop 2

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
				val ret = this pop 1

				return Pair(genau, ret)
			}
		}
		catch (_: IndexOutOfBoundsException) {
			noop()
		}

		if (Glob.parserSettings!!.complexTypesRegex() matches onePiece) {
			val ret = this pop 1
			return Pair(true, ret)
		}

		return Pair(false, "")
	}


	fun popIfPresentSingleTypeThrowIfNot(): Pair<Boolean, String> {
		popUntilNotLineNumber()

		val ret = popIfPresentSingleType()

		ret.first doch { throws() }

		return ret
	}
}