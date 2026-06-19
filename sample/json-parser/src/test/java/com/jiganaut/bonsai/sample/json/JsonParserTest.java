package com.jiganaut.bonsai.sample.json;

import static java.util.stream.Collectors.joining;

import java.io.StringReader;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import com.jiganaut.bonsai.parser.NonTerminalNode;
import com.jiganaut.bonsai.parser.ParseException;
import com.jiganaut.bonsai.parser.SimpleTreeVisitor;
import com.jiganaut.bonsai.parser.Token;
import com.jiganaut.bonsai.parser.Tree;

class JsonParserTest {

    @Test
    void test(TestReporter reporter) throws Exception {
        var input = """
                {
                  "stringExample": "This is a string",
                  "escapedStringExample": "This is an escaped string: \\"Hello, World!\\"",
                  "integerExample": 123,
                  "floatExample": 123.45,
                  "exponentialExample": 1.23e4,
                  "negativeNumberExample": -789,
                  "booleanTrueExample": true,
                  "booleanFalseExample": false,
                  "nullExample": null,
                  "arrayExample": ["String 1", "String 2", "String 3"],
                  "objectExample": {
                    "nestedString": "Nested string",
                    "nestedInteger": 456
                  }
                }""";

        try (var reader = new StringReader(input);
                var parser = new JsonParser(reader)) {

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
                case "json" -> {
                    return tree.subTrees()
                            .<Tree<String>>mapMulti((e, consumer) -> {
                                if (Objects.equals(e.getName(), "element")) {
                                    visit(e).forEach(consumer::accept);
                                }
                            });
                }
                case "value" -> {
                    var string = tree.subTrees()
                            .filter(e -> e.getName() == null)
                            .flatMap(e -> e.values())
                            .collect(joining());
                    if (!string.isEmpty()) {
                        return Stream.of(Token.ofUnnamed(string));
                    }
                    return tree.subTrees()
                            .<Tree<String>>mapMulti((e, consumer) -> {
                                if (Objects.equals(e.getName(), "object")) {
                                    visit(e).forEach(consumer::accept);
                                } else if (Objects.equals(e.getName(), "array")) {
                                    visit(e).forEach(consumer::accept);
                                } else if (Objects.equals(e.getName(), "string")) {
                                    visit(e).forEach(consumer::accept);
                                } else if (Objects.equals(e.getName(), "number")) {
                                    visit(e).forEach(consumer::accept);
                                }
                            });
                }
                case "object" -> {
                    var builder = NonTerminalNode.<String>builder(tree.getName());
                    tree.subTrees()
                            .<Tree<String>>mapMulti((e, consumer) -> {
                                if (Objects.equals(e.getName(), "members")) {
                                    visit(e).forEach(consumer::accept);
                                }
                            })
                            .forEach(builder::add);
                    return Stream.of(builder.build());
                }
                case "members" -> {
                    return tree.subTrees()
                            .<Tree<String>>mapMulti((e, consumer) -> {
                                if (Objects.equals(e.getName(), "member")) {
                                    visit(e).forEach(consumer::accept);
                                } else if (Objects.equals(e.getName(), "members")) {
                                    visit(e).forEach(consumer::accept);
                                }
                            });
                }
                case "member" -> {
                    String name = tree.subTrees()
                            .<Tree<String>>mapMulti((e, consumer) -> {
                                if (Objects.equals(e.getName(), "string")) {
                                    visit(e).forEach(consumer::accept);
                                }
                            })
                            .flatMap(Tree::values)
                            .collect(Collectors.joining());
                    var builder = NonTerminalNode.<String>builder(name);
                    tree.subTrees()
                            .<Tree<String>>mapMulti((e, consumer) -> {
                                if (Objects.equals(e.getName(), "element")) {
                                    visit(e).forEach(consumer::accept);
                                }
                            })
                            .forEach(builder::add);
                    return Stream.of(builder.build());
                }
                case "array" -> {
                    var builder = NonTerminalNode.<String>builder(tree.getName());
                    tree.subTrees()
                            .<Tree<String>>mapMulti((e, consumer) -> {
                                if (Objects.equals(e.getName(), "elements")) {
                                    visit(e).forEach(consumer::accept);
                                }
                            })
                            .forEach(builder::add);
                    return Stream.of(builder.build());
                }
                case "elements" -> {
                    return tree.subTrees()
                            .<Tree<String>>mapMulti((e, consumer) -> {
                                if (Objects.equals(e.getName(), "element")) {
                                    visit(e).forEach(consumer::accept);
                                } else if (Objects.equals(e.getName(), "elements")) {
                                    visit(e).forEach(consumer::accept);
                                }
                            });
                }
                case "element" -> {
                    return tree.subTrees()
                            .<Tree<String>>mapMulti((e, consumer) -> {
                                if (Objects.equals(e.getName(), "value")) {
                                    visit(e).forEach(consumer::accept);
                                }
                            });
                }
                case "string" -> {
                    var value = tree.subTrees()
                            .<String>mapMulti((e, consumer) -> {
                                if (Objects.equals(e.getName(), "characters")) {
                                    e.values().forEach(consumer::accept);
                                }
                            })
                            .collect(joining());
                    return Stream.of(Token.ofUnnamed(value));
                }
                case "number" -> {
                    var value = tree.subTrees()
                            .<String>mapMulti((e, consumer) -> {
                                e.values().forEach(consumer::accept);
                            })
                            .collect(joining());
                    return Stream.of(Token.ofUnnamed(value));
                }

                default -> throw new AssertionError();
            }
        }

    }
}
