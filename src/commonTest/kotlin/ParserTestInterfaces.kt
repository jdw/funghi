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
		val interfaceA = sortedInterfaces[0]
		assert(interfaceA.attributes.size == 1)
		assert(interfaceA.attributes[0].types.size == 1)
		assert(interfaceA.attributes[0].name == "a")
		assert(interfaceA.attributes[0].types[0].name == "double")
		assert(!interfaceA.attributes[0].types[0].isArray)
		assert(!interfaceA.attributes[0].types[0].isNullable)

		// Interface B
		val interfaceB = sortedInterfaces[1]
		assert(interfaceB.attributes.size == 1)
		assert(interfaceB.attributes[0].types.size == 1)
		assert(interfaceB.attributes[0].name == "b")
		assert(interfaceB.attributes[0].types[0].name == "A")
		assert(!interfaceB.attributes[0].types[0].isArray)
		assert(!interfaceB.attributes[0].types[0].isNullable)

		val interfaceC = sortedInterfaces[2]
		assert(interfaceC.attributes.size == 2)
		assert(interfaceC.attributes[0].types.size == 1)
		assert(interfaceC.attributes[0].name == "c")
		assert(interfaceC.attributes[0].types[0].name == "Int32Array")
		assert(interfaceC.attributes[1].types.size == 1)
		assert(!interfaceC.attributes[0].types[0].isArray)
		assert(!interfaceC.attributes[0].types[0].isNullable)
		assert(interfaceC.attributes[1].name == "c2")
		assert(interfaceC.attributes[1].types[0].name == "B")
		assert(!interfaceC.attributes[0].types[0].isArray)
		assert(!interfaceC.attributes[0].types[0].isNullable)
	}


	@Test
	fun `Should manage to fully parse the file input-interfaces-02 dot idl`() {
		val fileContent = this::class.java.classLoader.getResource("input-interface-02.idl")?.readText()!!
		val model = Funghi().parse(fileContent, "input-interfaces-02.idl")

		val sortedInterfaces = model.interfaces.sortedBy { it.name }
		assert(sortedInterfaces.map { it.name }.joinToString(" ") == "A B")

		// Interface A
		val interfaceA = sortedInterfaces[0]
		assert(interfaceA.isMixin)
		assert(interfaceA.attributes.size == 1)
		assert(interfaceA.attributes[0].types.size == 1)
		assert(interfaceA.attributes[0].name == "a")
		assert(interfaceA.attributes[0].types[0].name == "double")
		assert(interfaceA.operationConstructors.isEmpty())
		assert(interfaceA.operations.isEmpty())


		// Interface B
		val interfaceB = sortedInterfaces[1]
		assert(!interfaceB.isMixin)
		assert(interfaceB.attributes.size == 3)
		assert(interfaceB.attributes[0].types.size == 1)
		assert(interfaceB.attributes[0].name == "b")
		assert(interfaceB.attributes[0].types[0].name == "A")
		assert(interfaceB.attributes[1].types.size == 1)
		assert(interfaceB.attributes[1].name == "c")
		assert(interfaceB.attributes[1].types[0].name == "Int32Array")
		assert(interfaceB.attributes[2].types.size == 1)
		assert(interfaceB.attributes[2].name == "c2")
		assert(interfaceB.attributes[2].types[0].name == "B")
		assert(interfaceB.operationConstructors.isEmpty())
		assert(interfaceB.operations.isEmpty())
	}


	@Test
	fun `Should manage to fully parse the file input-interfaces-03 dot idl`() {
		val fileContent = this::class.java.classLoader.getResource("input-interface-03.idl")?.readText()!!
		val model = Funghi().parse(fileContent, "input-interfaces-03.idl")

		val sortedInterfaces = model.interfaces.sortedBy { it.name }
		assert(sortedInterfaces.map { it.name }.joinToString(" ") == "A B") {
			println(sortedInterfaces.map { it.name }.joinToString(" ") )
		}

		val a = sortedInterfaces[0]
		assert(a.name == "A")
		assert(a.isMixin)
		assert(a.operationConstructors.size == 1)
		assert(a.operationConstructors.toList()[0].arguments.isEmpty())
		assert(a.attributes.size == 1)
		//assert(a.operationConstructors[0].parameters.size == 0)

		val b = sortedInterfaces[1]
		assert(b.name == "B")
		assert(!b.isMixin)
		assert(b.operationConstructors.size == 2)
		assert(b.attributes.size == 3)
	}
}