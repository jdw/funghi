import com.github.jdw.funghi.parser.ParserSettings
import com.github.jdw.funghi.pieces.Pieces
import org.junit.Test

class PiecesTest {

	@Test
	fun `Should handle every single type`() {
		ParserSettings.entries.forEach { version ->
			Glob.parserSettings = version
			val allKeywords = version.allPredefinedTypesKeywords().joinToString(separator = " ${Glob.lineNumberKeyword}53 ", prefix = "${Glob.lineNumberKeyword}5 ", postfix = " ${Glob.lineNumberKeyword}35")
			val pieces = Pieces(allKeywords)

			version.allPredefinedTypesKeywords().forEach { _ ->
				val (result, value) = pieces.popIfPresentSingleTypeThrowIfNot()

				assert(result)
				assert(version.allPredefinedTypesKeywords().contains(value)) {
					println(allKeywords)
					println()
					println("'$value' was missing!")
				}
			}
		}
	}


	@Test
	fun `Should handle every single type with array markers added`() {
		ParserSettings.entries.forEach { version ->
			Glob.parserSettings = version
			val allPredefinedKeywordsWitsArrayMarkers = mutableListOf<String>()
				.apply {
					version.arrayMarkers().forEach { marker ->
						version.allPredefinedTypesKeywords().forEach { keyword ->
							add("$keyword$marker")
						}
					}
				}
				.toList()
			val allKeywords = allPredefinedKeywordsWitsArrayMarkers.joinToString(separator = " ${Glob.lineNumberKeyword}53 ", prefix = "${Glob.lineNumberKeyword}5 ", postfix = " ${Glob.lineNumberKeyword}35")
			val pieces = Pieces(allKeywords)

			version.allPredefinedTypesKeywords().forEach { _ ->
				val (result, value) = pieces.popIfPresentSingleTypeThrowIfNot()

				assert(result)
				assert(version.allPredefinedTypesKeywords().containsRemoveMarkers(value)) {
					println(allKeywords)
					println()
					println("'$value' was missing!")
				}
			}
		}
	}


	@Test
	fun `Should handle every single type with nullable markers added`() {
		ParserSettings.entries.forEach { version ->
			Glob.parserSettings = version
			val allPredefinedKeywordsWitsArrayMarkers = mutableListOf<String>()
				.apply {
					version.nullableMarkers().forEach { marker ->
						version.allPredefinedTypesKeywords().forEach { keyword ->
							add("$keyword$marker")
						}
					}
				}
				.toList()
			val allKeywords = allPredefinedKeywordsWitsArrayMarkers.joinToString(separator = " ${Glob.lineNumberKeyword}53 ", prefix = "${Glob.lineNumberKeyword}5 ", postfix = " ${Glob.lineNumberKeyword}35")
			val pieces = Pieces(allKeywords)

			version.allPredefinedTypesKeywords().forEach { _ ->
				val (result, value) = pieces.popIfPresentSingleTypeThrowIfNot()

				assert(result)
				assert(version.allPredefinedTypesKeywords().containsRemoveMarkers(value)) {
					println(allKeywords)
					println()
					println("'$value' was missing!")
				}
			}
		}
	}


	@Test
	fun `Should handle every single type with both array and nullable markers added`() {
		ParserSettings.entries.forEach { version ->
			Glob.parserSettings = version
			val allPredefinedKeywordsWitsArrayMarkers = mutableListOf<String>()
				.apply {
					version.arrayMarkers().forEach { arrayMarker ->
						version.nullableMarkers().forEach { nullableMarker ->
							version.allPredefinedTypesKeywords().forEach { keyword ->
								add("$keyword$arrayMarker$nullableMarker")
							}
						}
					}
				}
				.toList()
			val allKeywords = allPredefinedKeywordsWitsArrayMarkers.joinToString(separator = " ${Glob.lineNumberKeyword}53 ", prefix = "${Glob.lineNumberKeyword}5 ", postfix = " ${Glob.lineNumberKeyword}35")
			val pieces = Pieces(allKeywords)

			version.allPredefinedTypesKeywords().forEach { _ ->
				val (result, value) = pieces.popIfPresentSingleTypeThrowIfNot()

				assert(result)
				assert(version.allPredefinedTypesKeywords().containsRemoveMarkers(value)) {
					println(allKeywords)
					println()
					println("'$value' was missing!")
				}
			}
		}
	}


