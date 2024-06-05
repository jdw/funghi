package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlExtendedAttributeBuilder

/**
 * An extended attribute is an annotation that can appear on
 * [definitions](https://webidl.spec.whatwg.org/#dfn-definition), types as
 * [annotated types](https://webidl.spec.whatwg.org/#annotated-types),
 * [interface members](https://webidl.spec.whatwg.org/#dfn-interface-member),
 * [interface mixin members](https://webidl.spec.whatwg.org/#interface-mixin-member),
 * [callback interface members](https://webidl.spec.whatwg.org/#callback-interface-member),
 * [namespace members](https://webidl.spec.whatwg.org/#dfn-namespace-member),
 * [dictionary members](https://webidl.spec.whatwg.org/#dfn-dictionary-member), and
 * [operation](https://webidl.spec.whatwg.org/#dfn-operation) arguments, and is used to control how language bindings will handle those constructs.
 * Extended attributes are specified with an ExtendedAttributeList, which is a square bracket enclosed,
 * comma separated list of ExtendedAttributes.
 *
 *
 * The ExtendedAttribute grammar symbol matches nearly any sequence of tokens, however the
 * [extended attributes]() defined in this document only accept a more restricted syntax.
 * Any extended attribute encountered in an
 * [IDL fragment]() is matched against the following five grammar symbols to determine which form (or forms) it is in:
 *
 *
 * | Grammar symbol	| Form | Example |
 * |----------------|------|---------|
 * | ExtendedAttributeNoArgs | takes no arguments | [[Replaceable]] |
 * | ExtendedAttributeArgList | takes an argument list | Not currently used; previously used by [Constructor(double x, double y)] |
 * | ExtendedAttributeNamedArgList |takes a named argument list | [LegacyFactoryFunction=Image(DOMString src)] |
 * | ExtendedAttributeIdent | takes an identifier | [PutForwards=name] |
 * | ExtendedAttributeIdentList | takes an identifier list | [Exposed=(Window,Worker)] |
 * | ExtendedAttributeWildcard | takes a wildcard | [Exposed=*] |
 *
 * See further documentation:
 * * [The WebIDL standard](https://webidl.spec.whatwg.org/#idl-extended-attributes)
 *
 * @author Johannes Alexis Wirde
 */
class IdlExtendedAttribute(builder: IdlExtendedAttributeBuilder): IdlFragment {
	enum class Type {
		/**
		 * * Grammar symbol = [ExtendedAttributeNoArgs](https://webidl.spec.whatwg.org/#prod-ExtendedAttributeNoArgs)
		 * * Form = takes no arguments
		 * * Example = [[Replaceable]]
		 */
		NO_ARGS,

		/**
		 * * Grammar symbol = [ExtendedAttributeArgList](https://webidl.spec.whatwg.org/#prod-ExtendedAttributeArgList)
		 * * Form = takes an argument list
		 * * Example = Not currently used; previously used by [[Constructor(double x, double y)]]
		 */
		ARG_LIST,

		/**
		 * * Grammar symbol = [ExtendedAttributeNamedArgList](https://webidl.spec.whatwg.org/#prod-ExtendedAttributeNamedArgList)
		 * * Form = takes a named argument list
		 * * Example = [[LegacyFactoryFunction=Image(DOMString src)]]
		 */
		NAMED_ARG_LIST,

		/**
		 * * Grammar symbol = [ExtendedAttributeIdent](https://webidl.spec.whatwg.org/#prod-ExtendedAttributeIdent)
		 * * Form = takes an identifier
		 * * Example = [[PutForwards=name]]
		 */
		IDENT,

		/**
		 * * Grammar symbol = [ExtendedAttributeIdentList](https://webidl.spec.whatwg.org/#prod-ExtendedAttributeIdentList)
		 * * Form = takes an identifier list
		 * * Example = [[Exposed=(Window,Worker)]]
		 */
		IDENT_LIST,

		/**
		 * * Grammar symbol = [ExtendedAttributeWildcard](https://webidl.spec.whatwg.org/#prod-ExtendedAttributeWildcard)
		 * * Form = takes a wildcard
		 * * Example = [[Exposed=*]]
		 */
		WILDCARD
	}
}