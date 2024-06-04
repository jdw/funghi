package com.github.jdw.funghi.parser

enum class ParserSettings {
	v20240408 {
		override fun interfaceKeywords(): Set<String> = setOf("mixin")
		override fun dictionaryKeywords(): Set<String> = setOf()
		override fun typedefKeywords(): Set<String> = setOf()
		override fun modelKeywords(): Set<String> = setOf("typedef", "interface", "dictionary", "enum")
		override fun nonComplexTypesKeywords(): Set<String> = setOf("long", "long long", "DOMString", "double", "void", "undefined", "any")
		override fun argumentNameKeywords(): Set<String> = setOf("async", "attribute", "callback", "const", "constructor", "deleter", "dictionary", "enum", "getter", "includes", "inherit", "interface", "iterable", "maplike", "mixin", "namespace", "partial", "readonly", "required", "setlike", "setter", "static", "stringifier", "typedef", "unrestricted")
		override fun complexTypesRegex(): Regex = "([a-zA-Z]).([a-zA-Z0-9_])+".toRegex()
		override fun identifierReservedKeywords(): Set<String> = setOf("constructor", "toString")
		override fun complexTypesNameIsProhibited(name: String): Boolean = identifierReservedKeywords().contains(name) || name.startsWith("_")
		override fun nullableMarkers(): Set<String> = setOf("?")
		override fun arrayMarkers(): Set<String> = setOf("[]")
		override fun varargMarkers(): Set<String> = setOf("...")
		override fun operationRegex(): Regex = "[A-Za-z]+[a-zA-Z_0-9]*\\(.*\\)".toRegex()
		override fun constructorOperationDefinitionKeywords(): Set<String> = setOf("constructor")
		override fun attributeDefinitionKeywords(): Set<String> = setOf("attribute")
	};

	abstract fun interfaceKeywords(): Set<String>
	abstract fun dictionaryKeywords(): Set<String>
	abstract fun typedefKeywords(): Set<String>
	abstract fun modelKeywords(): Set<String>
	abstract fun nonComplexTypesKeywords(): Set<String>
	abstract fun argumentNameKeywords(): Set<String>
	abstract fun complexTypesRegex(): Regex
	abstract fun identifierReservedKeywords(): Set<String>
	abstract fun complexTypesNameIsProhibited(name: String): Boolean
	abstract fun nullableMarkers(): Set<String>
	abstract fun arrayMarkers(): Set<String>
	abstract fun varargMarkers(): Set<String>
	abstract fun operationRegex(): Regex
	abstract fun constructorOperationDefinitionKeywords(): Set<String>
	abstract fun attributeDefinitionKeywords(): Set<String>
}