package com.jiganaut.bonsai.sample;

import java.io.Reader;

import com.jiganaut.bonsai.grammar.ProductionSet;
import com.jiganaut.bonsai.grammar.Rules;
import com.jiganaut.bonsai.parser.Tokenizer;
import com.jiganaut.bonsai.parser.TokenizerFactory;

public class StringLiteralTokenizerFactory implements TokenizerFactory {
    private static final ProductionSet GRAMMAR = ProductionSet.builder()
            .add("STRING_LITERAL", Rules.concat(
                    Rules.reference("DOUBLE_QUOTATION"),
                    Rules.reference("CHARACTER").zeroOrMore(),
                    Rules.reference("DOUBLE_QUOTATION")))
            .add("STRING_LITERAL", Rules.pattern("(?s)[^\"].*"))
            .add("DOUBLE_QUOTATION", Rules.pattern("\""))
            .add("CHARACTER", Rules.oneOf(
                    Rules.reference("NON_ESCAPED"),
                    Rules.reference("ESCAPED")))
            .add("NON_ESCAPED", Rules.pattern("[^\"\r\n\\\\]"))
            .add("ESCAPED", Rules.concat(
                    Rules.pattern("\\\\"),
                    Rules.oneOf(
                            Rules.reference("DQUOTE"),
                            Rules.reference("BSLASH"),
                            Rules.reference("XHH"))))
            .add("DQUOTE", Rules.pattern("\""))
            .add("BSLASH", Rules.pattern("\\\\"))
            .add("XHH", Rules.concat(
                    Rules.pattern("x"),
                    Rules.pattern("[0-9a-fA-F]"),
                    Rules.pattern("[0-9a-fA-F]")))
            .build();
    private static final TokenizerFactory FACTORY = TokenizerFactory.newFactory(GRAMMAR);

    @Override
    public Tokenizer createTokenizer(Reader reader) {
        return FACTORY.createTokenizer(reader);
    }

    @Override
    public Tokenizer createTokenizer(Tokenizer tokenizer) {
        return FACTORY.createTokenizer(tokenizer);
    }

}
