package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Junji Mikami
 */
class RuleTest {

    @Test
    @DisplayName("Kind.isComposite()")
    void kindIsComposite() throws Exception {
        assertFalse(Rule.Kind.MATCH.isComposite());
        assertTrue(Rule.Kind.SEQUENCE.isComposite());
        assertTrue(Rule.Kind.CHOICE.isComposite());
        assertFalse(Rule.Kind.REFERENCE.isComposite());
        assertFalse(Rule.Kind.QUANTIFIER.isComposite());
        assertFalse(Rule.Kind.SKIP.isComposite());
        assertFalse(Rule.Kind.EMPTY.isComposite());
        assertFalse(Rule.Kind.PRODUCTION.isComposite());
    }

}