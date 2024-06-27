package com.github.jdw.funghi.pieces

enum class Scope {
	ARGUMENT,
	@Deprecated("Use ARGUMENT only!") ARGUMENTS, //TODO Remove
	ATTRIBUTE,
	CONSTANT,
	DICTIONARY,
	MEMBER,
	ENUM,
	EXTENDED_ATTRIBUTE,
	LIST,
	INCLUDES,
	INTERFACE,
	MODEL,
	OPERATION,
	CONSTRUCTOR,
	SEQUENCE,
	SPECIAL_OPERATION,
	SUPER_TYPE,
	TYPE,
	TYPEDEF,
	@Deprecated("Use TYPE instead!") UNION_TYPE; //TODO Remove


	fun nextScopeKeyword(): String = "${Glob.nextScopeKeyword}$name"
	fun startScopeKeyword(): String = "${Glob.startScopeKeyword}$name"
	fun stopScopeKeyword(): String = "${Glob.stopScopeKeyword}$name"


	fun childScopes(): Set<Scope> {
		return when (this) {
			ARGUMENT -> emptySet()
			ARGUMENTS -> setOf(ARGUMENT)
			ATTRIBUTE -> setOf(TYPE)
			CONSTANT -> setOf(TYPE)
			DICTIONARY -> setOf(MEMBER, SUPER_TYPE)
			MEMBER -> setOf(TYPE)
			ENUM -> emptySet()
			EXTENDED_ATTRIBUTE -> setOf(LIST)
			LIST -> emptySet()
			INCLUDES -> emptySet()
			INTERFACE -> setOf(ATTRIBUTE, SPECIAL_OPERATION, OPERATION, CONSTRUCTOR, CONSTANT, SUPER_TYPE)
			MODEL -> setOf(INTERFACE, DICTIONARY, ENUM, TYPEDEF)
			OPERATION -> setOf(ARGUMENTS)
			CONSTRUCTOR -> setOf(ARGUMENTS)
			SEQUENCE -> setOf(TYPE)
			SPECIAL_OPERATION -> setOf(ARGUMENTS)
			SUPER_TYPE -> emptySet()
			TYPE -> emptySet()
			TYPEDEF -> emptySet()
			UNION_TYPE -> emptySet()
		}
	}
}