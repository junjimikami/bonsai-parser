package com.unitedjiga.common.parsing;

import java.io.Reader;

import com.unitedjiga.common.parsing.grammar.Grammar;
import com.unitedjiga.common.parsing.impl.TokenizerService;

/**
 * 
 * @author Junji Mikami
 *
 */
public interface TokenizerFactory {

    public static TokenizerFactory newFactory(Grammar grammar) {
        return TokenizerService.createFactory(grammar);
    }
    
    public static TokenizerFactory loadFactory(String factoryName, ClassLoader cl) {
        return TokenizerService.loadFactory(factoryName, cl);
    }

    public static TokenizerFactory loadFactory(String factoryName) {
        return TokenizerService.loadFactory(factoryName, null);
    }

    public Tokenizer createTokenizer(Reader reader);
    public Tokenizer createTokenizer(Tokenizer tokenizer);
}
