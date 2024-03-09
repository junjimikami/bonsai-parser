package com.jiganaut.bonsai.sample;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.Quantifiable;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.Rules;
import com.jiganaut.bonsai.parser.NonTerminal;
import com.jiganaut.bonsai.parser.NonTerminalVisitor;
import com.jiganaut.bonsai.parser.Parser;
import com.jiganaut.bonsai.parser.ParserFactory;
import com.jiganaut.bonsai.parser.Terminal;
import com.jiganaut.bonsai.parser.TokenizerFactory;
import com.jiganaut.bonsai.parser.Tree;
import com.jiganaut.bonsai.parser.TreeVisitor;

class GrammarParserFactoryTest {

    @Test
    void testGrammarParserFactory() throws Exception {
        /**
         * CSV (RFC 4180)
         */
        var input = """
                file        = [header, CRLF], record, {CRLF, record}, [CRLF];
                header      = name, {COMMA, name};
                record      = field, {COMMA, field};
                name        = field;
                field       = escaped | non_escaped;
                escaped     = DQUOTE, {TEXTDATA | COMMA | CR | LF | 2DQUOTE}, DQUOTE;
                non_escaped = {TEXTDATA};

                COMMA    = "\\x2C";
                CR       = "\\x0D";
                DQUOTE   = "\\x22";
                2DQUOTE  = "\\x22\\x22";
                LF       = "\\x0A";
                CRLF     = "\\x0D", "\\x0A";
                TEXTDATA = "[\\x20-\\x21]|[\\x23-\\x2B]|[\\x2D-\\x7E]";
                """;
        var tokenizer = TokenizerFactory.loadFactory(StringLiteralTokenizerFactory.class.getCanonicalName())
                .createTokenizer(new StringReader(input));
        tokenizer = TokenizerFactory.loadFactory(WordTokenizerFactory.class.getCanonicalName())
                .createTokenizer(tokenizer);
        var parser = ParserFactory.loadFactory(GrammarParserFactory.class.getCanonicalName())
                .createParser(tokenizer);
        var tree = parser.parse();

        System.out.println(tree.accept(new TreeVisitor<String, String>() {
            @Override
            public String visitNonTerminal(NonTerminal tree, String p) {
                return p + tree.getSymbol() + tree.getSubTrees()
                        .stream()
                        .map(e -> visit(e, p + "  "))
                        .collect(Collectors.joining(",\n", "( // %s\n".formatted(tree), "\n" + p + ")"));
            }

            @Override
            public String visitTerminal(Terminal tree, String p) {
                return p + tree.getValue();
            }
        }, ""));

        var grammar = tree.accept(new NonTerminalVisitor<Grammar, Grammar.Builder>() {
            @Override
            public Grammar visitNonTerminal(NonTerminal tree, Grammar.Builder p) {
                visitGrammar(tree, p);
                return p.build();
            }

            void visitGrammar(NonTerminal tree, Grammar.Builder p) {
                assertEquals("GRAMMAR", tree.getSymbol());
                tree.getNonTerminals("PRODUCTION_RULE")
                        .forEach(e -> visitProductionRule(e, p));
            }

            void visitProductionRule(NonTerminal tree, Grammar.Builder p) {
                assertEquals("PRODUCTION_RULE", tree.getSymbol());
                assertEquals("SYMBOL", tree.getNonTerminal(0).getSymbol());
                assertEquals("EQUAL", tree.getNonTerminal(1).getSymbol());
                assertEquals("RULES", tree.getNonTerminal(2).getSymbol());
                assertEquals("SEMICOLON", tree.getNonTerminal(3).getSymbol());
                var symbol = tree.getNonTerminal("SYMBOL", 0).getTerminal(0).getValue();
                var rules = visitRules(tree.getNonTerminal("RULES", 0));
                p.add(symbol, rules);
            }

            Rule.Builder visitRules(NonTerminal tree) {
                assertEquals("RULES", tree.getSymbol());
                var rule = visitRule(tree.getNonTerminal("RULE", 0));
                if (tree.getSubTrees().size() == 1) {
                    return rule;
                }
                var rule2 = visitRule2(rule, tree.getNonTerminal("RULE_2", 0));
                return rule2;
            }

            Rule.Builder visitRule(NonTerminal tree) {
                assertEquals("RULE", tree.getSymbol());
                var rule = tree.getNonTerminal(0).asNonTerminal();
                return switch (rule.getSymbol()) {
                case "SYMBOL" -> visitSymbol(rule);
                case "PATTERN" -> visitPattern(rule);
                case "OPTION" -> visitOption(rule);
                case "ZERO_OR_MORE" -> visitZeroOrMore(rule);
                default -> throw new AssertionError();
                };
            }

            Rule.Builder visitSymbol(NonTerminal tree) {
                assertEquals("SYMBOL", tree.getSymbol());
                var symbol = tree.getTerminal(0).getValue();
                return Rules.reference(symbol);
            }

            Rule.Builder visitPattern(NonTerminal tree) {
                assertEquals("PATTERN", tree.getSymbol());
                assertEquals("\"", tree.getTerminal(0).getValue());
                assertEquals("\"", tree.getTerminal(2).getValue());
                var pattern = tree.getTerminal(1).getValue();
                return Rules.pattern(pattern);
            }

            Rule.Builder visitOption(NonTerminal tree) {
                assertEquals("OPTION", tree.getSymbol());
                var rule = (Quantifiable) visitRules(tree.getNonTerminal("RULES", 0));
                return rule.opt();
            }

            Rule.Builder visitZeroOrMore(NonTerminal tree) {
                assertEquals("ZERO_OR_MORE", tree.getSymbol());
                var rule = (Quantifiable) visitRules(tree.getNonTerminal("RULES", 0));
                return rule.zeroOrMore();
            }

            Rule.Builder visitRule2(Rule.Builder rule, NonTerminal tree) {
                assertEquals("RULE_2", tree.getSymbol());
                var symbol = tree.getNonTerminal(0).getSymbol();
                return switch (symbol) {
                case "OR_RULE" -> visitOrRule(rule, tree.getSubTrees());
                case "AND_RULE" -> visitAndRule(rule, tree.getSubTrees());
                default -> throw new AssertionError();
                };
            }

            Rule.Builder visitOrRule(Rule.Builder rule, List<? extends Tree> list) {
                var builder = Rules.choiceBuilder().add(rule);
                list.stream()
                        .map(e -> e.asNonTerminal())
                        .peek(e -> assertEquals("VERTICAL_BAR", e.getNonTerminal(0).getSymbol()))
                        .peek(e -> assertEquals("RULE", e.getNonTerminal(1).getSymbol()))
                        .map(e -> e.getNonTerminal("RULE", 0))
                        .map(e -> visitRule(e))
                        .forEach(e -> builder.add(e));
                return builder;
            }

            Rule.Builder visitAndRule(Rule.Builder rule, List<? extends Tree> list) {
                var builder = Rules.sequenceBuilder().add(rule);
                list.stream()
                        .map(e -> e.asNonTerminal())
                        .peek(e -> assertEquals("COMMA", e.getNonTerminal(0).getSymbol()))
                        .peek(e -> assertEquals("RULE", e.getNonTerminal(1).getSymbol()))
                        .map(e -> e.getNonTerminal("RULE", 0))
                        .map(e -> visitRule(e))
                        .forEach(e -> builder.add(e));
                return builder;
            }

        }, Grammar.builder());

        var input2 = """
                A,B,C\r\n\
                1,2,3\r\n\
                4,5,6""";
        var parser2 = Parser.newParser(grammar, new StringReader(input2));
        var tree2 = parser2.parse();
        System.out.println(tree2);
    }

}
