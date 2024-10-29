package com.jiganaut.bonsai.sample;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.parser.Tokenizer;
import com.jiganaut.bonsai.parser.TokenizerFactory;

class TokenizerFactoryTest {

    private final String INPUT = "The sun sets behind the mountains.\n"
            + "Raindrops tap against the windowpane.\r"
            + "The starry \"sky\" envelops the silence.\r\n"
            + "The wind sways the \"trees\" gently.\n\r"
            + "\"A moment when the heart feels full.\"";

    @Test
    void testStringLiteralTokenizerFactory() throws Exception {
        var factory = TokenizerFactory.loadFactory(StringLiteralTokenizerFactory.class.getCanonicalName());
        var tokenizer = factory.createTokenizer(new StringReader(INPUT));

        var sb = new StringBuilder();
        while (tokenizer.hasNext()) {
            tokenizer.next();
            var token = tokenizer.getValue();
            if (token.startsWith("\"")) {
                sb.append(token);
                System.out.print("[" + token + "]");
            } else {
                System.out.print(token);
            }
        }
        assertEquals("\"sky\"\"trees\"\"A moment when the heart feels full.\"", sb.toString());
    }

    @Test
    void testWordTokenizerFactory() throws Exception {
        var factory = TokenizerFactory.loadFactory(WordTokenizerFactory.class.getCanonicalName());
        var tokenizer = factory.createTokenizer(new StringReader(INPUT));

        var sb = new StringBuilder();
        while (tokenizer.hasNext()) {
            tokenizer.next();
            var token = tokenizer.getValue();
            sb.append(token);
            System.out.print("[" + token + "]");
        }
        assertEquals(INPUT, sb.toString());
    }
    
    @Test
    void testCrLfTokenizerFactory() throws Exception {
        var factory = TokenizerFactory.loadFactory(CrLfTokenizerFactory.class.getCanonicalName());
        var tokenizer = factory.createTokenizer(new StringReader(INPUT));
        
        var sb = new StringBuilder();
        while (tokenizer.hasNext()) {
            tokenizer.next();
            var token = tokenizer.getValue();
            sb.append(token);
            System.out.print("[" + token + "]");
        }
        assertEquals(INPUT, sb.toString());
    }

    @Test
    void testCombination() throws Exception {
        TokenizerFactory factory;
        Tokenizer tokenizer;

        factory = TokenizerFactory.loadFactory(CrLfTokenizerFactory.class.getCanonicalName());
        tokenizer = factory.createTokenizer(new StringReader(INPUT));
        
        factory = TokenizerFactory.loadFactory(StringLiteralTokenizerFactory.class.getCanonicalName());
        tokenizer = factory.createTokenizer(tokenizer);

        factory = TokenizerFactory.loadFactory(WordTokenizerFactory.class.getCanonicalName());
        tokenizer = factory.createTokenizer(tokenizer);

        var sb = new StringBuilder();
        while (tokenizer.hasNext()) {
            tokenizer.next();
            var token = tokenizer.getValue();
            sb.append(token);
            System.out.print("[" + token + "]");
        }
        assertEquals(INPUT, sb.toString());
    }
}
