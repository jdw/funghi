import com.github.jdw.funghi.parser.ParserSettings

object Glob {
	val version = "0.0.1"
	var currentParserSettings: ParserSettings? = null
	private val keywordStart = "___FUNGHI_KEYWORD_"
	val lineNumberKeyword = "${keywordStart}LINE_NUMBER_"
	val emptyArrayKeyword = "${keywordStart}EMPTY_ARRAY"
	val emptyDictionaryKeyword = "${keywordStart}EMPTY_DICTIONARY"

	val includesStartScopeKeyword = "${keywordStart}INCLUDES_START_SCOPE"
	val includesEndScopeKeyword = "${keywordStart}INCLUDES_END_SCOPE"

	val constAttributeStartScopeKeyword = "${keywordStart}CONSTANT_ATTRIBUTE_START_SCOPE"
	val constAttributeEndScopeKeyword = "${keywordStart}CONSTANT_ATTRIBUTE_END_SCOPE"

	val typedefStartScopeKeyword = "${keywordStart}TYPEDEF_START_SCOPE"
	val typedefEndScopeKeyword = "${keywordStart}TYPEDEF_END_SCOPE"

	val interfaceStartScopeKeyword = "${keywordStart}INTERFACE_START_SCOPE"
	val interfaceEndScopeKeyword = "${keywordStart}INTERFACE_END_SCOPE"

	val dictionaryStartScopeKeyword = "${keywordStart}DICTIONARY_START_SCOPE"
	val dictionaryEndScopeKeyword = "${keywordStart}DICTIONARY_END_SCOPE"

	val dictionaryMemberStartScopeKeyword = "${keywordStart}DICTIONARY_MEMBER_START_SCOPE"
	val dictionaryMemberEndScopeKeyword = "${keywordStart}DICTIONARY_MEMBER_END_SCOPE"

	val enumStartScopeKeyword = "${keywordStart}ENUM_START_SCOPE_SCOPE"
	val enumEndScopeKeyword = "${keywordStart}ENUM_END_SCOPE_SCOPE"

	val operationStartScopeKeyword = "${keywordStart}OPERATION_START_SCOPE"
	val operationEndScopeKeyword = "${keywordStart}OPERATION_END_SCOPE"

	val operationConstructorStartScopeKeyword = "${keywordStart}OPERATION_CONSTRUCTOR_START_SCOPE"
	val operationConstructorEndScopeKeyword = "${keywordStart}OPERATION_CONSTRUCTOR_END_SCOPE"

	val attributeStartScopeKeyword = "${keywordStart}ATTRIBUTE_START_SCOPE"
	val attributeEndScopeKeyword = "${keywordStart}ATTRIBUTE_END_SCOPE"

	val extendedAttributeStartScopeKeyword = "${keywordStart}EXTENDED_ATTRIBUTE_START_SCOPE"
	val extendedAttributeEndScopeKeyword = "${keywordStart}EXTENDED_ATTRIBUTE_END_SCOPE"

	var lineNumber = ""
	fun getLineNumber(): Int = lineNumber.replace(Glob.lineNumberKeyword, "").toInt()
}