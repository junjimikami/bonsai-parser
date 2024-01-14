package com.unitedjiga.common.parsing.grammar;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class GrammarTest {

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

    @Nested
    class MonkeyTest {

        @Test
        void test() {
            var builder = Grammar.builder();
            assertNotNull(builder);
            assertThrows(NullPointerException.class, () -> builder.add(null, "X"))
                    .printStackTrace();
            assertThrows(NullPointerException.class, () -> builder.add("X", (String) null))
            .printStackTrace();
            builder.add("A", Expressions.pattern("0"));
            builder.add("B", Expressions.pattern("1"));
            var grammar = builder.build();
            assertEquals("A", grammar.getStartSymbol());
            assertNull(grammar.getSkipPattern());
            assertEquals("A", grammar.productionSet().get("A").getSymbol());
            assertEquals("B", grammar.productionSet().get("B").getSymbol());
            assertThrows(IllegalStateException.class, () -> builder.add("C", "2"))
                    .printStackTrace();
            assertThrows(IllegalStateException.class, () -> builder.build())
                    .printStackTrace();
        }
    }

}