	@Test
	fun `Should handle complex single types`() {
		ParserSettings.entries.forEach { version ->
			Glob.parserSettings = version
			val allKeywords = listOf("CanvasGradient", "CanvasPattern", "byte", "CanvasDrawPath").joinToString(separator = " ${Glob.lineNumberKeyword}53 ", prefix = "${Glob.lineNumberKeyword}5 ", postfix = " ${Glob.lineNumberKeyword}35")
			val pieces = Pieces(allKeywords)

			(0..3).forEach { _ ->
				val peekResult = pieces.peekIsPresentSingleType()
				assert(peekResult)

				val (popResult, value) = pieces.popIfPresentSingleTypeThrowIfNot()
				assert(popResult && allKeywords.contains(value)) {
					println(allKeywords)
					println()
					println("'$value' was missing!")
				}
			}
		}
	}


	@Test
	fun `Should handle complex single types with array marker`() {
		ParserSettings.entries.forEach { version ->
			Glob.parserSettings = version
			version.arrayMarkers().forEach { marker ->
				val allKeywords = listOf(
					"CanvasGradient$marker",
					"CanvasPattern$marker",
					"byte$marker",
					"CanvasDrawPath$marker"
				).joinToString(
					separator = " ${Glob.lineNumberKeyword}53 ",
					prefix = "${Glob.lineNumberKeyword}5 ",
					postfix = " ${Glob.lineNumberKeyword}35"
				)

				val pieces = Pieces(allKeywords)

				(0..3).forEach { _ ->
					val peekResult = pieces.peekIsPresentSingleType()
					assert(peekResult)

					val (popResult, value) = pieces.popIfPresentSingleTypeThrowIfNot()
					assert(popResult && allKeywords.contains(value) && value.containsArrayMarker()) {
						println(allKeywords)
						println()
						println("'$value' was missing!")
						println()
						println(popResult)
						println()
						println(value.containsArrayMarker())
					}
				}
			}
		}
	}


	@Test
	fun `Should handle complex single types with nullable marker`() {
		ParserSettings.entries.forEach { version ->
			Glob.parserSettings = version
			version.nullableMarkers().forEach { marker ->
				val allKeywords = listOf(
					"CanvasGradient$marker",
					"CanvasPattern$marker",
					"byte$marker",
					"CanvasDrawPath$marker"
				).joinToString(
					separator = " ${Glob.lineNumberKeyword}53 ",
					prefix = "${Glob.lineNumberKeyword}5 ",
					postfix = " ${Glob.lineNumberKeyword}35"
				)

				val pieces = Pieces(allKeywords)

				(0..3).forEach { _ ->
					val peekResult = pieces.peekIsPresentSingleType()
					assert(peekResult)

					val (popResult, value) = pieces.popIfPresentSingleTypeThrowIfNot()
					assert(popResult && allKeywords.contains(value) && value.containsArrayMarker()) {
						println(allKeywords)
						println()
						println("'$value' was missing!")
						println()
						println(popResult)
						println()
						println(value.containsArrayMarker())
					}
				}
			}
		}
	}


	@Test
	fun `Should handle complex single types with both array and nullable marker`() {
		ParserSettings.entries.forEach { version ->
			Glob.parserSettings = version
			version.arrayMarkers().forEach { arrayMarker ->
				version.nullableMarkers().forEach { nullableMarker ->
					val allKeywords = listOf(
						"CanvasGradient$arrayMarker$nullableMarker",
						"CanvasPattern$arrayMarker$nullableMarker",
						"byte$arrayMarker$nullableMarker",
						"CanvasDrawPath$arrayMarker$nullableMarker",
						"double"
					).joinToString(
						separator = " ${Glob.lineNumberKeyword}53 ",
						prefix = "${Glob.lineNumberKeyword}5 ",
						postfix = " ${Glob.lineNumberKeyword}35"
					)

					val pieces = Pieces(allKeywords)

					(0..3).forEach { _ ->
						val peekResult = pieces.peekIsPresentSingleType()
						assert(peekResult)

						val (popResult, value) = pieces.popIfPresentSingleTypeThrowIfNot()
						assert(popResult && allKeywords.contains(value) && value.containsArrayMarker()) {
							println(allKeywords)
							println()
							println("'$value' was missing!")
							println()
							println(popResult)
							println()
							println(value.containsArrayMarker())
						}
					}
				}
			}
		}
	}
}