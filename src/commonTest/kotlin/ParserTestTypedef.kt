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

		assert(fileContentWithoutCommentsFormattedToOurLiking == model.toString()) {
			println(fileContentWithoutCommentsFormattedToOurLiking)
			println("---")
			println(model.toString())
		}
	}


	@Test
	fun `Should manage to use IdlModel toString for testing typedef file no 2`() {
		val fileContent = this::class.java.classLoader.getResource("input-typedef-02.idl")?.readText()!!
		val model = Funghi().parse(fileContent, "input-typedef-02.idl")
		val fileContentWithoutCommentsFormattedToOurLiking = """typedef (HTMLScriptElement or SVGScriptElement) HTMLOrSVGScriptElement;

typedef (MediaStream or MediaSource or Blob) MediaProvider;

typedef (CanvasRenderingContext2D or ImageBitmapRenderingContext or WebGLRenderingContext or WebGL2RenderingContext or GPUCanvasContext) RenderingContext;

typedef (HTMLImageElement or SVGImageElement)? HTMLOrSVGImageElement;

typedef (HTMLOrSVGImageElement or HTMLVideoElement or HTMLCanvasElement or ImageBitmap or OffscreenCanvas or VideoFrame) CanvasImageSource;

typedef (OffscreenCanvasRenderingContext2D or ImageBitmapRenderingContext or WebGLRenderingContext or WebGL2RenderingContext or GPUCanvasContext) OffscreenRenderingContext;

typedef (DOMString? or Function) TimerHandler;

typedef (CanvasImageSource or Blob or ImageData) ImageBitmapSource;

typedef (WindowProxy or MessagePort or ServiceWorker)? MessageEventSource;

typedef (Float32Array or sequence<GLfloat>) Float32List;

typedef (Int32Array or sequence<unsigned long long>) Int32List;

typedef (sequence<GLuint> or Uint32Array) Uint32List;"""

		assert(model.typedefs.size == 12)
		assert(model.toString() == fileContentWithoutCommentsFormattedToOurLiking) {
			println(fileContentWithoutCommentsFormattedToOurLiking)
			println("---")
			println(model.toString())
		}

		val timerHandler = model.typedefs.first { it.identifier == "TimerHandler" }
		assert(timerHandler.types.size == 2)
		assert(timerHandler.types.filter { it.isNullable && it.name == "DOMString" }.size == 1)

		val hTMLOrSVGImageElement = model.typedefs.first { it.identifier == "HTMLOrSVGImageElement" }
		assert(hTMLOrSVGImageElement.types.filter { it.isNullable }.size == 2)
	}
}