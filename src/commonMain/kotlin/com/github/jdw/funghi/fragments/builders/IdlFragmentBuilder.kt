package com.github.jdw.funghi.fragments.builders

import com.github.jdw.funghi.pieces.Pieces

interface IdlFragmentBuilder {
	fun parse(pieces: Pieces)
}