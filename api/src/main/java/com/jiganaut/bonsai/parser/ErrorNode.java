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
public interface ErrorNode extends NonTerminalNode {

    /**
     *
     */
    public static interface Builder extends NonTerminalNode.Builder {

        @Override
        public ErrorNode.Builder add(Tree tree);

        @Override
        public ErrorNode.Builder add(Tree.Builder builder);

        @Override
        public ErrorNode.Builder addAll(NonTerminalNode.Builder builder);

        @Override
        public ErrorNode build();

        public ErrorNode.Builder setGrammar(Grammar grammar);

        public ErrorNode.Builder setProductionPath(List<? extends ProductionRule> productionPath);

        public ErrorNode.Builder setExpectedRule(Rule expectedRule);

        public ErrorNode.Builder setMessage(String message);

    }

    public static ErrorNode.Builder builder(String name) {
        return ParserProvider.load().createErrorNodeBuilder(name);
    }

    public static ErrorNode.Builder builder() {
        return ParserProvider.load().createErrorNodeBuilder(null);
    }

    @Override
    public default Kind getKind() {
        return Kind.ERROR;
    }

     @Override
    public default <R, P> R accept(TreeVisitor<R, P> v, P p) {
        return v.visitError(this, p);
    }

    public Grammar getGrammar();

    public List<? extends ProductionRule> getProductionPath();

    public Rule getExpectedRule();

    public Tree getFoundToken();

    public String getMessage();

}
