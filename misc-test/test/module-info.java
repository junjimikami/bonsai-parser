module com.unitedjiga.commontest.misc {
    requires com.unitedjiga.common.misc;
    requires junit;
    requires org.junit.jupiter.api;
    
    provides com.unitedjiga.common.parsing.TokenizerFactory
            with com.unitedjiga.commontest.parsing.sp.TestTokenizerFactory,
            com.unitedjiga.commontest.parsing.sp.JavaTokenizerFactory;
    provides com.unitedjiga.common.parsing.ParserFactory with com.unitedjiga.commontest.parsing.sp.TestParserFactory;
}