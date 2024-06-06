import com.github.jdw.funghi.Funghi
import kotlin.test.Test

class ParserTestInterfaces {
	@Test
	fun `Should manage to fully parse the file input-interfaces-00 dot idl`() {
		val fileContent = this::class.java.classLoader.getResource("input-interface-00.idl")?.readText()!!
		val model = Funghi().parse(fileContent, "input-interfaces-00.idl")

		assert(model.interfaces.size == 5)
		assert(model.interfaces.sortedBy { it.name }.map { it.name }.joinToString(" ") == "A B C D E")
	}
}