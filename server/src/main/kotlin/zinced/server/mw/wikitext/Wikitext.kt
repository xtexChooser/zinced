package zinced.server.mw.wikitext

import org.sweble.wikitext.parser.WikitextEncodingValidator
import org.sweble.wikitext.parser.WikitextParser
import org.sweble.wikitext.parser.WikitextPreprocessor
import org.sweble.wikitext.parser.nodes.WtNode
import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage
import org.sweble.wikitext.parser.nodes.WtPreproWikitextPage
import org.sweble.wikitext.parser.parser.PreprocessorToParserTransformer
import org.sweble.wikitext.parser.utils.WtAstPrinter
import org.sweble.wikitext.parser.utils.WtPrettyPrinter

object Wikitext {

    fun newParser() = WikitextParser(WikitextParserConfig)
    fun newPreprocessor() = WikitextPreprocessor(WikitextParserConfig)
    fun newEncodingValidator() = WikitextEncodingValidator()

    fun formatAST(ast: WtNode) = WtAstPrinter.print(ast)
    fun formatASTPretty(ast: WtNode) = WtPrettyPrinter.print(ast)

    fun parseAST(title: String, source: String): WtParsedWikitextPage {
        val validated = newEncodingValidator().validate(WikitextParserConfig, source, title)
        val preprocessedPage = newPreprocessor().parseArticle(validated, title, false) as WtPreproWikitextPage
        val preprocessed = PreprocessorToParserTransformer.transform(preprocessedPage)
        return newParser().parseArticle(preprocessed, title) as WtParsedWikitextPage
    }

    fun parse(displayTitle: String, source: String) =
        ParsedWikitext(displayTitle = displayTitle, tree = parseAST(displayTitle, source))

}