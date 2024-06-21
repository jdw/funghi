package com.github.jdw.funghi.pieces


class Pieces(builder: PiecesBuilder): PiecesHolder() {
	override val pieces = builder.pieces.toList()


	init {
		popUntilNotLineNumber()
		Glob.pieces = this
	}
}