package com.github.jdw.funghi.pieces

enum class Scope {
	ARGUMENT,
	ARGUMENTS,
	ATTRIBUTE,
	CONSTANT_ATTRIBUTE,
	DICTIONARY,
	DICTIONARY_MEMBER,
	ENUM,
	EXTENDED_ATTRIBUTE,
	EXTENDED_ATTRIBUTE_LIST,
	INCLUDES,
	INTERFACE,
	MODEL,
	MULTI_TYPE,
	OPERATION,
	OPERATION_CONSTRUCTOR,
	SEQUENCE,
	SPECIAL_OPERATION,
	TYPEDEF,
	UNION_TYPE;

	fun nextScopeKeyword(): String = "${Glob.nextScopeKeyword}$name"
	fun startScopeKeyword(): String = "${Glob.startScopeKeyword}$name"
	fun stopScopeKeyword(): String = "${Glob.stopScopeKeyword}$name"
}