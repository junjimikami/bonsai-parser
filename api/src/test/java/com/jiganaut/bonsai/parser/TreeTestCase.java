package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.jiganaut.bonsai.TestCase;
import com.jiganaut.bonsai.parser.Tree.Kind;

interface TreeTestCase extends TestCase {

    interface BuilderTestCase extends TestCase {
        Tree.Builder createTarget();

        Tree expectedTree();

        boolean canBuild();

        @Test
        @DisplayName("setName(String) [Post-build operation]")
        default void setNameInCaseOfPostBuild() throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.setName(""));
        }

        @Test
        @DisplayName("setValue(String) [Post-build operation]")
        default void setValueInCaseOfPostBuild() throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.setValue(""));
        }

        @Test
        @DisplayName("build() [Post-build operation]")
        default void buildInCaseOfPostBuild() throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();
            builder.build();

            assertThrows(IllegalStateException.class, () -> builder.build());
        }

        @ParameterizedTest
        @EmptySource
        @ValueSource(strings = { "1", "a", "[" })
        @DisplayName("setName()")
        default void setName(String name) throws Exception {
            var builder = createTarget();

            assertEquals(builder, builder.setName(name));
        }

        @ParameterizedTest
        @EmptySource
        @ValueSource(strings = { "1", "a", "[" })
        @DisplayName("setValue()")
        default void setValue(String value) throws Exception {
            var builder = createTarget();

            assertEquals(builder, builder.setName(value));
        }

        @Test
        @DisplayName("build()")
        default void build() throws Exception {
            assumeTrue(canBuild());

            var builder = createTarget();

            assertEquals(expectedTree(), builder.build());
        }

    }

    Tree createTarget();

    Kind expectedKind();

    String expectedName();

    String expectedValue();

    List<Tree> expectedSubTrees();

    TreeVisitor<Object[], String> testVisitor = new TreeVisitor<>() {

        @Override
        public Object[] visitNonTerminal(NonTerminalNode node, String p) {
            assertEquals(Tree.Kind.NON_TERMINAL, node.getKind());
            return new Object[] { node, p };
        }

        @Override
        public Object[] visitTerminal(TerminalNode node, String p) {
            assertEquals(Tree.Kind.TERMINAL, node.getKind());
            return new Object[] { node, p };
        }

    };

    @Test
    @DisplayName("objectMethods()")
    default void objectMethods() throws Exception {
        var target = createTarget();

        target.toString();
    }

    @Test
    @DisplayName("getKind()")
    default void getKind() throws Exception {
        var target = createTarget();

        assertEquals(expectedKind(), target.getKind());
    }

    @Test
    @DisplayName("getName()")
    default void getName() throws Exception {
        var target = createTarget();

        assertEquals(expectedName(), target.getName());
    }

    @Test
    @DisplayName("getValue()")
    default void getValue() throws Exception {
        var target = createTarget();

        assertEquals(expectedValue(), target.getValue());
    }

    @Test
    @DisplayName("getSubTrees()")
    default void getSubTrees() throws Exception {
        var target = createTarget();

        assertIterableEquals(expectedSubTrees(), target.getSubTrees());
    }

    @Test
    @DisplayName("accept(ev:ElementVisitor) [Null parameter]")
    default void acceptEvInCaseOfNullParameter() throws Exception {
        var target = createTarget();

        assertThrows(NullPointerException.class, () -> target.accept(null));
    }

    @Test
    @DisplayName("accept(tv:TreeVisitor)")
    default void acceptTv() throws Exception {
        var target = createTarget();
        var visitor = testVisitor;
        var expected = new Object[] { target, null };
        var result = target.accept(visitor);
        var result2 = visitor.visit(target);

        assertArrayEquals(expected, result);
        assertArrayEquals(expected, result2);
    }

    @DisplayName("accept(tv:treeVisitor, p:P)")
    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = { "test" })
    default void acceptTvP(String arg) throws Exception {
        var rule = createTarget();
        var visitor = testVisitor;
        var expected = new Object[] { rule, arg };
        var result = rule.accept(visitor, arg);
        var result2 = visitor.visit(rule, arg);

        assertArrayEquals(expected, result);
        assertArrayEquals(expected, result2);
    }
}
