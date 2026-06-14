package com.jiganaut.bonsai.parser;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.ProductionRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.spi.ParserProvider;

/**
 *
 * @author Junji Mikami
 */
public non-sealed interface ErrorNode<T> extends Tree<T> {

    /**
     *
     */
    public static interface Builder<T> extends Tree.Builder<T> {

        @Override
        public ErrorNode<T> build();

        public ErrorNode.Builder<T> setGrammar(Grammar<T> grammar);

        public ErrorNode.Builder<T> setProductionPath(List<ProductionRule<T>> productionPath);

        public ErrorNode.Builder<T> setExpectedRule(Rule<T> expectedRule);

        public ErrorNode.Builder<T> setFoundToken(Token<T> foundToken);

        public ErrorNode.Builder<T> setMessage(String message);

    }

    public static <T> ErrorNode.Builder<T> builder() {
        return ParserProvider.load().createErrorNodeBuilder();
    }

    @Override
    public default Kind getKind() {
        return Kind.ERROR;
    }

    @Override
    public default Position getPosition() {
        if (getFoundToken() == null) {
            return Position.UNKNOWN;
        }
        return getFoundToken().getPosition();
    }

    @Override
    public default Stream<Tree<T>> subTrees() {
        return Stream.ofNullable(getFoundToken());
    }

    @Override
    public default Stream<T> values() {
        return Stream.ofNullable(getFoundToken()).map(Token::getValue);
    }

    @Override
    public default <R, P> R accept(TreeVisitor<T, R, P> visitor, P p) {
        Objects.requireNonNull(visitor, () -> Message.VALIDATION_PARAMETER_NULL.format("visitor"));
        return visitor.visitError(this, p);
    }

    public Grammar<T> getGrammar();

    public List<ProductionRule<T>> getProductionPath();

    public Rule<T> getExpectedRule();

    public Token<T> getFoundToken();

    public String getMessage();

}
