package com.jiganaut.bonsai.sample;

import java.io.Reader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.Rules;
import com.jiganaut.bonsai.parser.Tokenizer;
import com.jiganaut.bonsai.parser.TokenizerFactory;

public class StringLiteralTokenizerFactory implements TokenizerFactory {
    private static final Grammar GRAMMAR = Grammar.builder()
            .add("STRING_LITERAL", Rules.of(
                    Rules.reference("DOUBLE_QUOTATION"),
                    Rules.reference("CHARACTER").zeroOrMore(),
                    Rules.reference("DOUBLE_QUOTATION")))
            .add("STRING_LITERAL", Rules.pattern("(?s)[^\"].*"))
            .add("DOUBLE_QUOTATION", Rules.pattern("\""))
            .add("CHARACTER", Rules.oneOfreferences("NON_ESCAPED", "ESCAPED"))
            .add("NON_ESCAPED", Rules.pattern("[^\"\r\n\\\\]"))
            .add("ESCAPED", Rules.of(
                    Rules.pattern("\\\\"),
                    Rules.oneOfreferences("DQUOTE", "BSLASH", "XHH")))
            .add("DQUOTE", Rules.pattern("\""))
            .add("BSLASH", Rules.pattern("\\\\"))
            .add("XHH", Rules.ofPatterns("x", "[0-9a-fA-F]", "[0-9a-fA-F]"))
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
