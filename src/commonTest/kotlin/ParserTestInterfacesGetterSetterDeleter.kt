import com.github.jdw.funghi.Funghi
import java.io.File
import kotlin.test.Test

class ParserTestInterfacesGetterSetterDeleter {
	@Test
	fun `Should manage to use IdlModel toString for testing typedef file no 13`() {
		val fileContent = this::class.java.classLoader.getResource("input-interface-13.idl")?.readText()!!
		val model = Funghi().parse(fileContent, "input-interface-13.idl")
		val fileContentWithoutCommentsFormattedToOurLiking = """[Exposed=Window]
interface Dictionary {
	readonly attribute unsigned long propertyCount;
	getter double (DOMString propertyName);
	setter undefined (DOMString propertyName, double propertyValue);
	deleter undefined (DOMString propertyName);
};

[Exposed=Window]
interface Storage {
	DOMString? key(unsigned long index);
	getter DOMString? getItem(DOMString key);
	setter undefined setItem(DOMString key, DOMString value);
	deleter undefined removeItem(DOMString key);
	undefined clear();

	readonly attribute unsigned long length;
};"""

		assert(fileContentWithoutCommentsFormattedToOurLiking == model.toString()) {
			println(fileContentWithoutCommentsFormattedToOurLiking)
			println("---")
			println(model.toString())
		}
	}
}