import com.github.jdw.funghi.Funghi
import java.io.File
import kotlin.test.Test

class ParserTestEnum {
	@Test
	fun `Should manage to use IdlModel toString for testing enum file no 1`() {
		val fileContent = this::class.java.classLoader.getResource("input-enum-01.idl")?.readText()!!
		val model = Funghi().parse(fileContent, "input-enum-01.idl")

//		File("test-model.txt").printWriter().use { out ->
//			out.write(model.toString())
//		}
		assert(fileContent == model.toString()) {
			println(fileContent)
			println("---")
			println(model.toString())
		}
	}


	@Test
	fun `Should manage to use IdlModel toString for testing enum file no 2`() {
		val fileContent = this::class.java.classLoader.getResource("input-enum-02.idl")?.readText()!!
		val model = Funghi().parse(fileContent, "input-enum-02.idl")

//		File("test-model.txt").printWriter().use { out ->
//			out.write(model.toString())
//		}

		val names = model.enums.map { it.name }.toList()
		assert(names.contains("SelectionMode"))
		assert(names.contains("DOMParserSupportedType"))
		assert(names.contains("CanPlayTypeResult"))
		assert(!names.contains(""))
		assert(!names.contains("Test"))

		model.enums.forEach { enum ->
			if ("SelectionMode" == enum.name) {
				assert(enum.values.contains("select"))
				assert(enum.values.contains("start"))
				assert(enum.values.contains("end"))
				assert(enum.values.contains("preserve"))
			}
			else if ("DOMParserSupportedType" == enum.name) {
				assert(enum.values.contains("text/html"))
				assert(enum.values.contains("text/xml"))
				assert(enum.values.contains("application/xml"))
				assert(enum.values.contains("application/xhtml+xml"))
				assert(enum.values.contains("image/svg+xml"))
			}
			else if ("CanPlayTypeResult" == enum.name) {
				assert(enum.values.contains(""))
				assert(enum.values.contains("maybe"))
				assert(enum.values.contains("probably"))
			}
			else assert(false)
		}
	}
}