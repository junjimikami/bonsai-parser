package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.jiganaut.bonsai.parser.NonTerminalNode.Builder;

class NonTerminalNodeTest {

    @Test
    @DisplayName("of(String, String, Tree...) [Null parameter]")
    void ofInCaseOfNullParameter() throws Exception {
        assertThrows(NullPointerException.class, () -> NonTerminalNode.of(null, ""));
        assertThrows(NullPointerException.class, () -> NonTerminalNode.of("", "", (Tree[]) null));
    }

    static Stream<Arguments> of() {
        return Stream.of(
                Arguments.arguments("", null, List.<Tree>of()),
                Arguments.arguments("", "", List.<Tree>of()),
                Arguments.arguments("1", "2", List.<Tree>of(mock(Tree.class))),
                Arguments.arguments("a", "b", List.<Tree>of(mock(Tree.class), mock(Tree.class))),
                Arguments.arguments("[", "*", List.<Tree>of()));
    }

    @ParameterizedTest
    @MethodSource
    @DisplayName("of(name:String, value:String)")
    void of(String name, String value, List<Tree> trees) throws Exception {
        var tree = NonTerminalNode.of(name, value, trees.toArray(Tree[]::new));

        assertEquals(Tree.Kind.NON_TERMINAL, tree.getKind());
        assertEquals(name, tree.getName());
        assertEquals(value, tree.getValue());
        assertIterableEquals(trees, tree.getSubTrees());
    }

    @Nested
    class TestCase1 implements NonTerminalNodeTestCase {

        List<Tree> testData = List.of(mock(Tree.class));

        @Override
        public NonTerminalNode createTarget() {
            return NonTerminalNode.of(
                    expectedName(),
                    expectedValue(),
                    expectedSubTrees().toArray(Tree[]::new));
        }

        @Override
        public String expectedName() {
            return "NAME";
        }

        @Override
        public String expectedValue() {
            return "VALUE";
        }

        @Override
        public List<Tree> expectedSubTrees() {
            return testData;
        }

    }

    @Nested
    class BuilderTestCase1 implements NonTerminalNodeTestCase.BuilderTestCase {
        @Override
        public Builder createTarget() {
            return NonTerminalNode.builder();
        }

        @Override
        public boolean isSetName() {
            return false;
        }

        @Override
        public NonTerminalNode expectedTree() {
            return null;
        }
    }

    @Nested
    class BuilderTestCase2 implements NonTerminalNodeTestCase.BuilderTestCase {
        @Override
        public Builder createTarget() {
            return NonTerminalNode.builder()
                    .setValue("VALUE");
        }

        @Override
        public boolean isSetName() {
            return false;
        }

        @Override
        public NonTerminalNode expectedTree() {
            return null;
        }
    }

    @Nested
    class BuilderTestCase3 implements NonTerminalNodeTestCase.BuilderTestCase {
        @Override
        public Builder createTarget() {
            return NonTerminalNode.builder()
                    .setName("NAME");
        }

        @Override
        public boolean isSetName() {
            return true;
        }

        @Override
        public NonTerminalNode expectedTree() {
            return NonTerminalNode.of("NAME", null);
        }
    }

    @Nested
    class BuilderTestCase4 implements NonTerminalNodeTestCase.BuilderTestCase {
        @Override
        public Builder createTarget() {
            return NonTerminalNode.builder()
                    .setName("NAME")
                    .setValue("VALUE");
        }

        @Override
        public boolean isSetName() {
            return true;
        }

        @Override
        public NonTerminalNode expectedTree() {
            return NonTerminalNode.of("NAME", "VALUE");
        }
    }

    @Nested
    class BuilderTestCase5 implements NonTerminalNodeTestCase.BuilderTestCase {

        Tree testData = mock(Tree.class);

        @Override
        public Builder createTarget() {
            return NonTerminalNode.builder()
                    .setName("NAME")
                    .add(testData);
        }

        @Override
        public boolean isSetName() {
            return true;
        }

        @Override
        public NonTerminalNode expectedTree() {
            return NonTerminalNode.of("NAME", null, testData);
        }
    }

    @Nested
    class BuilderTestCase6 implements NonTerminalNodeTestCase.BuilderTestCase {

        Tree testData = mock(Tree.class);

        @Override
        public Builder createTarget() {
            return NonTerminalNode.builder()
                    .add(testData)
                    .setName("NAME");
        }

        @Override
        public boolean isSetName() {
            return true;
        }

        @Override
        public NonTerminalNode expectedTree() {
            return NonTerminalNode.of("NAME", null, testData);
        }
    }

}
