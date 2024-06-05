package com.github.jdw.funghi.fragments

enum class IdlScopes {
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
	MULTI_TYPE;

	fun startScopeKeyword(): String = "${Glob.startScopeKeyword}$name"
	fun endScopeKeyword(): String = "${Glob.endScopeKeyword}$name"
}