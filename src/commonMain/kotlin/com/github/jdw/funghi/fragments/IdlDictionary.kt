package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlDictionaryBuilder

/**
 * A `dictionary` is a definition (matching [Dictionary](https://webidl.spec.whatwg.org/#prod-Dictionary)) used to
 * define an [ordered map](https://infra.spec.whatwg.org/#ordered-map) data type with a fixed,
 * ordered set of [entries](https://infra.spec.whatwg.org/#map-entry), termed `dictionary members`, where
 * [keys](https://infra.spec.whatwg.org/#map-getting-the-keys) are strings and
 * [values](https://infra.spec.whatwg.org/#map-getting-the-values) are of a particular type specified in the definition.
 *```
 * dictionary identifier {
 *   /* dictionary_members... */
 * };
 * ```
 * Dictionary instances do not retain a reference to their language-specific representations (e.g., the corresponding
 * JavaScript object). So for example, returning a dictionary from an
 * [operation](https://webidl.spec.whatwg.org/#dfn-operation) will result in a new JavaScript object being created
 * from the current values of the dictionary.
 * And, an operation that accepts a dictionary as an argument will perform a one-time conversion from the given
 * JavaScript value into the dictionary, based on the current properties of the JavaScript object. Modifications to
 * the dictionary will not be reflected in the corresponding JavaScript object, and vice-versa.
 *
 *
 * Dictionaries must not be used as the type of an [attribute](https://webidl.spec.whatwg.org/#dfn-attribute) or
 * [constant](https://webidl.spec.whatwg.org/#dfn-constant).
 *
 *
 * A dictionary can be defined to inherit from another dictionary. If the identifier of the dictionary is
 * followed by a colon and an
 * [identifier](https://webidl.spec.whatwg.org/#dfn-identifier), then that identifier identifies the inherited dictionary. The identifier must identify a dictionary.
 *
 * A dictionary must not be declared such that its inheritance hierarchy has a cycle. That is, a dictionary
 * A cannot inherit from itself, nor can it inherit from another dictionary B that inherits from A, and so on.
 *```
 * dictionary Base {
 *   /* dictionary_members... */
 * };
 *
 * dictionary Derived : Base {
 *   /* dictionary_members... */
 * };
 * ```
 * The inherited dictionaries of a given dictionary D is the set of all dictionaries that D inherits from, directly or
 * indirectly. If D does not
 * [inherit](https://webidl.spec.whatwg.org/#dfn-inherit-dictionary) from another dictionary, then the set is empty.
 * Otherwise, the set includes the dictionary E that D
 * [inherits](https://webidl.spec.whatwg.org/#dfn-inherit) from and all of E’s
 * [inherited dictionaries](https://webidl.spec.whatwg.org/#dfn-inherited-dictionaries).
 *
 * [Dictionary members]() can be specified as `required`, meaning that converting a language-specific value to a
 * dictionary requires providing a value for that member.
 * Any dictionary member that is not
 * [required](https://webidl.spec.whatwg.org/#required-dictionary-member) is `optional`.
 *
 * Note that specifying
 * [dictionary members](https://webidl.spec.whatwg.org/#dfn-dictionary-member) as
 * [required](https://webidl.spec.whatwg.org/#required-dictionary-member) only has an observable effect when
 * converting other representations of dictionaries (like a JavaScript value supplied as an argument to an
 * [operation](https://webidl.spec.whatwg.org/#dfn-operation)) to an IDL dictionary.
 * Specification authors should leave the members
 * [optional](https://webidl.spec.whatwg.org/#dictionary-member-optional) in all other cases, including when a
 * dictionary type is used solely as the
 * [return type](https://webidl.spec.whatwg.org/#dfn-return-type) of
 * [operations](https://webidl.spec.whatwg.org/#dfn-operation).
 *
 * A given dictionary value of type D can have
 * [entries](https://infra.spec.whatwg.org/#map-entry) for each of the dictionary members defined on D and on any
 * of D’s
 * [inherited dictionaries](https://webidl.spec.whatwg.org/#dfn-inherited-dictionaries). Dictionary members that are
 * specified as
 * [required](https://webidl.spec.whatwg.org/#required-dictionary-member), or that are specified as having a
 * [default value](https://webidl.spec.whatwg.org/#dfn-dictionary-member-default-value), will always have such
 * corresponding
 * [entries](https://infra.spec.whatwg.org/#map-entry).
 * Other members' entries might or might not
 * [exist](https://infra.spec.whatwg.org/#map-exists) in the dictionary value.
 *
 * See further documentation:
 * * [The WebIDL specification](https://webidl.spec.whatwg.org/#idl-interfaces)
 *
 * @author Johannes Alexis Wirde
 */
class IdlDictionary(builder: IdlDictionaryBuilder): IdlFragment {
	companion object {
		val TYPE = IdlFragment.Type.DICTIONARY
	}
}