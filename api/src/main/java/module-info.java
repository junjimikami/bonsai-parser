module com.jiganaut.bonsai {
    exports com.jiganaut.bonsai.parser;
    exports com.jiganaut.bonsai.grammar;

    uses com.jiganaut.bonsai.parser.TokenizerFactory;
    uses com.jiganaut.bonsai.parser.ParserFactory;
    uses com.jiganaut.bonsai.parser.spi.TokenizerFactoryProvider;
    uses com.jiganaut.bonsai.parser.spi.ParserFactoryProvider;
}