package com.jiganaut.bonsai.parser;

import java.util.List;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.ProductionRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.parser.spi.ParserProvider;

/**
 *
 * @author Junji Mikami
 */
public interface ErrorNode<T> extends NonTerminalNode<T> {

    /**
     *
     */
    public static interface Builder<T> extends NonTerminalNode.Builder<T> {

        @Override
        public ErrorNode.Builder<T> add(Tree<T> tree);

        @Override
        public ErrorNode.Builder<T> add(Tree.Builder<T> builder);

        @Override
        public ErrorNode.Builder<T> addAll(NonTerminalNode.Builder<T> builder);

        @Override
        public ErrorNode<T> build();

        public ErrorNode.Builder<T> setGrammar(Grammar<T> grammar);

        public ErrorNode.Builder<T> setProductionPath(List<ProductionRule<T>> productionPath);

        public ErrorNode.Builder<T> setExpectedRule(Rule<T> expectedRule);

        public ErrorNode.Builder<T> setMessage(String message);

    }

    public static <T> ErrorNode.Builder<T> builder(String name) {
        return ParserProvider.load().createErrorNodeBuilder(name);
    }

    public static <T> ErrorNode.Builder<T> builder() {
        return ParserProvider.load().createErrorNodeBuilder(null);
    }

    @Override
    public default Kind getKind() {
        return Kind.ERROR;
    }

     @Override
    public default <R, P> R accept(TreeVisitor<T, R, P> v, P p) {
        return v.visitError(this, p);
    }

    public Grammar<T> getGrammar();

    public List<ProductionRule<T>> getProductionPath();

    public Rule<T> getExpectedRule();

    public Tree<T> getFoundToken();

    public String getMessage();

}
