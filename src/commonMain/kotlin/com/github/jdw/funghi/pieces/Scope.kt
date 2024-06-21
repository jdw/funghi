package com.github.jdw.funghi.pieces

enum class Scope {
	TYPEDEF,
	DICTIONARY,
	INTERFACE,
	ENUM,
	ATTRIBUTE,
	OPERATION,
	OPERATION_CONSTRUCTOR,
	INCLUDES,
	CONST_ATTRIBUTE,
	DICTIONARY_MEMBER,
	EXTENDED_ATTRIBUTE,
	MULTI_TYPE,
	MODEL,
	ARGUMENT,
	EXTENDED_ATTRIBUTE_LIST;

	fun startScopeKeyword(): String = "${Glob.startScopeKeyword}$name"
	fun endScopeKeyword(): String = "${Glob.endScopeKeyword}$name"
}