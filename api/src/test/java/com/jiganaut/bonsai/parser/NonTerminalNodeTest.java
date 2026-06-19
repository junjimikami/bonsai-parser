package com.jiganaut.bonsai.parser;

import static com.jiganaut.bonsai.parser.MockFactory.mockToken;

import java.util.List;
import org.junit.jupiter.api.Nested;
import com.jiganaut.bonsai.parser.NonTerminalNode.Builder;

/**
 *
 * @author Junji Mikami
 */
class NonTerminalNodeTest {

    @Nested
    class TestCase1 implements NonTerminalNodeTestCase<String> {

        List<Tree<String>> testData = List.of(mockToken());

        @Override
        public NonTerminalNode<String> createTarget() {
            var builder = NonTerminalNode.<String>builder(expectedName());
            expectedSubTrees().forEach(builder::add);
            return builder.build();
        }

        @Override
        public String expectedName() {
            return "NAME";
        }

        @Override
        public List<Tree<String>> expectedSubTrees() {
            return testData;
        }

    }

    @Nested
    class BuilderTestCase3 implements NonTerminalNodeTestCase.BuilderTestCase<String> {
        @Override
        public Builder<String> createTarget() {
            return NonTerminalNode.<String>builder("NAME");
        }

        @Override
        public boolean canBuild() {
            return false;
        }

        @Override
        public NonTerminalNode<String> expectedTree() {
            return null;
        }
    }

    @Nested
    class BuilderTestCase5 implements NonTerminalNodeTestCase.BuilderTestCase<String> {

        Tree<String> testData = mockToken();

        @Override
        public Builder<String> createTarget() {
            return NonTerminalNode.<String>builder("NAME")
                    .add(testData);
        }

        @Override
        public boolean canBuild() {
            return true;
        }

        @Override
        public NonTerminalNode<String> expectedTree() {
            return NonTerminalNode.<String>builder("NAME")
                    .add(testData)
                    .build();
        }
    }

    @Nested
    class BuilderTestCase6 implements NonTerminalNodeTestCase.BuilderTestCase<String> {

        Tree<String> testData = mockToken();

        @Override
        public Builder<String> createTarget() {
            return NonTerminalNode.<String>builder("NAME")
                    .add(testData);
        }

        @Override
        public boolean canBuild() {
            return true;
        }

        @Override
        public NonTerminalNode<String> expectedTree() {
            return NonTerminalNode.<String>builder("NAME")
                    .add(testData)
                    .build();
        }
    }

}
