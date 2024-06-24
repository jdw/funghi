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


	@Test
	fun `Should manage to use IdlModel toString for testing typedef file no 10`() {
		val fileContent = this::class.java.classLoader.getResource("input-interface-10.idl")?.readText()!!
		val model = Funghi().parse(fileContent, "input-interface-10.idl")
		val fileContentWithoutCommentsFormattedToOurLiking = """interface A {
	constructor(optional double a);

};

interface B {
};

interface C {
	constructor(optional (double or float) a);

	undefined operationA(optional A a, optional double b);
	undefined operationB(optional (A or double or float?) a, double b);
	float operationC((float or double or bigint)? a);

};"""

		assert(fileContentWithoutCommentsFormattedToOurLiking == model.toString()) {
			println(fileContentWithoutCommentsFormattedToOurLiking)
			println("---")
			println(model.toString())
		}
	}


	@Test
	fun `Should manage to use IdlModel toString for testing typedef file no 11`() {
		val fileContent = this::class.java.classLoader.getResource("input-interface-11.idl")?.readText()!!
		val model = Funghi().parse(fileContent, "input-interface-11.idl")
		val fileContentWithoutCommentsFormattedToOurLiking = """interface A {
	constructor(optional (double or float) a);

	(double or float) operationA(optional A a, optional double b);
	(bigint or byte) operationB(optional (A or double or float?) a, double b);
	(bigint or byte?) operationC();
	(bigint or byte)? operationD();
	float operationE((float or double or bigint)? a);

};"""

		assert(fileContentWithoutCommentsFormattedToOurLiking == model.toString()) {
			println(fileContentWithoutCommentsFormattedToOurLiking)
			println("---")
			println(model.toString())
		}
	}
}