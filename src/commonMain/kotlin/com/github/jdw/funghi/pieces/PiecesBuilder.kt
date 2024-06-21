package com.github.jdw.funghi.pieces


class PiecesBuilder(data: String): PiecesHolder() {
	override val pieces = data.split(" ").toMutableList()
}