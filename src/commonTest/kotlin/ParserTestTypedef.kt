import com.github.jdw.funghi.Funghi
import java.io.File
import kotlin.test.Test

class ParserTestTypedef {
	@Test
	fun `Should manage to use IdlModel toString for testing typedef file no 1`() {
		val fileContent = this::class.java.classLoader.getResource("input-typedef-01.idl")?.readText()!!
		val model = Funghi().parse(fileContent, "input-typedef-01.idl")

		assert(fileContent == model.toString()) {
			println(fileContent)
			println("---")
			println(model.toString())
		}
	}


	@Test
	fun `Should manage to use IdlModel toString for testing typedef file no 3`() {
		val fileContent = this::class.java.classLoader.getResource("input-typedef-03.idl")?.readText()!!
		val model = Funghi().parse(fileContent, "input-typedef-03.idl")
		val fileContentWithoutCommentsFormattedToOurLiking = """typedef unsigned long GLenum;

typedef boolean GLboolean;

typedef unsigned long GLbitfield;

typedef byte GLbyte;

typedef short GLshort;

typedef long GLint;

typedef long GLsizei;

typedef long long GLintptr;

typedef long long GLsizeiptr;

typedef octet GLubyte;

typedef unsigned short GLushort;

typedef unsigned long GLuint;

typedef unrestricted float GLfloat;

typedef unrestricted float GLclampf;"""

		assert(fileContentWithoutCommentsFormattedToOurLiking == model.toString())
	}
}