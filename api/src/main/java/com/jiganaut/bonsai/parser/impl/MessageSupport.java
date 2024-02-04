package com.jiganaut.bonsai.parser.impl;

import java.util.stream.Collectors;

import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.Rule;

final class MessageSupport {

    private MessageSupport() {
    }

    static String tokenNotMatchRule(Rule rule, Context context) {
        var symbol = context.production().getSymbol();
        var firsetSet = FirstSet.of(rule, context.followSet())
                .stream()
                .map(Rule::toString)
                .collect(Collectors.joining(", ", "[", "]"));
        var token = context.tokenizer().hasNext()
                ? context.tokenizer().next().toString()
                : "EOF";
        return Message.TOKEN_NOT_MATCH_RULE.format(symbol, firsetSet, token);
    }

    static String ambiguousChoice(Rule rule, Context context) {
        var symbol = context.production().getSymbol();
        var firsetSet = FirstSet.of(rule, context.followSet())
                .stream()
                .map(Rule::toString)
                .collect(Collectors.joining(", ", "[", "]"));
        return Message.AMBIGUOUS_CHOICE.format(symbol, firsetSet);
    }

    static String tokenCountOutOfRange(QuantifierRule quantfier, Context context, long count) {
        var symbol = context.production().getSymbol();
        var firsetSet = FirstSet.of(quantfier, context.followSet())
                .stream()
                .map(Rule::toString)
                .collect(Collectors.joining(", ", "[", "]"));
        var from = quantfier.getMinCount();
        if (quantfier.getMaxCount().isPresent()) {
            var to = quantfier.getMaxCount().getAsInt();
            return Message.TOKEN_COUNT_OUT_OF_RANGE.format(symbol, firsetSet, "%d-%d".formatted(from, to), count);
        }
        return Message.TOKEN_COUNT_OUT_OF_RANGE.format(symbol, firsetSet, from, count);
    }
}
