import com.github.jdw.funghi.Funghi
import java.io.File
import kotlin.test.Test

class ParserTestInterfacesUnionTypes {
	@Test
	fun `Should manage to use IdlModel toString for testing typedef file no 8`() {
		val fileContent = this::class.java.classLoader.getResource("input-interface-08.idl")?.readText()!!
		val model = Funghi().parse(fileContent, "input-interface-08.idl")

		assert(fileContent == model.toString()) {
			println(fileContent)
			println("---")
			println(model.toString())
		}
	}


	@Test
	fun `Should manage to use IdlModel toString for testing typedef file no 9`() {
		val fileContent = this::class.java.classLoader.getResource("input-interface-09.idl")?.readText()!!
		val model = Funghi().parse(fileContent, "input-interface-09.idl")
		val fileContentWithoutCommentsFormattedToOurLiking = """interface A {
};

interface B {
};

interface D {
	attribute A a;
	attribute (A or B) b;
	attribute (long long or unsigned long long or B)? allNullable;
	attribute (long long? or unsigned long long? or A) someNullable;
};"""

		assert(fileContentWithoutCommentsFormattedToOurLiking == model.toString()) {
			println(fileContentWithoutCommentsFormattedToOurLiking)
			println("---")
			println(model.toString())
		}
	}
}