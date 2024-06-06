import com.github.jdw.funghi.Funghi
import kotlin.test.Test

class ParserTestInterfaces {
	@Test
	fun `Should manage to fully parse the file input-interfaces-00 dot idl`() {
		val fileContent = this::class.java.classLoader.getResource("input-interface-00.idl")?.readText()!!
		val model = Funghi().parse(fileContent, "input-interfaces-00.idl")

		assert(model.interfaces.sortedBy { it.name }.map { it.name }.joinToString(" ") == "A B C D E")
	}


	@Test
	fun `Should manage to fully parse the file input-interfaces-01 dot idl`() {
		val fileContent = this::class.java.classLoader.getResource("input-interface-01.idl")?.readText()!!
		val model = Funghi().parse(fileContent, "input-interfaces-01.idl")

		val sortedInterfaces = model.interfaces.sortedBy { it.name }
		assert(sortedInterfaces.map { it.name }.joinToString(" ") == "A B C")

		// Interface A
		assert(sortedInterfaces[0].attributes.size == 1)
		assert(sortedInterfaces[0].attributes[0].types.size == 1)
		assert(sortedInterfaces[0].attributes[0].types[0].name == "a")
		assert(sortedInterfaces[0].attributes[0].types[0].type == "double")

		// Interface B
		assert(sortedInterfaces[1].attributes.size == 1)
		assert(sortedInterfaces[1].attributes[0].types.size == 1)
		assert(sortedInterfaces[1].attributes[0].types[0].name == "b")
		assert(sortedInterfaces[1].attributes[0].types[0].type == "A")

		// Interface C
		assert(sortedInterfaces[2].attributes.size == 2)
		assert(sortedInterfaces[2].attributes[0].types.size == 1)
		assert(sortedInterfaces[2].attributes[0].types[0].name == "c")
		assert(sortedInterfaces[2].attributes[0].types[0].type == "Int32Array")
		assert(sortedInterfaces[2].attributes[1].types.size == 1)
		assert(sortedInterfaces[2].attributes[1].types[0].name == "c2")
		assert(sortedInterfaces[2].attributes[1].types[0].type == "B")
	}
}