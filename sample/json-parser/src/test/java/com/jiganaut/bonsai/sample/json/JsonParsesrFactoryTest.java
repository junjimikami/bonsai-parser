package com.jiganaut.bonsai.sample.json;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.parser.NonTerminalNode;
import com.jiganaut.bonsai.parser.ParserFactory;
import com.jiganaut.bonsai.parser.TerminalNode;
import com.jiganaut.bonsai.parser.Tree;
import com.jiganaut.bonsai.parser.TreeVisitor;

class JsonParsesrFactoryTest {

    @Test
    void test() throws Exception {
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
        var factory = ParserFactory.load(JsonParsesrFactory.class);

        try (var reader = new StringReader(input);
                var parser = factory.createParser(reader)) {

            var tree = parser.parse();
            System.out.println(tree);
            assertEquals("json", tree.getName());
            assertEquals("element", tree.getSubTrees().get(0).getName());
            assertEquals("ws", tree.getSubTrees().get(0)
                    .getSubTrees().get(0).getName());
            assertEquals("value", tree.getSubTrees().get(0)
                    .getSubTrees().get(1).getName());

            var ast = tree.accept(new AbstractSyntaxTree());
            System.out.println(ast);
            assertEquals("object", ast.getName());
            assertEquals("member", ast.getSubTrees().get(0).getName());
            assertEquals("string", ast.getSubTrees().get(0)
                    .getSubTrees().get(0).getName());
            assertEquals("string", ast.getSubTrees().get(0)
                    .getSubTrees().get(1).getName());
        }

    }

    class AbstractSyntaxTree implements TreeVisitor<Tree, Void> {

        @Override
        public Tree visitTerminal(TerminalNode tree, Void p) {
            return tree;
        }

        @Override
        public Tree visitNonTerminal(NonTerminalNode tree, Void p) {
            switch (tree.getName()) {
            case "json":
                return tree.getSubTrees().stream()
                        .<Tree>mapMulti((e, consumer) -> {
                            if (Objects.equals(e.getName(), "element")) {
                                visit(e).getSubTrees().forEach(consumer::accept);
                            }
                        })
                        .findFirst()
                        .get();
            case "value":
                var value = tree.getSubTrees().stream()
                        .<String>mapMulti((e, consumer) -> {
                            if (e.getValue() != null) {
                                consumer.accept(e.getValue());
                            }
                        })
                        .collect(joining());
                if (value.length() > 0) {
                    return NonTerminalNode.of(value, TerminalNode.ofUnnamed(value));
                }
                return tree.getSubTrees().stream()
                        .<Tree>mapMulti((e, consumer) -> {
                            if (Objects.equals(e.getName(), "object")) {
                                consumer.accept(visit(e));
                            } else if (Objects.equals(e.getName(), "array")) {
                                consumer.accept(visit(e));
                            } else if (Objects.equals(e.getName(), "string")) {
                                consumer.accept(visit(e));
                            } else if (Objects.equals(e.getName(), "number")) {
                                consumer.accept(visit(e));
                            }
                        })
                        .findFirst()
                        .get();
            case "object":
                return tree.getSubTrees().stream()
                        .<Tree>mapMulti((e, consumer) -> {
                            if (Objects.equals(e.getName(), "members")) {
                                visit(e).getSubTrees().forEach(consumer::accept);
                            }
                        })
                        .collect(NonTerminalNode::builder,
                                NonTerminalNode.Builder::add,
                                NonTerminalNode.Builder::addAll)
                        .setName(tree.getName())
                        .build();
            case "members":
                return tree.getSubTrees().stream()
                        .<Tree>mapMulti((e, consumer) -> {
                            if (Objects.equals(e.getName(), "member")) {
                                consumer.accept(visit(e));
                            } else if (Objects.equals(e.getName(), "members")) {
                                visit(e).getSubTrees().forEach(consumer::accept);
                            }
                        })
                        .collect(NonTerminalNode::builder,
                                NonTerminalNode.Builder::add,
                                NonTerminalNode.Builder::addAll)
                        .setName(tree.getName())
                        .build();
            case "member":
                return tree.getSubTrees().stream()
                        .<Tree>mapMulti((e, consumer) -> {
                            if (Objects.equals(e.getName(), "string")) {
                                consumer.accept(visit(e));
                            } else if (Objects.equals(e.getName(), "element")) {
                                visit(e).getSubTrees().forEach(consumer::accept);
                            }
                        })
                        .collect(NonTerminalNode::builder,
                                NonTerminalNode.Builder::add,
                                NonTerminalNode.Builder::addAll)
                        .setName(tree.getName())
                        .build();
            case "array":
                return tree.getSubTrees().stream()
                        .<Tree>mapMulti((e, consumer) -> {
                            if (Objects.equals(e.getName(), "elements")) {
                                visit(e).getSubTrees().forEach(consumer::accept);
                            }
                        })
                        .collect(NonTerminalNode::builder,
                                NonTerminalNode.Builder::add,
                                NonTerminalNode.Builder::addAll)
                        .setName(tree.getName())
                        .build();
            case "elements":
                return tree.getSubTrees().stream()
                        .<Tree>mapMulti((e, consumer) -> {
                            if (Objects.equals(e.getName(), "element")) {
                                visit(e).getSubTrees().forEach(consumer::accept);
                            } else if (Objects.equals(e.getName(), "elements")) {
                                visit(e).getSubTrees().forEach(consumer::accept);
                            }
                        })
                        .collect(NonTerminalNode::builder,
                                NonTerminalNode.Builder::add,
                                NonTerminalNode.Builder::addAll)
                        .setName(tree.getName())
                        .build();
            case "element":
                return tree.getSubTrees().stream()
                        .<Tree>mapMulti((e, consumer) -> {
                            if (Objects.equals(e.getName(), "value")) {
                                consumer.accept(visit(e));
                            }
                        })
                        .collect(NonTerminalNode::builder,
                                NonTerminalNode.Builder::add,
                                NonTerminalNode.Builder::addAll)
                        .setName(tree.getName())
                        .build();
            case "string":
                return tree.getSubTrees().stream()
                        .<Tree>mapMulti((e, consumer) -> {
                            if (Objects.equals(e.getName(), "characters")) {
                                consumer.accept(visit(e));
                            }
                        })
                        .collect(NonTerminalNode::builder,
                                NonTerminalNode.Builder::add,
                                NonTerminalNode.Builder::addAll)
                        .setName(tree.getName())
                        .build();
            case "number":
                return tree.getSubTrees().stream()
                        .map(this::visit)
                        .collect(NonTerminalNode::builder,
                                NonTerminalNode.Builder::add,
                                NonTerminalNode.Builder::addAll)
                        .setName(tree.getName())
                        .build();
            case "characters", "character", "escape", "hex", "integer", "digits", "digit", "onenine",
                    "fraction", "exponent", "sign":
                return tree.getSubTrees().stream()
                        .map(this::visit)
                        .map(Tree::getValue)
                        .collect(collectingAndThen(joining(), TerminalNode::ofUnnamed));

            default:
                throw new AssertionError();
            }
        }

    }
}
