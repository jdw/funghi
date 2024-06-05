package com.github.jdw.funghi.pieces

import com.github.jdw.funghi.fragments.IdlScopes
import doch
import throws

class Pieces(data: String) {
	private val pieces = data.split(" ").toMutableList()
	private val previousPieces = mutableListOf<String>()
	private val previousScopeStarts = mutableListOf<IdlScopes>()

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


	fun popStartScopeThrowIfNot(): IdlScopes {
		val (_, value) = popIfFirstStartsWithThrowIfNot(Glob.startScopeKeyword)

		return IdlScopes.valueOf(value.replace(Glob.startScopeKeyword, ""))
	}


	fun popIfPresent(value: String): Boolean {
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
		val ret = popIfFirstStartsWith(value)

		ret.first.doch { throws() }

		return ret
	}


	fun popIfPresentThrowIfNot(value: String) {
		popUntilNotLineNumber()

		popIfPresent(value)
			.doch { throws("First was not '$value'!") }
	}
}