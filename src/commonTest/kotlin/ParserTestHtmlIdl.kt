import com.github.jdw.funghi.Funghi
import kotlin.test.Test

class ParserTestHtmlIdl {
	@Test
	fun `Should manage to fully parse the file`() {
		val fileContent = this::class.java.classLoader.getResource("html.idl")?.readText()!!
		val model = Funghi().parse(fileContent, "html.idl")

		var interfaze = model.interfaces.find { it.name == "HTMLAllCollection" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLFormControlsCollection" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "RadioNodeList" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLOptionsCollection" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "DOMStringList" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "Document" } 
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "DocumentOrShadowRoot" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLUnknownElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLOrSVGElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "DOMStringMap" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLHtmlElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLHeadElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLTitleElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLBaseElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLLinkElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLMetaElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLStyleElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLBodyElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLHeadingElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLParagraphElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLHRElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLPreElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLQuoteElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLOListElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLUListElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLMenuElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLLIElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLDListElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLDivElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLAnchorElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLDataElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLTimeElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLSpanElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLBRElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLHyperlinkElementUtils" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLModElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLPictureElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLSourceElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLImageElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLIFrameElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLEmbedElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLObjectElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLVideoElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLAudioElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLTrackElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "HTMLMediaElement" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "MediaError" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "AudioTrackList" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "AudioTrack" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "VideoTrackList" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "VideoTrack" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "TextTrackList" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "TextTrack" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "TextTrackCueList" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "TextTrackCue" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "TimeRanges" }
		assert(null != interfaze)
		interfaze = model.interfaces.find { it.name == "TrackEvent" }
		assert(null != interfaze)
	}
}