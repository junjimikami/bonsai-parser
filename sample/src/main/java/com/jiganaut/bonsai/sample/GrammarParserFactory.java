package com.jiganaut.bonsai.sample;

import java.io.Reader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.Rules;
import com.jiganaut.bonsai.parser.Parser;
import com.jiganaut.bonsai.parser.ParserFactory;
import com.jiganaut.bonsai.parser.Tokenizer;

public class GrammarParserFactory implements ParserFactory {
    private static final Grammar GRAMMAR = Grammar.builder()
            .add("GRAMMAR", Rules.reference("PRODUCTION_RULE").zeroOrMore())
            .add("PRODUCTION_RULE", Rules.ofReferences("SYMBOL", "EQUAL", "RULES", "SEMICOLON"))
            .add("SYMBOL", Rules.pattern("\\w+"))

            .add("RULES", Rules.of(
                    Rules.reference("RULE"),
                    Rules.reference("RULE_2").opt()))
            .add("RULE", Rules.oneOfreferences("SYMBOL", "PATTERN", "OPTION", "ZERO_OR_MORE"))
            .add("RULE_2", Rules.oneOf(
                    Rules.reference("OR_RULE").oneOrMore(),
                    Rules.reference("AND_RULE").oneOrMore()))
            .add("OR_RULE", Rules.ofReferences("VERTICAL_BAR", "RULE"))
            .add("AND_RULE", Rules.ofReferences("COMMA", "RULE"))
            .add("OPTION", Rules.ofReferences("LSQB", "RULES", "RSQB"))
            .add("ZERO_OR_MORE", Rules.ofReferences("LCUB", "RULES", "RCUB"))

            .add("PATTERN", Rules.ofPatterns(
                    "\"",
                    "([\\\\][\"]|[\\\\][\\\\]|[\\\\]x[0-9A-Fa-f][0-9A-Fa-f]|[^[\\\\]\"\r\n])*",
                    "\""))
            .add("EQUAL", Rules.pattern("="))
            .add("DOUBLE_QUOTE", Rules.pattern("\""))
            .add("VERTICAL_BAR", Rules.pattern("\\|"))
            .add("COMMA", Rules.pattern(","))
            .add("LSQB", Rules.pattern("\\["))
            .add("RSQB", Rules.pattern("\\]"))
            .add("LCUB", Rules.pattern("\\{"))
            .add("RCUB", Rules.pattern("\\}"))
            .add("SEMICOLON", Rules.pattern(";"))
            .setSkipPattern("\\s")
            .build();
    private static final ParserFactory FACTORY = ParserFactory.newFactory(GRAMMAR);

    @Override
    public Parser createParser(Tokenizer arg0) {
        return FACTORY.createParser(arg0);
    }

    @Override
    public Parser createParser(Reader arg0) {
        return FACTORY.createParser(arg0);
    }

}
