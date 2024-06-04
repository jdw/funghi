package com.github.jdw.funghi.fragments.builders

interface IdlFragmentBuilder {
	fun supportedKeywords(): Set<String>
	fun parseLine(line: String)
	fun lineIsRelevant(line: String): Boolean
}