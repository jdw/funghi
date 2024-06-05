package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlInterfaceBuilder

/**
 * [IDL fragments](https://webidl.spec.whatwg.org/#dfn-idl-fragment) are used to describe object oriented systems.
 * In such systems, objects are entities that have identity and which are encapsulations of state and behavior.
 * An `interface` is a definition (matching `interface` [InterfaceRest](https://webidl.spec.whatwg.org/#prod-InterfaceRest))
 * that declares some state and behavior that an object implementing that interface will expose.
 *```
 * [extended_attributes]
 * interface identifier {
 *   /* interface_members... */
 * };
 * ```
 * An interface is a specification of a set of `interface members` (matching [InterfaceMembers](https://webidl.spec.whatwg.org/#prod-InterfaceMembers)).
 * These are the [members](https://webidl.spec.whatwg.org/#dfn-member) that appear between the braces in the
 * interface declaration.
 *
 * Interfaces in Web IDL describe how objects that implement the interface behave. In bindings for object-oriented
 * languages, it is expected that an object that implements a particular IDL interface provides ways to inspect
 * and modify the object’s state and to invoke the behavior described by the interface.
 *
 * An interface can be defined to inherit from another interface. If the identifier of the interface is followed by
 * a U+003A (:) and an identifier, then that identifier identifies the inherited interface. An object that implements
 * an interface that inherits from another also implements that inherited interface. The object therefore will also
 * have members that correspond to the interface members from the inherited interface.
 *```
 * interface identifier : identifier_of_inherited_interface {
 *   /* interface_members... */
 * };
 * ```
 * The order that members appear in has significance for property enumeration in the
 * [JavaScript binding](https://webidl.spec.whatwg.org/#js-interfaces).
 *
 * Interfaces may specify an interface member that has the same name as one from an inherited interface. Objects
 * that implement the derived interface will expose the member on the derived interface. It is language binding
 * specific whether the overridden member can be accessed on the object.
 *
 * See further documentation:
 * * [The WebIDL specification](https://webidl.spec.whatwg.org/#idl-interfaces)
 *
 * @author Johannes Alexis Wirde
 */
class IdlInterface(builder: IdlInterfaceBuilder): IdlFragment {
	val type = IdlFragment.Type.INTERFACE
	val name = builder.name!!

	init {
		println("hej")
	}
}