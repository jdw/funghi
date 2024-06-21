import com.github.jdw.funghi.Funghi
import kotlin.test.Test

class ParserTestInterfacesExtendedAttributes {
	@Test
	fun `Should manage to use IdlModel toString for testing interface file no 6`() {
		val fileContent = this::class.java.classLoader.getResource("input-interface-06.idl")?.readText()!!
		val model = Funghi().parse(fileContent, "input-interfaces-06.idl")

		assert(fileContent == model.toString()) {
			println(fileContent)
			println("---")
			println(model.toString())
		}
	}
}