package com.unitedjiga.common.parsing;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.unitedjiga.common.parsing.grammar.Expressions;
import com.unitedjiga.common.parsing.grammar.Grammar;
import static com.unitedjiga.common.parsing.grammar.Expressions.*
;

class ParserTest {

    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    void setUp() throws Exception {
    }

    @AfterEach
    void tearDown() throws Exception {
    }

    @Test
    void test() {
        var grammar = Grammar.builder()
                .add("A", "B")
                .add("B", pattern("0"))
                .build();
        var factory = ParserFactory.newFactory(grammar);
        var parser = factory.createParser(new StringReader("0"));
        var tree = parser.parse();
        assertTrue(tree.getKind().isNonTerminal());
        var name = tree.accept(new TreeVisitor<String, Void>() {
            @Override
            public String visitNonTerminal(NonTerminal s, Void p) {
                return s.getSymbol() + s.getSubTrees().stream()
                        .filter(t -> t.getKind().isNonTerminal())
                        .map(this::visit)
                        .collect(Collectors.joining(",", "(", ")"));
            }

            @Override
            public String visitTerminal(Terminal s, Void p) {
                return null;
            }
        });
        assertEquals("A(B())", name);
    }

}
