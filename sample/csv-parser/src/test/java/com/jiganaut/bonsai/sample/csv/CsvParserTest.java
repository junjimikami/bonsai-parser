package com.jiganaut.bonsai.sample.csv;

import static java.util.stream.Collectors.joining;

import java.io.StringReader;
import java.util.Objects;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import com.jiganaut.bonsai.parser.NonTerminalNode;
import com.jiganaut.bonsai.parser.ParseException;
import com.jiganaut.bonsai.parser.SimpleTreeVisitor;
import com.jiganaut.bonsai.parser.Token;
import com.jiganaut.bonsai.parser.Tree;

class CsvParserTest {

    @Test
    void test(TestReporter reporter) throws Exception {
        var input = """
                name,age,city\r
                Alice,30,New York\r
                Bob,25,"San Francisco"\r
                "Charlie, Jr.",40,"Tokyo, Japan"\r
                """;

        try (var reader = new StringReader(input);
                var parser = new CsvParser(reader)) {

            var tree = parser.parse();
            reporter.publishEntry("Parse tree", tree.toString());

            var ast = tree.accept(new AbstractSyntaxTree()).findFirst().get();
            reporter.publishEntry("AST", ast.toString());

        } catch (ParseException e) {
            System.err.println(e.getErrorNode());
            throw e;
        }
    }

    class AbstractSyntaxTree implements SimpleTreeVisitor<String, Stream<Tree<String>>, Void> {

        @Override
        public Stream<Tree<String>> visitNonTerminal(NonTerminalNode<String> tree, Void p) {
            switch (tree.getName()) {
                case "file" -> {
                    var builder = NonTerminalNode.<String>builder(tree.getName());
                    tree.subTrees()
                            .<Tree<String>>mapMulti((e, consumer) -> {
                                if (Objects.equals(e.getName(), "record")) {
                                    visit(e).forEach(consumer::accept);
                                } else if (Objects.equals(e.getName(), "file")) {
                                    visit(e).forEach(consumer::accept);
                                }
                            })
                            .forEach(builder::add);
                    return Stream.of(builder.build());
                }
                case "record" -> {
                    var builder = NonTerminalNode.<String>builder(tree.getName());
                    tree.subTrees()
                            .<Tree<String>>mapMulti((e, consumer) -> {
                                if (Objects.equals(e.getName(), "field")) {
                                    visit(e).forEach(consumer::accept);
                                } else if (Objects.equals(e.getName(), "record")) {
                                    visit(e).forEach(consumer::accept);
                                }
                            })
                            .forEach(builder::add);
                    return Stream.of(builder.build());
                }
                case "field" -> {
                    return tree.subTrees()
                            .<Tree<String>>mapMulti((e, consumer) -> {
                                if (Objects.equals(e.getName(), "escaped")) {
                                    visit(e).forEach(consumer::accept);
                                } else if (Objects.equals(e.getName(), "non-escaped")) {
                                    visit(e).forEach(consumer::accept);
                                }
                            });
                }
                case "escaped" -> {
                    var value = tree.subTrees()
                            .<String>mapMulti((e, consumer) -> {
                                if (Objects.equals(e.getName(), "escaped-content")) {
                                    e.values().forEach(consumer::accept);
                                }
                            })
                            .collect(joining());
                    return Stream.of(Token.ofUnnamed(value));
                }
                case "non-escaped" -> {
                    var value = tree.values().collect(joining());
                    return Stream.of(Token.ofUnnamed(value));
                }

                default -> throw new AssertionError();
            }
        }

    }
}
