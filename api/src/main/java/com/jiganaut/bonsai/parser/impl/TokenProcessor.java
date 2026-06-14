package com.jiganaut.bonsai.parser.impl;

import java.util.stream.Collector;

import com.jiganaut.bonsai.parser.NonTerminalNode;
import com.jiganaut.bonsai.parser.Token;

/**
 * @author Junji Mikami
 *
 */
final class TokenProcessor<T, R> extends Processor<T, Token<R>> {

    private final Collector<? super T, ?, R> collector;

    TokenProcessor(Collector<? super T, ?, R> collector) {
        assert collector != null;
        this.collector = collector;
    }

    @Override
    Token<R> process(Context<T> context) {
        var productionChoice = context.grammar().toChoiceRule();
        while (context.hasNext()) {
            var tree = visit(productionChoice, context)
                    .map(e -> (NonTerminalNode<T>) e)
                    .findFirst()
                    .get();
            if (tree.getSubTrees().isEmpty()) {
                continue;
            }
            var name = tree.getName();
            var value = tree.values().collect(collector);
            var position = tree.getPosition();
            return new DefaultToken<>(name, value, position);
        }
        return null;
    }

}
