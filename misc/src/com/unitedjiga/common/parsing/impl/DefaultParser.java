package com.unitedjiga.common.parsing.impl;

import java.util.Set;

import com.unitedjiga.common.parsing.NonTerminal;
import com.unitedjiga.common.parsing.Parser;
import com.unitedjiga.common.parsing.ParsingException;
import com.unitedjiga.common.parsing.Terminal;
import com.unitedjiga.common.parsing.Tokenizer;
import com.unitedjiga.common.parsing.Tree;
import com.unitedjiga.common.parsing.grammar.Grammar;
import com.unitedjiga.common.parsing.grammar.Production;

class DefaultParser implements Parser {
    private final Production production;
    private final Context context;

    DefaultParser(Grammar grammar, Tokenizer tokenizer) {
        this.production = grammar.getStart();
        this.context = new Context(tokenizer, Set.of());
    }
    @Override
    public Tree parse() {
        var tree = Interpreter.parse(production, context);
        if (context.getTokenizer().hasNext()) {
            throw new ParsingException();//TODO:パースエラー
        }
        return tree;
    }
    
    @Override
    public NonTerminal parseNonTerminal() {
        return (NonTerminal) parse();
    }
    
    @Override
    public Terminal parseTerminal() {
        return (Terminal) parse();
    }
}
