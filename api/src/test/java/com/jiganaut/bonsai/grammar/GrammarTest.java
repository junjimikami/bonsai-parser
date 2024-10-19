package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.grammar.Rule.Kind;

class GrammarTest {

    @Nested
    class BuilderTest {

        @Test
        @DisplayName("build() [No elements]")
        void buildInCaseNoElements() throws Exception {
            var builder = Grammar.builder();

            assertThrows(IllegalStateException.class, () -> builder.build());
        }

        @Test
        @DisplayName("build() [No such start symbol]")
        void buildInCaseNoSuchStartSymbol() throws Exception {
            var builder = Grammar.builder();
            builder.add("A", Stubs.DUMMY_RULE_BUILDER);
            builder.setStartSymbol("B");

            assertThrows(NoSuchElementException.class, () -> builder.build());
        }

        @Test
        @DisplayName("build() [Builder returning null]")
        void buildInCaseBuilderReturningNull() throws Exception {
            var builder = Grammar.builder();
            builder.add("A", () -> null);

            assertThrows(NullPointerException.class, () -> builder.build());
        }

        @Test
        @DisplayName("add(st:String, eb:Rule.Builder) [st == null]")
        void addStEbInCaseStIsNull() throws Exception {
            var builder = Grammar.builder();

            assertThrows(NullPointerException.class, () -> builder.add(null, Stubs.DUMMY_RULE_BUILDER));
        }

        @Test
        @DisplayName("add(st:String, eb:Rule.Builder) [eb == null]")
        void addStEbInCaseEbIsNull() throws Exception {
            var builder = Grammar.builder();

            assertThrows(NullPointerException.class, () -> builder.add("A", (Rule.Builder) null));
        }

        @Test
        @DisplayName("setStartSymbol(st:String) [st == null]")
        void setStartSymbolInCaseNullParameter() throws Exception {
            var builder = Grammar.builder();

            assertThrows(NullPointerException.class, () -> builder.setStartSymbol(null));
        }

        @Test
        @DisplayName("add(st:String, eb:Rule.Builder) [Post-build operation]")
        void addStEbInCasePostBuild() throws Exception {
            var builder = Grammar.builder();
            builder.add("A", Stubs.DUMMY_RULE_BUILDER);
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.add("A", Stubs.DUMMY_RULE_BUILDER));
        }

        @Test
        @DisplayName("setStartSymbol(st:String) [Post-build operation]")
        void setStartSymbolInCasePostBuild() throws Exception {
            var builder = Grammar.builder();
            builder.add("A", Stubs.DUMMY_RULE_BUILDER);
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.build());
        }

        @Test
        @DisplayName("add(st:String, eb:Rule.Builder)")
        void addStEb() throws Exception {
            var builder = Grammar.builder();

            assertEquals(builder, builder.add("A", Stubs.DUMMY_RULE_BUILDER));
        }

        @Test
        @DisplayName("add(st:String, st2:String)")
        void addStSt2() throws Exception {
            var builder = Grammar.builder();

            assertEquals(builder, builder.add("A", Stubs.DUMMY_RULE_BUILDER));
        }

        @Test
        @DisplayName("setSkipPattern(st:String)")
        void setSkipPatternSt() throws Exception {
            var builder = Grammar.builder();

            assertEquals(builder, builder.add("A", Stubs.DUMMY_RULE_BUILDER));
        }

        @Test
        @DisplayName("setSkipPattern(pa:Pattern)")
        void setSkipPatternPa() throws Exception {
            var builder = Grammar.builder();

            assertEquals(builder, builder.add("A", Stubs.DUMMY_RULE_BUILDER));
        }

        @Test
        @DisplayName("setStartSymbol(st:String)")
        void setStartSymbol() throws Exception {
            var builder = Grammar.builder();

            assertEquals(builder, builder.add("A", Stubs.DUMMY_RULE_BUILDER));
        }

    }

    @Test
    @DisplayName("productionSet()")
    void productionSet() throws Exception {
        var grammar = Grammar.builder()
                .add("S", Stubs.DUMMY_RULE_BUILDER)
                .add("A", Stubs.DUMMY_RULE_BUILDER)
                .build();
        var productionSet = grammar;

        assertTrue(productionSet.containsSymbol("S"));
        assertEquals("S", productionSet.getProduction("S").getSymbol());
        assertEquals(Stubs.DUMMY_RULE, productionSet.getProduction("S").getRule());

        assertTrue(productionSet.containsSymbol("A"));
        assertEquals("A", productionSet.getProduction("A").getSymbol());
        assertEquals(Stubs.DUMMY_RULE, productionSet.getProduction("A").getRule());

        assertFalse(productionSet.containsSymbol("B"));
        assertThrows(NoSuchElementException.class, () -> productionSet.getProduction("B"));
    }

    @Test
    @DisplayName("productionSet() [Add multiple rules to one symbol]")
    void productionSetInCaseAddMultipleRulesToOneSymbol() throws Exception {
        var grammar = Grammar.builder()
                .add("S", () -> Stubs.rule("RULE0"))
                .add("S", () -> Stubs.rule("RULE1"))
                .add("S", () -> Stubs.rule("RULE2"))
                .build();
        var productionSet = grammar;

        assertTrue(productionSet.containsSymbol("S"));
        assertEquals("S", productionSet.getProduction("S").getSymbol());
        var rule = productionSet.getProduction("S").getRule();
        assertInstanceOf(ChoiceRule.class, rule);
        assertEquals(Kind.CHOICE, rule.getKind());
        rule.accept(new TestRuleVisitor<Void, Void>() {
            public Void visitChoice(ChoiceRule choice, Void p) {
                assertEquals(3, choice.getChoices().size());
                assertEquals("RULE0", choice.getChoices().get(0).toString());
                assertEquals("RULE1", choice.getChoices().get(1).toString());
                assertEquals("RULE2", choice.getChoices().get(2).toString());
                return null;
            };
        });
    }

    @Test
    @DisplayName("getStartProduction()")
    void getStartProduction() throws Exception {
        var grammar = Grammar.builder()
                .add("S", Stubs.DUMMY_RULE_BUILDER)
                .add("A", Stubs.DUMMY_RULE_BUILDER)
                .build();

        assertEquals("S", grammar.getStartProduction().getSymbol());
    }

    @Test
    @DisplayName("getStartProduction() [Start symbol changed]")
    void getStartProductionInCaseStartSymbolChanged() throws Exception {
        var grammar = Grammar.builder()
                .add("S", Stubs.DUMMY_RULE_BUILDER)
                .add("A", Stubs.DUMMY_RULE_BUILDER)
                .setStartSymbol("A")
                .build();

        assertEquals("A", grammar.getStartProduction().getSymbol());
    }

}
