fun throws(): Nothing = throw Exception()

fun noop(): Unit {}

fun MutableList<String>.popIfPresent(value: String): Boolean {
	popUnitlNotLineNumber()

	return if (value == first()) {
		removeFirst()
		true
	}
	else {
		false
	}
}


fun MutableList<String>.popIfFirstStartsWith(value: String): Pair<Boolean, String> {
	popUnitlNotLineNumber()

	if (first().startsWith(value)) {
		val ret = Pair(true, first())
		removeFirst()
		return ret
	}

	return Pair(false, "")
}


fun MutableList<String>.popUnitlNotLineNumber() {
	while (first().startsWith(Glob.lineNumberKeyword)) {
		Glob.lineNumber = first()
		removeFirst()
	}
}


fun String.toPieces(): MutableList<String> {
	return this.split(" ").toMutableList()
}

/**
 * Invokes *block* if receiver is *true* then returns receiver.
 *
 * @param block The code to be executed.
 * @receiver Boolean
 * @since Sea of Shadows 0.0.3-SNAPSHOT
 * @author Johannes Alexis Wirde (johannes.wirde@gmail.com)
 */
fun Boolean.echt(block: () -> Unit): Boolean {
	if (this) block.invoke()

	return this
}


/**
 * Invokes *block* if receiver is *false* then returns receiver.
 *
 * @param block The code to be executed.
 * @receiver Boolean
 * @since Sea of Shadows 0.0.3-SNAPSHOT
 * @author Johannes Alexis Wirde (johannes.wirde@gmail.com)
 */
fun Boolean.doch(block: () -> Unit): Boolean {
	if (!this) block.invoke()

	return this
}