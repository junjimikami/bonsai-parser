package com.jiganaut.bonsai.parser.impl;

import java.util.List;
import java.util.Objects;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.ProductionRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.impl.BaseBuilder;
import com.jiganaut.bonsai.parser.ErrorNode;
import com.jiganaut.bonsai.parser.Token;

/**
 *
 * @author Junji Mikami
 *
 */
class DefaultErrorNode<T> implements ErrorNode<T> {

    /**
     *
     */
    static class Builder<T> extends BaseBuilder  implements ErrorNode.Builder<T> {

        private String message;
        private Grammar<T> grammar;
        private List<ProductionRule<T>> productionPath;
        private Rule<T> expectedRule;
        private Token<T> foundToken;

        @Override
        public ErrorNode.Builder<T> setGrammar(Grammar<T> grammar) {
            check();
            this.grammar = grammar;
            return this;
        }

        @Override
        public ErrorNode.Builder<T> setProductionPath(List<ProductionRule<T>> productionPath) {
            check();
            this.productionPath = productionPath;
            return this;
        }

        @Override
        public ErrorNode.Builder<T> setExpectedRule(Rule<T> expectedRule) {
            check();
            this.expectedRule = expectedRule;
            return this;
        }

        @Override
        public ErrorNode.Builder<T> setFoundToken(Token<T> foundToken) {
            check();
            this.foundToken = foundToken;
            return this;
        }

        @Override
        public ErrorNode.Builder<T> setMessage(String message) {
            check();
            this.message = message;
            return this;
        }

        @Override
        public ErrorNode<T> build() {
            checkForBuild();
            return new DefaultErrorNode<>(message, grammar, productionPath, expectedRule, foundToken);
        }

    }

    private final String message;
    private final Grammar<T> grammar;
    private final List<ProductionRule<T>> productionPath;
    private final Rule<T> expectedRule;
    private final Token<T> foundToken;

    private DefaultErrorNode(
            String message,
            Grammar<T> grammar,
            List<ProductionRule<T>> productionPath,
            Rule<T> expectedRule,
            Token<T> foundToken) {
        this.message = message;
        this.grammar = grammar;
        this.productionPath = productionPath;
        this.expectedRule = expectedRule;
        this.foundToken = foundToken;
    }

    static String toString(ErrorNode<?> error, String indent) {
        var sb = new StringBuilder();
        // Message
        sb.append("message: ");
        sb.append(Objects.toString(error.getMessage(), ""));
        // Grammar
        sb.append("\n");
        sb.append(indent);
        sb.append("grammar:");
        sb.append(grammarToString(error.getGrammar(), indent + "  "));
        // Production path
        sb.append("\n");
        sb.append(indent);
        sb.append("production path: ");
        sb.append(productionPathToString(error.getProductionPath(), indent + "  "));
        // Expected rule
        sb.append("\n");
        sb.append(indent);
        sb.append("expected: ");
        sb.append(Objects.toString(error.getExpectedRule(), ""));
        // Found token
        sb.append("\n");
        sb.append(indent);
        sb.append("found: ");
        sb.append(foundTokenToString(error.getFoundToken(), indent + "  "));
        return sb.toString();
    }

    private static String grammarToString(Grammar<?> grammar, String indent) {
        if (grammar == null) {
            return "";
        }
        var sb = new StringBuilder();
        for (var production : grammar.getProductionRules()) {
            sb.append("\n");
            sb.append(indent);
            sb.append(production.getSymbol());
            sb.append(": ");
            sb.append(production.getRule());
        }
        return sb.toString();
    }

    private static String productionPathToString(List<? extends ProductionRule<?>> productionPath, String indent) {
        if (productionPath == null) {
            return "";
        }
        var sb = new StringBuilder();
        productionPath.reversed().stream()
                .map(ProductionRule::getSymbol)
                .forEach(s -> sb.append("\n")
                        .append(indent)
                        .append("- ")
                        .append(s));
        return sb.toString();
    }

    private static String foundTokenToString(Token<?> foundToken, String indent) {
        if (foundToken == null) {
            return "null";
        }
        var sb = new StringBuilder();
        if (foundToken.getName() != null) {
            sb.append("\n");
            sb.append(indent);
            sb.append("name: ");
            sb.append(foundToken.getName());
        }
        if (foundToken instanceof EndOfToken<?>) {
            sb.append("\n");
            sb.append(indent);
            sb.append("kind: EOF");
        } else {
            sb.append("\n");
            sb.append(indent);
            sb.append("value: ");
            sb.append(foundToken.getValue());
        }
        sb.append("\n");
        sb.append(indent);
        sb.append("position: ");
        sb.append(foundToken.getPosition());
        return sb.toString();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Grammar<T> getGrammar() {
        return grammar;
    }

    @Override
    public List<ProductionRule<T>> getProductionPath() {
        return productionPath;
    }

    @Override
    public Rule<T> getExpectedRule() {
        return expectedRule;
    }

    @Override
    public Token<T> getFoundToken() {
        return foundToken;
    }

    @Override
    public String toString() {
        return toString(this, "");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ErrorNode<?> other) {
            return this.getKind() == other.getKind()
                    && Objects.equals(this.message, other.getMessage())
                    && Objects.equals(this.grammar, other.getGrammar())
                    && Objects.equals(this.productionPath, other.getProductionPath())
                    && Objects.equals(this.expectedRule, other.getExpectedRule())
                    && Objects.equals(this.foundToken, other.getFoundToken());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKind(), message, grammar, productionPath, expectedRule, foundToken);
    }

}
