package com.jiganaut.bonsai.grammar;

public interface GrammarVisitor<R, P> {

    public default R visit(Grammar grammar) {
        return visit(grammar, null);
    }

    public default R visit(Grammar grammar, P p) {
        return grammar.accept(this, p);
    }

    public R visitChoice(ChoiceGrammar grammar, P p);

    public R visitSingleOrigin(SingleOriginGrammar grammar, P p);
}
