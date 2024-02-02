package com.unitedjiga.common.parsing.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class GrammarTest {

    @Nested
    class BuilderTest {

        @Test
        @DisplayName("build() [No elements]")
        void buildInCaseNoElements() throws Exception {
            var builder = Grammar.builder();

            assertThrows(IllegalStateException.class, () -> builder.build())
                    .printStackTrace();
        }

        @Test
        @DisplayName("build() [Invalid reference]")
        void buildInCaseInvalidReference() throws Exception {
            var builder = Grammar.builder();
            builder.add("A", "B");

            assertThrows(NoSuchElementException.class, () -> builder.build())
                    .printStackTrace();
        }

        @Test
        @DisplayName("build() [No such start symbol]")
        void buildInCaseNoSuchStartSymbol() throws Exception {
            var builder = Grammar.builder();
            builder.add("A", Stubs.DUMMY_EXPRESSION_BUILDER);
            builder.setStartSymbol("B");

            assertThrows(NoSuchElementException.class, () -> builder.build())
                    .printStackTrace();
        }

        @Test
        @DisplayName("build() [Builder returning null]")
        void buildInCaseBuilderReturningNull() throws Exception {
            var builder = Grammar.builder();
            builder.add("A", set -> null);

            assertThrows(NullPointerException.class, () -> builder.build())
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(st:String, eb:Expression.Builder) [st == null]")
        void addStEbInCaseStIsNull() throws Exception {
            var builder = Grammar.builder();

            assertThrows(NullPointerException.class, () -> builder.add(null, Stubs.DUMMY_EXPRESSION_BUILDER))
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(st:String, st2:String) [st == null]")
        void addStSt2InCaseStIsNull() throws Exception {
            var builder = Grammar.builder();

            assertThrows(NullPointerException.class, () -> builder.add(null, "B"))
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(st:String, eb:Expression.Builder) [eb == null]")
        void addStEbInCaseEbIsNull() throws Exception {
            var builder = Grammar.builder();

            assertThrows(NullPointerException.class, () -> builder.add("A", (Expression.Builder) null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(st:String, st2:String) [st2 == null]")
        void addStSt2InCaseSt2IsNull() throws Exception {
            var builder = Grammar.builder();

            assertThrows(NullPointerException.class, () -> builder.add("A", (String) null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("setSkipPattern(st:String) [st == null]")
        void setSkipPatternStInCaseNullParameter() throws Exception {
            var builder = Grammar.builder();

            assertThrows(NullPointerException.class, () -> builder.setSkipPattern((String) null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("setSkipPattern(pa:Pattern) [pa == null]")
        void setSkipPatternPaInCaseNullParameter() throws Exception {
            var builder = Grammar.builder();

            assertThrows(NullPointerException.class, () -> builder.setSkipPattern((Pattern) null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("setStartSymbol(st:String) [st == null]")
        void setStartSymbolInCaseNullParameter() throws Exception {
            var builder = Grammar.builder();

            assertThrows(NullPointerException.class, () -> builder.setStartSymbol(null))
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(st:String, eb:Expression.Builder) [Post-build operation]")
        void addStEbInCasePostBuild() throws Exception {
            var builder = Grammar.builder();
            builder.add("A", Stubs.DUMMY_EXPRESSION_BUILDER);
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.add("A", Stubs.DUMMY_EXPRESSION_BUILDER))
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(st:String, st2:String) [Post-build operation]")
        void addStSt2InCasePostBuild() throws Exception {
            var builder = Grammar.builder();
            builder.add("A", Stubs.DUMMY_EXPRESSION_BUILDER);
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.add("A", "B"))
                    .printStackTrace();
        }

        @Test
        @DisplayName("setSkipPattern(st:String) [Post-build operation]")
        void setSkipPatternStInCasePostBuild() throws Exception {
            var builder = Grammar.builder();
            builder.add("A", Stubs.DUMMY_EXPRESSION_BUILDER);
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.setSkipPattern(""))
                    .printStackTrace();
        }

        @Test
        @DisplayName("setSkipPattern(pa:Pattern) [Post-build operation]")
        void setSkipPatternPaInCasePostBuild() throws Exception {
            var builder = Grammar.builder();
            builder.add("A", Stubs.DUMMY_EXPRESSION_BUILDER);
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.setSkipPattern(Pattern.compile("")))
                    .printStackTrace();
        }

        @Test
        @DisplayName("setStartSymbol(st:String) [Post-build operation]")
        void setStartSymbolInCasePostBuild() throws Exception {
            var builder = Grammar.builder();
            builder.add("A", Stubs.DUMMY_EXPRESSION_BUILDER);
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.build())
                    .printStackTrace();
        }

        @Test
        @DisplayName("add(st:String, eb:Expression.Builder)")
        void addStEb() throws Exception {
            var builder = Grammar.builder();

            assertEquals(builder, builder.add("A", Stubs.DUMMY_EXPRESSION_BUILDER));
        }

        @Test
        @DisplayName("add(st:String, st2:String)")
        void addStSt2() throws Exception {
            var builder = Grammar.builder();

            assertEquals(builder, builder.add("A", Stubs.DUMMY_EXPRESSION_BUILDER));
        }

        @Test
        @DisplayName("setSkipPattern(st:String)")
        void setSkipPatternSt() throws Exception {
            var builder = Grammar.builder();

            assertEquals(builder, builder.add("A", Stubs.DUMMY_EXPRESSION_BUILDER));
        }

        @Test
        @DisplayName("setSkipPattern(pa:Pattern)")
        void setSkipPatternPa() throws Exception {
            var builder = Grammar.builder();

            assertEquals(builder, builder.add("A", Stubs.DUMMY_EXPRESSION_BUILDER));
        }

        @Test
        @DisplayName("setStartSymbol(st:String)")
        void setStartSymbol() throws Exception {
            var builder = Grammar.builder();

            assertEquals(builder, builder.add("A", Stubs.DUMMY_EXPRESSION_BUILDER));
        }

    }

    @Test
    @DisplayName("getStartSymbol()")
    void getStartSymbol() throws Exception {
        var grammar = Grammar.builder()
                .add("S", Stubs.DUMMY_EXPRESSION_BUILDER)
                .add("A", Stubs.DUMMY_EXPRESSION_BUILDER)
                .build();

        assertEquals("S", grammar.getStartSymbol());
    }

    @Test
    @DisplayName("getStartSymbol() [Start symbol changed]")
    void getStartSymbolInCaseStartSymbolChanged() throws Exception {
        var grammar = Grammar.builder()
                .add("S", Stubs.DUMMY_EXPRESSION_BUILDER)
                .add("A", Stubs.DUMMY_EXPRESSION_BUILDER)
                .setStartSymbol("A")
                .build();

        assertEquals("A", grammar.getStartSymbol());
    }

    @Test
    @DisplayName("getSkipPattern()")
    void getSkipPattern() throws Exception {
        var grammar = Grammar.builder()
                .add("S", Stubs.DUMMY_EXPRESSION_BUILDER)
                .build();

        assertNull(grammar.getSkipPattern());
    }

    @Test
    @DisplayName("getSkipPattern() [Skip pattern set]")
    void getSkipPatternInCaseSkipPatternSet() throws Exception {
        var grammar = Grammar.builder()
                .add("S", Stubs.DUMMY_EXPRESSION_BUILDER)
                .setSkipPattern("123")
                .build();

        assertEquals("123", grammar.getSkipPattern().pattern());
    }

    @Test
    @DisplayName("productionSet()")
    void productionSet() throws Exception {
        var grammar = Grammar.builder()
                .add("S", Stubs.DUMMY_EXPRESSION_BUILDER)
                .add("A", Stubs.DUMMY_EXPRESSION_BUILDER)
                .build();
        var productionSet = grammar.productionSet();

        assertTrue(productionSet.containsSymbol("S"));
        assertEquals("S", productionSet.get("S").getSymbol());
        assertEquals(Stubs.DUMMY_EXPRESSION, productionSet.get("S").getExpression());

        assertTrue(productionSet.containsSymbol("A"));
        assertEquals("A", productionSet.get("A").getSymbol());
        assertEquals(Stubs.DUMMY_EXPRESSION, productionSet.get("A").getExpression());

        assertFalse(productionSet.containsSymbol("B"));
        assertThrows(NoSuchElementException.class, () -> productionSet.get("B"));
    }

    @Test
    @DisplayName("getStartProduction()")
    void getStartProduction() throws Exception {
        var grammar = Grammar.builder()
                .add("S", Stubs.DUMMY_EXPRESSION_BUILDER)
                .add("A", Stubs.DUMMY_EXPRESSION_BUILDER)
                .build();

        assertEquals("S", grammar.getStartProduction().getSymbol());
    }

    @Test
    @DisplayName("getStartProduction() [Start symbol changed]")
    void getStartProductionInCaseStartSymbolChanged() throws Exception {
        var grammar = Grammar.builder()
                .add("S", Stubs.DUMMY_EXPRESSION_BUILDER)
                .add("A", Stubs.DUMMY_EXPRESSION_BUILDER)
                .setStartSymbol("A")
                .build();

        assertEquals("A", grammar.getStartProduction().getSymbol());
    }

}
