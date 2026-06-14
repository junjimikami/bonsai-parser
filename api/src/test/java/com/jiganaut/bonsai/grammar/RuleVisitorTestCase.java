package com.jiganaut.bonsai.grammar;

import static com.jiganaut.bonsai.grammar.MockFactory.mockRule;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import com.jiganaut.bonsai.TestCase;

/**
 *
 * @author Junji Mikami
 */
interface RuleVisitorTestCase<T, R, P> extends TestCase {

    @Override
    RuleVisitor<T, R, P> createTarget();

    P createParameter();

    @Test
    @DisplayName("visit(ru:Rule) [ru == null]")
    default void visitRuWhenRuIsNull(TestReporter testReporter) throws Exception {
        var visitor = createTarget();

        var ex = assertThrows(NullPointerException.class, () -> visitor.visit((Rule<T>) null));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("visit(ru:Rule, p:P) [ru == null]")
    default void visitRuPWhenRuIsNull(TestReporter testReporter) throws Exception {
        var visitor = createTarget();

        var ex = assertThrows(NullPointerException.class, () -> visitor.visit((Rule<T>) null, createParameter()));
        testReporter.publishEntry("Exception: %s".formatted(ex.getMessage()));
    }

    @Test
    @DisplayName("visit(ru:Rule)")
    default void visit() throws Exception {
        var visitor = createTarget();
        Rule<T> rule = mockRule();

        visitor.visit(rule);

        verify(rule).accept(visitor);
    }

    @Test
    @DisplayName("visit(ru:Rule, p:P)")
    default void visitP() throws Exception {
        var visitor = createTarget();
        Rule<T> rule = mockRule();

        visitor.visit(rule, createParameter());

        verify(rule).accept(visitor, createParameter());
    }

}