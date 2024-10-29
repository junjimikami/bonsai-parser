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
            .add("PRODUCTION_RULE", Rules.concat(
                    Rules.reference("SYMBOL"),
                    Rules.reference("EQUAL"),
                    Rules.reference("RULES"),
                    Rules.reference("SEMICOLON")))
            .add("SYMBOL", Rules.pattern("\\w+"))

            .add("RULES", Rules.concat(
                    Rules.reference("RULE"),
                    Rules.reference("RULE_2").opt()))
            .add("RULE", Rules.oneOf(
                    Rules.reference("SYMBOL"),
                    Rules.reference("PATTERN"),
                    Rules.reference("OPTION"),
                    Rules.reference("ZERO_OR_MORE")))
            .add("RULE_2", Rules.oneOf(
                    Rules.reference("OR_RULE").oneOrMore(),
                    Rules.reference("AND_RULE").oneOrMore()))
            .add("OR_RULE", Rules.concat(
                    Rules.reference("VERTICAL_BAR"),
                    Rules.reference("RULE")))
            .add("AND_RULE", Rules.concat(
                    Rules.reference("COMMA"),
                    Rules.reference("RULE")))
            .add("OPTION", Rules.concat(
                    Rules.reference("LSQB"),
                    Rules.reference("RULES"),
                    Rules.reference("RSQB")))
            .add("ZERO_OR_MORE", Rules.concat(
                    Rules.reference("LCUB"),
                    Rules.reference("RULES"),
                    Rules.reference("RCUB")))

            .add("PATTERN", Rules.concat(
                    Rules.pattern("\""),
                    Rules.pattern("([\\\\][\"]|[\\\\][\\\\]|[\\\\]x[0-9A-Fa-f][0-9A-Fa-f]|[^[\\\\]\"\r\n])*"),
                    Rules.pattern("\"")))
            .add("EQUAL", Rules.pattern("="))
            .add("DOUBLE_QUOTE", Rules.pattern("\""))
            .add("VERTICAL_BAR", Rules.pattern("\\|"))
            .add("COMMA", Rules.pattern(","))
            .add("LSQB", Rules.pattern("\\["))
            .add("RSQB", Rules.pattern("\\]"))
            .add("LCUB", Rules.pattern("\\{"))
            .add("RCUB", Rules.pattern("\\}"))
            .add("SEMICOLON", Rules.pattern(";"))
            .add("SKIP", Rules.pattern("\\s").skip())
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
