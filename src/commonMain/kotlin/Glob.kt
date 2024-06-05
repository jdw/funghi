import com.github.jdw.funghi.parser.ParserSettings
import com.github.jdw.funghi.pieces.Pieces

object Glob {
	val version = "0.0.1"
	var filename = ""
	var pieces: Pieces? = null
	var currentParserSettings: ParserSettings? = null
	val keywordStart = "___FUNGHI_KEYWORD_"
	val lineNumberKeyword = "${keywordStart}LINE_NUMBER_"
	val emptyArrayKeyword = "${keywordStart}EMPTY_ARRAY"
	val emptyDictionaryKeyword = "${keywordStart}EMPTY_DICTIONARY"
	val startScopeKeyword = "${keywordStart}_START_SCOPE_"
	val endScopeKeyword = "${keywordStart}_EMD_SCOPE_"
	var lineNumber = ""
	fun getLineNumber(): Int = lineNumber.replace(Glob.lineNumberKeyword, "").toInt()
}