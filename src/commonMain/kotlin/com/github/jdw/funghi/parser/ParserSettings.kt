package com.github.jdw.funghi.parser

enum class ParserSettings {
	V_20240408 {
		override fun interfaceKeywords(): Set<String> = setOf("mixin")
		override fun dictionaryKeywords(): Set<String> = setOf()
		override fun typedefKeywords(): Set<String> = setOf()
		override fun modelKeywords(): Set<String> = setOf("typedef", "interface", "dictionary", "enum")
		override fun integerTypesKeywords(): Set<String> = setOf("byte", "octet", "short", "unsigned short", "long", "unsigned long", "long long", "unsigned long long")
		override fun numericTypesKeywords(): Set<String> = setOf(*integerTypesKeywords().toTypedArray(), "float", "unrestricted float", "double", "unrestricted double")
		override fun primitiveTypesKeywords(): Set<String> = setOf(*numericTypesKeywords().toTypedArray(), "bigint", "boolean")
		override fun stringTypesKeywords(): Set<String> = setOf("DOMString", "ByteString", "USVString")
		override fun bufferTypesKeywords(): Set<String> = setOf("ArrayBuffer", "SharedArrayBuffer")
		override fun typedArrayTypesKeywords(): Set<String> = setOf("Int8Array", "Int16Array", "Int32Array", "Uint8Array", "Uint16Array", "Uint32Array", "Uint8ClampedArray", "BigInt64Array", "BigUint64Array", "Float16Array", "Float32Array", "Float64Array")
		override fun bufferViewTypesKeywords(): Set<String> = setOf(*typedArrayTypesKeywords().toTypedArray(), "DataView")
		override fun bufferSourceTypesKeywords(): Set<String> = setOf(*bufferTypesKeywords().toTypedArray(), *bufferViewTypesKeywords().toTypedArray())
		override fun allPredefinedTypesKeywords(): Set<String> =
			setOf(*integerTypesKeywords().toTypedArray(),
				*numericTypesKeywords().toTypedArray(),
				*primitiveTypesKeywords().toTypedArray(),
				*stringTypesKeywords().toTypedArray(),
				*bufferTypesKeywords().toTypedArray(),
				*typedArrayTypesKeywords().toTypedArray(),
				*bufferViewTypesKeywords().toTypedArray(),
				*bufferSourceTypesKeywords().toTypedArray())
		override fun argumentNameKeywords(): Set<String> = setOf("async", "attribute", "callback", "const", "constructor", "deleter", "dictionary", "enum", "getter", "includes", "inherit", "interface", "iterable", "maplike", "mixin", "namespace", "partial", "readonly", "required", "setlike", "setter", "static", "stringifier", "typedef", "unrestricted")
		override fun complexTypesRegex(): Regex = "([a-zA-Z])+([a-zA-Z0-9_])*".toRegex()
		override fun identifierReservedKeywords(): Set<String> = setOf("constructor", "toString")
		override fun identifierIsProhibited(name: String): Boolean = identifierReservedKeywords().contains(name) || name.startsWith("_")
		override fun identifierRegex(): Regex = "([a-zA-Z])+([a-zA-Z0-9_])*".toRegex()
		override fun nullableMarkers(): Set<String> = setOf("?")
		override fun arrayMarkers(): Set<String> = setOf("[]")
		override fun varargMarkers(): Set<String> = setOf("...")
		override fun operationRegex(): Regex = "[A-Za-z]+[a-zA-Z_0-9]*\\(.*\\);".toRegex()
		override fun constructorOperationDefinitionKeywords(): Set<String> = setOf("constructor")
		override fun constructorOperationRegex(): Regex = "constructor\\(.*\\);".toRegex()
		override fun attributeDefinitionKeywords(): Set<String> = setOf("attribute")
		override fun extendedAttributesWildcardMarkers(): Set<String> = setOf("*")
	};

	abstract fun interfaceKeywords(): Set<String>
	abstract fun dictionaryKeywords(): Set<String>
	abstract fun typedefKeywords(): Set<String>
	abstract fun modelKeywords(): Set<String>
	abstract fun integerTypesKeywords(): Set<String>
	abstract fun numericTypesKeywords(): Set<String>
	abstract fun primitiveTypesKeywords(): Set<String>
	abstract fun stringTypesKeywords(): Set<String>
	abstract fun bufferTypesKeywords(): Set<String>
	abstract fun typedArrayTypesKeywords(): Set<String>
	abstract fun bufferViewTypesKeywords(): Set<String>
	abstract fun bufferSourceTypesKeywords(): Set<String>
	abstract fun allPredefinedTypesKeywords(): Set<String>
	abstract fun argumentNameKeywords(): Set<String>
	abstract fun complexTypesRegex(): Regex
	abstract fun identifierReservedKeywords(): Set<String>
	abstract fun identifierIsProhibited(name: String): Boolean
	abstract fun identifierRegex(): Regex
	abstract fun nullableMarkers(): Set<String>
	abstract fun arrayMarkers(): Set<String>
	abstract fun varargMarkers(): Set<String>
	abstract fun operationRegex(): Regex
	abstract fun constructorOperationDefinitionKeywords(): Set<String>
	abstract fun constructorOperationRegex(): Regex
	abstract fun attributeDefinitionKeywords(): Set<String>
	abstract fun extendedAttributesWildcardMarkers(): Set<String>
}