package com.unitedjiga.commontest.parsing;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

import com.unitedjiga.common.parsing.Lexer;
import com.unitedjiga.common.parsing.Token;
import com.unitedjiga.common.parsing.Tokenizer;
import com.unitedjiga.common.parsing.TokenizerFactory;

class TokenizerTest {

	@Test
	void sample() throws IOException {
    	String input = "_a__b___c____|____d___e__f_";
		try (Lexer lexer = new Lexer(new StringReader(input));
				Tokenizer tzer = TokenizerFactory.createTokenizer(lexer)) {
			lexer.setWhitespaceChars('_').setLineTerminatorChars('|');
			while (tzer.hasNext()) {
				Token t = tzer.next();
				System.out.println(t);
				System.out.println(t.skippedWhitespace());
				System.out.println(t.getValue());
			}
			Token t = tzer.peek();
			System.out.println(t);
			System.out.println(t.skippedWhitespace());
			System.out.println(t.getValue());
		}
	}
}
