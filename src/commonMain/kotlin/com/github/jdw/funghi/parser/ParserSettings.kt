package com.github.jdw.funghi.parser

enum class ParserSettings {
	V_20240610,
	V_20240408;

	open fun interfaceKeywords(): Set<String> = setOf("mixin", "partial")
	open fun dictionaryKeywords(): Set<String> = emptySet()
	open fun typedefKeywords(): Set<String> = emptySet()
	open fun modelKeywords(): Set<String> = setOf("typedef", "interface", "dictionary", "enum")
	open fun integerTypesKeywords(): Set<String> = setOf("byte", "octet", "short", "unsigned short", "long", "unsigned long", "long long", "unsigned long long")
	open fun numericTypesKeywords(): Set<String> = setOf(*integerTypesKeywords().toTypedArray(), "float", "unrestricted float", "double", "unrestricted double")
	open fun primitiveTypesKeywords(): Set<String> = setOf(*numericTypesKeywords().toTypedArray(), "bigint", "boolean")
	open fun stringTypesKeywords(): Set<String> = setOf("DOMString", "ByteString", "USVString")
	open fun bufferTypesKeywords(): Set<String> = setOf("ArrayBuffer", "SharedArrayBuffer")
	open fun typedArrayTypesKeywords(): Set<String> = setOf("Int8Array", "Int16Array", "Int32Array", "Uint8Array", "Uint16Array", "Uint32Array", "Uint8ClampedArray", "BigInt64Array", "BigUint64Array", "Float16Array", "Float32Array", "Float64Array")
	open fun bufferViewTypesKeywords(): Set<String> = setOf(*typedArrayTypesKeywords().toTypedArray(), "DataView")
	open fun bufferSourceTypesKeywords(): Set<String> = setOf(*bufferTypesKeywords().toTypedArray(), *bufferViewTypesKeywords().toTypedArray())
	open fun allPredefinedTypesKeywords(): Set<String> =
		setOf(*integerTypesKeywords().toTypedArray(),
			*numericTypesKeywords().toTypedArray(),
			*primitiveTypesKeywords().toTypedArray(),
			*stringTypesKeywords().toTypedArray(),
			*bufferTypesKeywords().toTypedArray(),
			*typedArrayTypesKeywords().toTypedArray(),
			*bufferViewTypesKeywords().toTypedArray(),
			*bufferSourceTypesKeywords().toTypedArray())
	open fun argumentNameKeywords(): Set<String> = setOf("async", "attribute", "callback", "const", "constructor", "deleter", "dictionary", "enum", "getter", "includes", "inherit", "interface", "iterable", "maplike", "mixin", "namespace", "partial", "readonly", "required", "setlike", "setter", "static", "stringifier", "typedef", "unrestricted")
	open fun complexTypesRegex(): Regex = "([a-zA-Z])+([a-zA-Z0-9_])*".toRegex()
	open fun identifierReservedKeywords(): Set<String> = setOf("constructor", "toString")
	open fun identifierIsProhibited(name: String): Boolean = identifierReservedKeywords().contains(name) || name.startsWith("_")
	open fun identifierRegex(): Regex = "([a-zA-Z])+([a-zA-Z0-9_])*".toRegex()
	open fun nullableMarkers(): Set<String> = setOf("?")
	open fun arrayMarkers(): Set<String> = setOf("[]")
	open fun varargMarkers(): Set<String> = setOf("...")
	open fun operationRegex(): Regex = "[A-Za-z]+[a-zA-Z_0-9]*\\(.*\\);".toRegex()
	open fun constructorOperationDefinitionKeywords(): Set<String> = setOf("constructor")
	open fun constructorOperationRegex(): Regex = "constructor\\(.*\\);".toRegex()
	open fun attributeDefinitionKeywords(): Set<String> = setOf("attribute")
	open fun extendedAttributesWildcardMarkers(): Set<String> = setOf("*")
}