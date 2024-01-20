package com.unitedjiga.common.parsing.grammar;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@FunctionalInterface
interface ReferenceRelatedTest {
    Expression.Builder builder();

    @Test
    @DisplayName("build() [Null production set]")
    default void buildNullProductionSet() throws Exception {
        var builder = builder();

        assertThrows(NullPointerException.class, () -> builder.build())
                .printStackTrace();
    }

    @Test
    @DisplayName("build(ps:ProductionSet) [Null production set]")
    default void buildPsNullProductionSet() throws Exception {
        var builder = builder();

        assertThrows(NullPointerException.class, () -> builder.build(null))
                .printStackTrace();
    }

    @Test
    @DisplayName("build(ps:ProductionSet) [Invalid production set]")
    default void buildPsInvalidProductionSet() throws Exception {
        var builder = builder();

        assertThrows(NoSuchElementException.class, () -> builder.build(Stubs.INVALID_PRODUCTION_SET))
                .printStackTrace();
    }

}