package com.github.jdw.funghi.pieces


class PiecesBuilder(data: String): PiecesHolder() {
	override val pieces = data.split(" ").toMutableList()

	fun backward() = currentIdx--


	fun forward() = currentIdx++


	infix fun forward(fast: Int) {
		currentIdx += fast
	}


	infix fun forwardUntil(value: String) {
		while (!peek(value)) currentIdx++
	}


	infix fun push(value: String) = pieces.add(currentIdx, value)


	infix fun replace(value: String) {
		pieces.removeAt(currentIdx)
		push(value)
	}
}