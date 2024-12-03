package com.jiganaut.bonsai.parser.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.impl.Message;

final class MessageSupport {

    private MessageSupport() {
    }

    static String tokenNotMatchRule(Rule rule, Context context) {
        var symbol = context.production().getSymbol();
        var line = context.getLineNumber();
        var index = context.getIndex();
        var token = "EOF";
        if (context.hasNext()) {
            context.next();
            token = context.getValue();
        }
        return Message.TOKEN_NOT_MATCH_RULE.format(rule, symbol, token, line, index);
    }

    static String ambiguousChoice(Rule rule, Context context) {
        var symbol = context.production().getSymbol();
        var firsetSet = FirstSet.of(rule, context)
                .stream()
                .map(Rule::toString)
                .collect(Collectors.joining(", ", "[", "]"));
        return Message.AMBIGUOUS_CHOICE.format(firsetSet, symbol);
    }

    static String ambiguousProductionSet(List<Production> list) {
        var productions = list.stream()
                .map(e -> e.toString())
                .collect(Collectors.joining(", ", "[", "]"));
        return Message.AMBIGUOUS_PRODUCTION_SET.format(productions);
    }

    static String tokenCountOutOfRange(QuantifierRule quantfier, Context context, long count) {
        var rule = quantfier.getRule();
        var symbol = context.production().getSymbol();
        var from = quantfier.getMinCount();
        var line = context.getLineNumber();
        var index = context.getIndex();
        var token = "EOF";
        if (context.hasNext()) {
            context.next();
            token = context.getValue();
        }
        if (quantfier.getMaxCount().isEmpty()) {
            return Message.TOKEN_COUNT_OUT_OF_RANGE_WITHOUT_UPPER_LIMIT.format(from, null, rule, symbol, count, token,
                    line, index);
        }
        var to = quantfier.getMaxCount().getAsInt();
        if (from == to) {
            return Message.TOKEN_COUNT_MISMATCH.format(from, to, rule, symbol, count, token, line, index);
        }
        return Message.TOKEN_COUNT_OUT_OF_RANGE.format(from, to, rule, symbol, count, token, line, index);
    }

    static String tokensRemained(Context context) {
        var line = context.getLineNumber();
        var index = context.getIndex();
        var token = "EOF";
        if (context.hasNext()) {
            context.next();
            token = context.getValue();
        }
        return Message.TOKEN_REMAINED.format(token, line, index);
    }
}
