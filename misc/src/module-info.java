module com.unitedjiga.common.misc {
    exports com.unitedjiga.common.parsing;
    exports com.unitedjiga.common.parsing.util;
    exports com.unitedjiga.common.util to com.unitedjiga.commontest.misc;
    
    uses com.unitedjiga.common.parsing.TokenizerFactory;
    uses com.unitedjiga.common.parsing.ParserFactory;
}