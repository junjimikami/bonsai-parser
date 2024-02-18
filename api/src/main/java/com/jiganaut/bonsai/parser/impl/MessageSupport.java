package com.jiganaut.bonsai.parser.impl;

import java.util.stream.Collectors;

import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.Rule;

final class MessageSupport {

    private MessageSupport() {
    }

    static String tokenNotMatchRule(Rule rule, Context context) {
        var symbol = context.production().getSymbol();
        var line = context.tokenizer().getLineNumber();
        var index = context.tokenizer().getIndex();
        var token = context.tokenizer().hasNext()
                ? context.tokenizer().next().toString()
                : "EOF";
        return Message.TOKEN_NOT_MATCH_RULE.format(rule, symbol, token, line, index);
    }

    static String ambiguousChoice(Rule rule, Context context) {
        var symbol = context.production().getSymbol();
        var firsetSet = FirstSet.of(rule, context.followSet())
                .stream()
                .map(Rule::toString)
                .collect(Collectors.joining(", ", "[", "]"));
        return Message.AMBIGUOUS_CHOICE.format(firsetSet, symbol);
    }

    static String tokenCountOutOfRange(QuantifierRule quantfier, Context context, long count) {
        var rule = quantfier.getRule();
        var symbol = context.production().getSymbol();
        var from = quantfier.getMinCount();
        var line = context.tokenizer().getLineNumber();
        var index = context.tokenizer().getIndex();
        var token = context.tokenizer().hasNext()
                ? context.tokenizer().next().toString()
                : "EOF";
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
        var line = context.tokenizer().getLineNumber();
        var index = context.tokenizer().getIndex();
        var token = context.tokenizer().hasNext()
                ? context.tokenizer().next().toString()
                : "EOF";
        return Message.TOKEN_REMAINED.format(token, line, index);
    }
}
