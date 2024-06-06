package com.github.jdw.funghi.fragments

import com.github.jdw.funghi.fragments.builders.IdlTypeBuilder

/**
 * This section lists the types supported by Web IDL, the set of values or Infra
 * type corresponding to each type, and how constants of that type are represented.
 *
 * See further documentation:
 * * (IDL Types)[https://webidl.spec.whatwg.org/#idl-types]
 *
 * @author Johannes Alexis Wirde
 */
class IdlType(builder: IdlTypeBuilder): IdlFragment {
	val name = builder.name!!
	val type = builder.type!!

	enum class TYPE {
		/**
		 * The following types are known as integer types:
		 * [byte](https://webidl.spec.whatwg.org/#idl-byte),
		 * [octet](https://webidl.spec.whatwg.org/#idl-octet),
		 * [short](https://webidl.spec.whatwg.org/#idl-short),
		 * [unsigned short](https://webidl.spec.whatwg.org/#idl-unsigned-short),
		 * [long](https://webidl.spec.whatwg.org/#idl-long),
		 * [unsigned long](https://webidl.spec.whatwg.org/#idl-unsigned-long),
		 * [long long](https://webidl.spec.whatwg.org/#idl-long-long)
		 * and
		 * [unsigned long long](https://webidl.spec.whatwg.org/#idl-unsigned-long-long).
		 */
		INTEGER,

		/**
		 * The following types are known as numeric types:
		 * [the integer types](https://webidl.spec.whatwg.org/#dfn-integer-type),
		 * [float](https://webidl.spec.whatwg.org/#idl-float),
		 * [unrestricted float](https://webidl.spec.whatwg.org/#idl-unrestricted-float),
		 * [double](https://webidl.spec.whatwg.org/#idl-double) and
		 * [unrestricted double](https://webidl.spec.whatwg.org/#idl-unrestricted-double).
		 */
		NUMERIC,

		/**
		 * The primitive types are
		 * [bigint](https://webidl.spec.whatwg.org/#idl-bigint),
		 * [boolean](https://webidl.spec.whatwg.org/#idl-boolean) and
		 * [the numeric types](https://webidl.spec.whatwg.org/#dfn-numeric-type).
		 */
		PRIMITIVE,

		/**
		 * The string types are
		 * [DOMString](https://webidl.spec.whatwg.org/#idl-DOMString),
		 * all [enumeration types](https://webidl.spec.whatwg.org/#idl-enumeration),
		 * [ByteString](https://webidl.spec.whatwg.org/#idl-ByteString) and
		 * [USVString](https://webidl.spec.whatwg.org/#idl-USVString).
		 */
		STRING,

		/**
		 * The buffer types are [ArrayBuffer](https://webidl.spec.whatwg.org/#idl-ArrayBuffer) and
		 * [SharedArrayBuffer](https://webidl.spec.whatwg.org/#idl-SharedArrayBuffer).
		 */
		BUFFER,

		/**
		 * The typed array types are
		 * [Int8Array](https://webidl.spec.whatwg.org/#idl-Int8Array),
		 * [Int16Array](https://webidl.spec.whatwg.org/#idl-Int16Array),
		 * [Int32Array](https://webidl.spec.whatwg.org/#idl-Int32Array),
		 * [Uint8Array](https://webidl.spec.whatwg.org/#idl-Uint8Array),
		 * [Uint16Array](https://webidl.spec.whatwg.org/#idl-Uint16Array),
		 * [Uint32Array](https://webidl.spec.whatwg.org/#idl-Uint32Array),
		 * [Uint8ClampedArray](https://webidl.spec.whatwg.org/#idl-Uint8ClampedArray),
		 * [BigInt64Array](https://webidl.spec.whatwg.org/#idl-BigInt64Array),
		 * [BigUint64Array](https://webidl.spec.whatwg.org/#idl-BigUint64Array),
		 * [Float16Array](https://webidl.spec.whatwg.org/#idl-Float16Array),
		 * [Float32Array](https://webidl.spec.whatwg.org/#idl-Float32Array), and
		 * [Float64Array](https://webidl.spec.whatwg.org/#idl-Float64Array).
		 */
		TYPED_ARRAY,

		/**
		 * The buffer view types are
		 * [DataView](https://webidl.spec.whatwg.org/#idl-DataView) and the
		 * [typed array types](https://webidl.spec.whatwg.org/#dfn-typed-array-type).
		 */
		BUFFER_VIEW,

		/**
		 * The buffer source types are the
		 * [buffer types](https://webidl.spec.whatwg.org/#buffer-types)
		 * and the
		 * [buffer view types](https://webidl.spec.whatwg.org/#buffer-view-types).
		 */
		BUFFER_SOURCE,

		/**
		 * The
		 * [object](https://webidl.spec.whatwg.org/#idl-object)
		 * type, all
		 * [interface types](https://webidl.spec.whatwg.org/#idl-interface)
		 * , and all
		 * [callback interface](https://webidl.spec.whatwg.org/#idl-callback-interface)
		 * types are known as object types.
		 */
		OBJECT
	}
}