import com.github.jdw.funghi.parser.ParserSettings
import com.github.jdw.funghi.pieces.Pieces

object Glob {
	val version = "0.0.1"
	var filename = ""
	var pieces: Pieces? = null
	var parserSettings: ParserSettings? = null
	const val keywordStart = "___FUNGHI_KEYWORD_"
	val lineNumberKeyword = "${keywordStart}LINE_NUMBER_"
	val emptyArrayKeyword = "${keywordStart}EMPTY_ARRAY"
	val emptyDictionaryKeyword = "${keywordStart}EMPTY_DICTIONARY"
	val extendedAttributeWildcardKeyword = "${keywordStart}EXTENDED_ATTRIBUTES_WILDCARD"
	val nullKeyword = "${keywordStart}NULL"
	val emptyStringKeyword = "${keywordStart}EMPTY_STRING"
	val startScopeKeyword = "${keywordStart}START_SCOPE_"
	val stopScopeKeyword = "${keywordStart}END_SCOPE_"
	val nextScopeKeyword = "${keywordStart}NEXT_SCOPE_"
	var lineNumber = ""


	fun getLineNumber(): Int {
		return try {
			lineNumber.replace(Glob.lineNumberKeyword, "").toInt()
		}
		catch (_: Exception) {
			-1
		}
	}
}