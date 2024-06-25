import com.github.jdw.funghi.Funghi
import java.io.File
import kotlin.test.Test

class ParserTestInterfacesConstants {
	@Test
	fun `Should manage to use IdlModel toString for testing typedef file no 12`() {
		val fileContent = this::class.java.classLoader.getResource("input-interface-12.idl")?.readText()!!
		val model = Funghi().parse(fileContent, "input-interface-12.idl")
		val fileContentWithoutCommentsFormattedToOurLiking = """interface A {
	const unsigned long POINTS = 0x0000;
	const unsigned long LINES = 0x0001;
	const unsigned long LINE_LOOP = 0x0002;
	const unsigned long LINE_STRIP = 0x0003;
	const unsigned long TRIANGLES = 0x0004;
	const unsigned long TRIANGLE_STRIP = 0x0005;
	const unsigned long long TRIANGLE_FAN = 0x0006;
	const double TESTING_DOUBLE = 123.456;
	const bigint TESTING_INTEGER = 123456;
	const float TESTING_FLOAT = 654.321;
	const unrestricted float TESTING_MINUS_INFINITY = -Infinity;
	const unrestricted double TESTING_POSITIVE_INFINITY = Infinity;
	const double TESTING_NAN = NaN;
	const double TESTING_BOOLEAN_TRUE = true;
	const double TESTING_BOOLEAN_FALSE = false;
};"""

		assert(fileContentWithoutCommentsFormattedToOurLiking == model.toString()) {
			println(fileContentWithoutCommentsFormattedToOurLiking)
			println("---")
			println(model.toString())
		}
	}
}