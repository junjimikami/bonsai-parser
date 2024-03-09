import com.jiganaut.bonsai.sample.CrLfTokenizerFactory;
import com.jiganaut.bonsai.sample.GrammarParserFactory;
import com.jiganaut.bonsai.sample.StringLiteralTokenizerFactory;
import com.jiganaut.bonsai.sample.WordTokenizerFactory;

module com.jiganaut.bonsai.sample {
    requires com.jiganaut.bonsai;

    provides com.jiganaut.bonsai.parser.TokenizerFactory with WordTokenizerFactory, StringLiteralTokenizerFactory, CrLfTokenizerFactory;
    provides com.jiganaut.bonsai.parser.ParserFactory with GrammarParserFactory;
}