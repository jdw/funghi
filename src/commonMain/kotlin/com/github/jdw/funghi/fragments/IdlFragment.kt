package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.parser.ParserSettings

/**
 * This section describes a language, Web IDL, which can be used to define interfaces for APIs in
 * the Web platform. A specification that defines Web APIs can include one or more IDL fragments
 * that describe the interfaces (the state and behavior that objects can exhibit) for the APIs
 * defined by that specification.
 * An IDL fragment is a sequence of definitions that matches the Definitions grammar symbol. The set
 * of IDL fragments that an implementation supports is not ordered. See [IDL grammar](https://webidl.spec.whatwg.org/#idl-grammar) for the complete
 * grammar and an explanation of the notation used.
 *
 * @author Johannes Alexis Wirde
 */
interface IdlFragment {
	enum class Type {
		INTERFACE, DICTIONARY, TYPE, TYPEDEF, ATTRIBUTE, EXTENDED_ATTRIBUTE, ENUM, MEMBER, NAMESPACE, CALLBACK_INTERFACE;
	}
}