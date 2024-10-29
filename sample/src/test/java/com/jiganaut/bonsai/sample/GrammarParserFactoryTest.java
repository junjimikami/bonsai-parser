package com.jiganaut.bonsai.sample;

import java.io.StringReader;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.parser.NonTerminalNode;
import com.jiganaut.bonsai.parser.ParserFactory;
import com.jiganaut.bonsai.parser.TerminalNode;
import com.jiganaut.bonsai.parser.TokenizerFactory;
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
            public String visitNonTerminal(NonTerminalNode tree, String p) {
                return p + tree.getName() + tree.getSubTrees()
                        .stream()
                        .map(e -> visit(e, p + "  "))
                        .collect(Collectors.joining(",\n", "( // %s\n".formatted(tree), "\n" + p + ")"));
            }

            @Override
            public String visitTerminal(TerminalNode tree, String p) {
                return p + tree.getValue();
            }
        }, ""));

//        var grammar = tree.accept(new TreeVisitor<Grammar, Grammar.Builder>() {
//            @Override
//            public Grammar visitNonTerminal(NonTerminalNode tree, Grammar.Builder p) {
//                visitGrammar(tree, p);
//                return p.build();
//            }
//
//            void visitGrammar(NonTerminalNode tree, Grammar.Builder p) {
//                assertEquals("GRAMMAR", tree.getName());
//                tree.getNonTerminals("PRODUCTION_RULE")
//                        .forEach(e -> visitProductionRule(e, p));
//            }
//
//            void visitProductionRule(NonTerminalNode tree, Grammar.Builder p) {
//                assertEquals("PRODUCTION_RULE", tree.getName());
//                assertEquals("SYMBOL", tree.getNonTerminal(0).getName());
//                assertEquals("EQUAL", tree.getNonTerminal(1).getName());
//                assertEquals("RULES", tree.getNonTerminal(2).getName());
//                assertEquals("SEMICOLON", tree.getNonTerminal(3).getName());
//                var symbol = tree.getNonTerminal("SYMBOL", 0).getTerminal(0).getValue();
//                var rules = visitRules(tree.getNonTerminal("RULES", 0));
//                p.add(symbol, rules);
//            }
//
//            Rule.Builder visitRules(NonTerminalNode tree) {
//                assertEquals("RULES", tree.getName());
//                var rule = visitRule(tree.getNonTerminal("RULE", 0));
//                if (tree.getSubTrees().size() == 1) {
//                    return rule;
//                }
//                var rule2 = visitRule2(rule, tree.getNonTerminal("RULE_2", 0));
//                return rule2;
//            }
//
//            Rule.Builder visitRule(NonTerminalNode tree) {
//                assertEquals("RULE", tree.getName());
//                var rule = tree.getNonTerminal(0).asNonTerminal();
//                return switch (rule.getName()) {
//                case "SYMBOL" -> visitSymbol(rule);
//                case "PATTERN" -> visitPattern(rule);
//                case "OPTION" -> visitOption(rule);
//                case "ZERO_OR_MORE" -> visitZeroOrMore(rule);
//                default -> throw new AssertionError();
//                };
//            }
//
//            Rule.Builder visitSymbol(NonTerminalNode tree) {
//                assertEquals("SYMBOL", tree.getName());
//                var symbol = tree.getTerminal(0).getValue();
//                return Rules.reference(symbol);
//            }
//
//            Rule.Builder visitPattern(NonTerminalNode tree) {
//                assertEquals("PATTERN", tree.getName());
//                assertEquals("\"", tree.getTerminal(0).getValue());
//                assertEquals("\"", tree.getTerminal(2).getValue());
//                var pattern = tree.getTerminal(1).getValue();
//                return Rules.pattern(pattern);
//            }
//
//            Rule.Builder visitOption(NonTerminalNode tree) {
//                assertEquals("OPTION", tree.getName());
//                var rule = (Quantifiable) visitRules(tree.getNonTerminal("RULES", 0));
//                return rule.opt();
//            }
//
//            Rule.Builder visitZeroOrMore(NonTerminalNode tree) {
//                assertEquals("ZERO_OR_MORE", tree.getName());
//                var rule = (Quantifiable) visitRules(tree.getNonTerminal("RULES", 0));
//                return rule.zeroOrMore();
//            }
//
//            Rule.Builder visitRule2(Rule.Builder rule, NonTerminalNode tree) {
//                assertEquals("RULE_2", tree.getName());
//                var symbol = tree.getNonTerminal(0).getName();
//                return switch (symbol) {
//                case "OR_RULE" -> visitOrRule(rule, tree.getSubTrees());
//                case "AND_RULE" -> visitAndRule(rule, tree.getSubTrees());
//                default -> throw new AssertionError();
//                };
//            }
//
//            Rule.Builder visitOrRule(Rule.Builder rule, List<? extends Tree> list) {
//                var builder = Rules.choiceBuilder().add(rule);
//                list.stream()
//                        .map(e -> e.asNonTerminal())
//                        .peek(e -> assertEquals("VERTICAL_BAR", e.getNonTerminal(0).getName()))
//                        .peek(e -> assertEquals("RULE", e.getNonTerminal(1).getName()))
//                        .map(e -> e.getNonTerminal("RULE", 0))
//                        .map(e -> visitRule(e))
//                        .forEach(e -> builder.add(e));
//                return builder;
//            }
//
//            Rule.Builder visitAndRule(Rule.Builder rule, List<? extends Tree> list) {
//                var builder = Rules.sequenceBuilder().add(rule);
//                list.stream()
//                        .map(e -> e.asNonTerminal())
//                        .peek(e -> assertEquals("COMMA", e.getNonTerminal(0).getName()))
//                        .peek(e -> assertEquals("RULE", e.getNonTerminal(1).getName()))
//                        .map(e -> e.getNonTerminal("RULE", 0))
//                        .map(e -> visitRule(e))
//                        .forEach(e -> builder.add(e));
//                return builder;
//            }
//
//        }, Grammar.builder());

//        var input2 = """
//                A,B,C\r\n\
//                1,2,3\r\n\
//                4,5,6""";
//        var parser2 = Parser.newParser(grammar, new StringReader(input2));
//        var tree2 = parser2.parse();
//        System.out.println(tree2);
    }

}
