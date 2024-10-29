package com.jiganaut.bonsai.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.jiganaut.bonsai.parser.TerminalNode.Builder;

class TerminalNodeTest {

    @Test
    @DisplayName("of(String, String) [Null parameter]")
    void ofInCaseOfNullParameter() throws Exception {
        assertThrows(NullPointerException.class, () -> TerminalNode.of("", null));
        assertThrows(NullPointerException.class, () -> TerminalNode.of(null, null));
    }

    @ParameterizedTest
    @CsvSource(nullValues = "nil", value = {
            "nil, ''",
            " '', ''",
            "  1, 2",
            "  a, b",
            "  [, *",
    })
    @DisplayName("of(name:String, value:String)")
    void of(String name, String value) throws Exception {
        var tree = TerminalNode.of(name, value);

        assertEquals(Tree.Kind.TERMINAL, tree.getKind());
        assertEquals(name, tree.getName());
        assertEquals(value, tree.getValue());
        assertTrue(tree.getSubTrees().isEmpty());
    }

    @Nested
    class TestCase1 implements TerminalNodeTestCase {

        @Override
        public TerminalNode createTarget() {
            return TerminalNode.of(expectedName(), expectedValue());
        }

        @Override
        public String expectedName() {
            return "NAME";
        }

        @Override
        public String expectedValue() {
            return "VALUE";
        }

    }

    @Nested
    class BuilderTestCase1 implements TerminalNodeTestCase.BuilderTestCase {

        @Override
        public Builder createTarget() {
            return TerminalNode.builder();
        }

        @Override
        public boolean isSetValue() {
            return false;
        }

        @Override
        public TerminalNode expectedTree() {
            return null;
        }
    }

    @Nested
    class BuilderTestCase2 implements TerminalNodeTestCase.BuilderTestCase {

        @Override
        public Builder createTarget() {
            return TerminalNode.builder()
                    .setName("NAME");
        }

        @Override
        public boolean isSetValue() {
            return false;
        }

        @Override
        public TerminalNode expectedTree() {
            return null;
        }
    }

    @Nested
    class BuilderTestCase3 implements TerminalNodeTestCase.BuilderTestCase {

        @Override
        public Builder createTarget() {
            return TerminalNode.builder()
                    .setValue("VALUE");
        }

        @Override
        public boolean isSetValue() {
            return true;
        }

        @Override
        public TerminalNode expectedTree() {
            return TerminalNode.of(null, "VALUE");
        }
    }

    @Nested
    class BuilderTestCase4 implements TerminalNodeTestCase.BuilderTestCase {

        @Override
        public Builder createTarget() {
            return TerminalNode.builder()
                    .setName("NAME")
                    .setValue("VALUE");
        }

        @Override
        public boolean isSetValue() {
            return true;
        }

        @Override
        public TerminalNode expectedTree() {
            return TerminalNode.of("NAME", "VALUE");
        }
    }

    @Nested
    class BuilderTestCase5 implements TerminalNodeTestCase.BuilderTestCase {

        @Override
        public Builder createTarget() {
            return TerminalNode.builder()
                    .setName("1")
                    .setValue("2")
                    .setName("NAME")
                    .setValue("VALUE");
        }

        @Override
        public boolean isSetValue() {
            return true;
        }

        @Override
        public TerminalNode expectedTree() {
            return TerminalNode.of("NAME", "VALUE");
        }
    }

}
