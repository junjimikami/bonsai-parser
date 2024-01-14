package com.unitedjiga.common.parsing.impl;

import java.util.stream.Collectors;

import com.unitedjiga.common.parsing.grammar.Expression;
import com.unitedjiga.common.parsing.grammar.QuantifierExpression;

final class MessageSupport {

    private MessageSupport() {
    }

    static String tokenNotMatchExpression(Expression expression, Context context) {
        var symbol = context.getProduction().getSymbol();
        var firsetSet = FirstSet.of(expression, context.getFollowSet())
                .stream()
                .map(Expression::toString)
                .collect(Collectors.joining(", ", "[", "]"));
        var token = context.getTokenizer().hasNext()
                ? context.getTokenizer().next().toString()
                : "EOF";
        return Message.TOKEN_NOT_MATCH_EXPRESSION.format(symbol, firsetSet, token);
    }

    static String ambiguousChoice(Expression expression, Context context) {
        var symbol = context.getProduction().getSymbol();
        var firsetSet = FirstSet.of(expression, context.getFollowSet())
                .stream()
                .map(Expression::toString)
                .collect(Collectors.joining(", ", "[", "]"));
        return Message.AMBIGUOUS_CHOICE.format(symbol, firsetSet);
    }

    static String tokenCountOutOfRange(QuantifierExpression quantfier, Context context, int count) {
        var symbol = context.getProduction().getSymbol();
        var firsetSet = FirstSet.of(quantfier, context.getFollowSet())
                .stream()
                .map(Expression::toString)
                .collect(Collectors.joining(", ", "[", "]"));
        var from = quantfier.getLowerLimit();
        if (quantfier.getUpperLimit().isPresent()) {
            var to = quantfier.getUpperLimit().getAsInt();
            return Message.TOKEN_COUNT_OUT_OF_RANGE.format(symbol, firsetSet, "%d-%d".formatted(from, to), count);
        }
        return Message.TOKEN_COUNT_OUT_OF_RANGE.format(symbol, firsetSet, from, count);
    }
}
