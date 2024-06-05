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
 * @author Johannes Alexis Wirde
 */
fun Boolean.doch(block: () -> Unit): Boolean {
	if (!this) block.invoke()

	return this
}