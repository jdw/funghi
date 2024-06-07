package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.pieces.Pieces
import throws

abstract class IdlFragmentBuilder {
	val objection = ""
	val thus = this


	infix fun throwing(value: String) {
		if ("" == value) throws()

		throws(value)
	}

	abstract infix fun parse(pieces: Pieces)
}