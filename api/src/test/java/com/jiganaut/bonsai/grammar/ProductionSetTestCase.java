package com.jiganaut.bonsai.grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import com.jiganaut.bonsai.TestCase;

/**
 * 
 * @author Junji Mikami
 *
 */
interface ProductionSetTestCase extends TestCase {

    @Override
    ProductionSet createTarget();

    Set<Production> expectedSet();

    default boolean expectedToBeShortCircuit() {
        return false;
    }

    @Test
    @DisplayName("containsSymbol(String)")
    default void containsSymbol() throws Exception {
        var target = createTarget();

        expectedSet().forEach(e -> {
            var symbol = e.getSymbol();
            assertTrue(target.containsSymbol(symbol));
        });
        assertFalse(target.containsSymbol(null));
        assertFalse(target.containsSymbol(""));
    }

    @Test
    @DisplayName("withSymbol(String) [No such symbol]")
    default void withSymbolInCaseOfNoSuchSymbol(TestReporter testReporter) throws Exception {
        var target = createTarget();

        var ex0 = assertThrows(NoSuchElementException.class, () -> target.withSymbol(null));
        testReporter.publishEntry(ex0.getMessage());
        var ex1 = assertThrows(NoSuchElementException.class, () -> target.withSymbol(""));
        testReporter.publishEntry(ex1.getMessage());
    }

    @Test
    @DisplayName("withSymbol(String)")
    default void withSymbol() throws Exception {
        var target = createTarget();

        expectedSet().forEach(e -> {
            var symbol = e.getSymbol();
            var set = target.withSymbol(symbol);
            assertTrue(set.stream()
                    .map(Production::getSymbol)
                    .allMatch(e2 -> e2.equals(symbol)));
        });
    }

    @Test
    @DisplayName("isShortCircuit()")
    default void isShortCircuit() throws Exception {
        var target = createTarget();

        assertEquals(expectedToBeShortCircuit(), target.isShortCircuit());
    }

    @Test
    @DisplayName("shortCircuit()")
    default void shortCircuit() throws Exception {
        var target = createTarget();

        assertNotSame(target, target.shortCircuit());
        assertTrue(target.shortCircuit().isShortCircuit());
    }

    @Test
    @DisplayName("iterator()")
    default void iterator() throws Exception {
        var target = createTarget();

        var expected = new HashSet<String>();
        expectedSet().iterator().forEachRemaining(e -> expected.add(e.getSymbol() + ":" + e.getRule()));
        var actual = new HashSet<String>();
        target.iterator().forEachRemaining(e -> actual.add(e.getSymbol() + ":" + e.getRule()));
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("add(pr:Production)")
    default void addPr() throws Exception {
        var target = createTarget();

        assertThrows(UnsupportedOperationException.class, () -> target.add(mock(Production.class)));
    }

    @Test
    @DisplayName("remove(ob:Object)")
    default void removeOb() throws Exception {
        var target = createTarget();
        var production = target.iterator().next();

        assertThrows(UnsupportedOperationException.class, () -> target.remove(production));
    }

}
