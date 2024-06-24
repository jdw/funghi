package com.github.jdw.funghi.pieces

import doch
import echt
import genau
import nein
import noop
import removeArrayMarkers
import removeNullableMarkers
import throws

open class PiecesHolder {
	var currentIdx = 0
	open val pieces = listOf<String>()
	private val thus = this

	protected fun takeAStep() {
		currentIdx++
	}


	fun printAll() {
		pieces.forEachIndexed { idx, piece ->
			(idx == currentIdx)
				.echt { println("--- Current piece ---> $piece <--- Current piece ---") }
				.doch { println(piece) }
		}
	}


	override fun toString(): String {
		val ret = mutableListOf<String>()

		val backwards = currentIdx - 10
		(backwards..currentIdx).forEach { idx ->
			(idx >= 0 && idx != currentIdx) echt { ret.add(pieces[idx]) }
		}

		ret.add("--- Current piece ---> ${pieces[currentIdx]} <--- Current piece ---")

		(currentIdx + 1..currentIdx + 11).forEach { idx ->
			if (idx < pieces.size) ret.add(pieces[idx])
		}

		return ret.joinToString("\n")
	}


	fun peek(): String {
		popUntilNotLineNumber()

		pieces.isEmpty() echt { throws() }
		(pieces.size < currentIdx) echt { throws() }

		return pieces[currentIdx]
	}


	infix fun peek(pattern: Regex): Boolean {
		popUntilNotLineNumber()

		pieces.isEmpty() echt { throws() }
		(pieces.size < currentIdx) echt { throws() }

		return pieces[currentIdx] matches pattern
	}


	infix fun peek(value: String): Boolean {
		popUntilNotLineNumber()

		pieces.isEmpty() echt { throws() }
		(pieces.size < currentIdx) echt { throws() }

		return pieces[currentIdx] == value
	}


	fun peekEndScope(): Boolean {
		popUntilNotLineNumber()

		return pieces[currentIdx].startsWith(Glob.endScopeKeyword)
	}


	fun peekIsSingleType(): Boolean {
		popUntilNotLineNumber()

		if (pieces.size >= currentIdx + 3) {
			val threePiece = pieces
				.subList(currentIdx, currentIdx + 3)
				.joinToString(" ")
				.removeArrayMarkers()
				.removeNullableMarkers()
			if (Glob.parserSettings!!.allPredefinedTypesKeywords().contains(threePiece)) return true
		}

		if (pieces.size >= currentIdx + 2) {
			val twoPiece = pieces
				.subList(currentIdx, currentIdx + 2)
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


	infix fun peekIsSingleType(block: (Boolean) -> Unit) {
		popUntilNotLineNumber()

		pieces.isEmpty() echt { throws() }
		(pieces.size < currentIdx) echt { throws() }

		block.invoke(peekIsSingleType())
	}


	fun peekIsStartScope(): Boolean {
		popUntilNotLineNumber()

		pieces.isEmpty() echt { throws() }
		(pieces.size < currentIdx) echt { throws() }

		return pieces[currentIdx].startsWith(Glob.startScopeKeyword)
	}


	fun peekStartScope(): Scope {
		popUntilNotLineNumber()

		pieces.isEmpty() echt { throws() }
		(pieces.size < currentIdx) echt { throws() }

		pieces[currentIdx].startsWith(Glob.startScopeKeyword) doch { throws() }

		val enumValue = Scope.valueOf(pieces[currentIdx].replace(Glob.startScopeKeyword, ""))

		return enumValue
	}


	fun pop(): String {
		popUntilNotLineNumber()

		val ret = pieces[currentIdx]
		takeAStep()

		return ret
	}


	infix fun pop(pattern: Regex): String {
		popUntilNotLineNumber()

		var ret = ""
		(pieces[currentIdx] matches pattern)
			.echt {
				ret = pieces[currentIdx]
				takeAStep()
			}
			.doch {
				throws()
			}

		return ret
	}


	infix fun pop(value: String) {
		popUntilNotLineNumber()

		(value == pieces[currentIdx])
			.echt {
				takeAStep()
			}
			.doch {
				throws()
			}
	}


	infix fun pop(count: Int): String {
		popUntilNotLineNumber()

		var ret = ""

		(0 == count) echt { throws() }
		(pieces.size > currentIdx + count - 1) doch { throws() }

		(0..<count).forEach { _ ->
			popUntilNotLineNumber()
			ret += "${pieces[currentIdx]} "
			takeAStep()
		}

		return ret.removeSuffix(" ")
	}


	infix fun popEndScope(scope: Scope) {
		popUntilNotLineNumber()

		(pieces[currentIdx].startsWith(Glob.endScopeKeyword))
			.echt {
				val value = Scope.valueOf(pieces[currentIdx].replace(Glob.endScopeKeyword, ""))
				(value == scope)
					.echt { thus pop 1 }
					.doch { throws("value" to value, "scope" to scope) }
			}
			.doch {	throws() }
	}


	infix fun popIfPresent(value: String): Boolean {
		popUntilNotLineNumber()

		return (value == pieces[currentIdx]) echt { takeAStep() }
	}


	infix fun popIfPresent(pattern: Regex): Boolean {
		popUntilNotLineNumber()

		return (pieces[currentIdx] matches pattern) echt { takeAStep() }
	}


	fun popIfPresentSingleType(): Pair<Boolean, String> {
		popUntilNotLineNumber()

		try {
			val threePiece = pieces
				.subList(currentIdx, currentIdx + 3)
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
				.subList(currentIdx, currentIdx + 2)
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


	fun popIfPresentSingleTypeThrowIfNot(): Pair<Boolean, String> { //TODO remove
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


	infix fun popStartScope(scope: Scope) {
		popUntilNotLineNumber()

		(pieces[currentIdx].startsWith(Glob.startScopeKeyword))
			.echt {
				val foundScope = Scope.valueOf(pieces[currentIdx].replace(Glob.startScopeKeyword, ""))

				(foundScope == scope)
					.echt {
						takeAStep()
					}
					.doch { throws() }
			}
			.doch {
				throws()
			}
	}


	protected fun popUntilNotLineNumber() {
		while (pieces[currentIdx].startsWith(Glob.lineNumberKeyword)) {
			Glob.lineNumber = pieces[currentIdx]
			takeAStep()
		}
	}
}