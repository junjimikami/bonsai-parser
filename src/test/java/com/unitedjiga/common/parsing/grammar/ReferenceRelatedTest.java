package com.unitedjiga.common.parsing.grammar;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

interface ReferenceRelatedTest {
    Expression.Builder builder();

    @Test
    @DisplayName("build(ps:ProductionSet) [Null production set]")
    default void buildPsInCaseNullProductionSet() throws Exception {
        var builder = builder();

        assertThrows(NullPointerException.class, () -> builder.build(null))
                .printStackTrace();
    }

    @Test
    @DisplayName("build(ps:ProductionSet) [Invalid reference]")
    default void buildPsInCaseInvalidReference() throws Exception {
        var builder = builder();

        assertThrows(NoSuchElementException.class, () -> builder.build(Stubs.EMPTY_PRODUCTION_SET))
                .printStackTrace();
    }

}