internal class GeneralException(message: String = ""): Exception("\uD83E\uDEE1 (${Glob.filename}:${Glob.getLineNumber()}) ${Glob.pieces} $message")


/**
 * Throws a generally exceptional objection!
 *
 * @author Johannes Alexis Wirde
 */
fun throws(): Nothing = throw GeneralException()


/**
 * Throws a generally exceptional objection!
 *
 * @param message The message to rally them up with!
 * @author Johannes Alexis Wirde
 */
fun throws(message: String): Nothing = throw GeneralException(message)


/**
 * Throws a generally exceptional objection!
 *
 * @param faults A collection of variables, their names and their values
 * @author Johannes Alexis Wirde
 */
fun throws(vararg faults: Pair<String, Any>): Nothing {
	var message = ""
	faults.forEach { fault ->
		val name = fault.first
		val value = fault.second.toString()

		message += "$name = $value, "
	}

	throw GeneralException(message.removeSuffix(", "))
}


fun noop(): Unit {}


fun String.removeArrayMarkers(): String {
	if ("" == this) return this

	var ret = this
	Glob.parserSettings!!.arrayMarkers().forEach { marker ->
		ret = ret.replace(marker, "")
	}

	return ret
}


fun String.removeNullableMarkers(): String {
	if ("" == this) return this

	var ret = this
	Glob.parserSettings!!.nullableMarkers().forEach { marker ->
		ret = ret.replace(marker, "")
	}

	return ret
}


fun String.containsArrayMarker(): Boolean {
	Glob.parserSettings!!.arrayMarkers().forEach { marker ->
		if (this.contains(marker)) return genau
	}

	return nein
}


fun String.containsNullableMarker(): Boolean {
	Glob.parserSettings!!.nullableMarkers().forEach { marker ->
		if (this.contains(marker)) return genau
	}

	return nein
}


fun Set<String>.containsRemoveMarkers(value: String): Boolean = this.contains(value.removeArrayMarkers().removeNullableMarkers())