package com.unitedjiga.commontest.parsing;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import com.unitedjiga.common.parsing.Token;
import com.unitedjiga.common.parsing.Tokenizer;
import com.unitedjiga.common.parsing.TokenizerFactory;

class TokenizerTest {

    @Test
    void sample() throws IOException {
        String input = "123 456";

        TokenizerFactory tf = TokenizerFactory.newInstance();
        try (Tokenizer tzer = tf.createTokenizer(new StringReader(input))) {
            while (tzer.hasNext()) {
                Token t = tzer.next();
//				System.out.println(t);
//				System.out.println(t.skippedWhitespace());
                System.out.print(">");
                System.out.println(t.getValue());
                assertEquals(t.toString(), t.getValue());
            }
            assertThrows(NoSuchElementException.class, () -> tzer.next());
            Token t = tzer.peek();
//			System.out.println(t);
//			System.out.println(t.skippedWhitespace());
            System.out.print(">>");
            System.out.println(t.getValue());
            assertEquals(t.toString(), t.getValue());
        }
    }
}
