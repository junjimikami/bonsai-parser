package com.jiganaut.bonsai.parser.impl;

import java.util.LinkedList;

import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.ReferenceRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.SequenceRule;
import com.jiganaut.bonsai.grammar.SkipRule;
import com.jiganaut.bonsai.parser.ParseException;

/**
 * @author Junji Mikami
 *
 */
final class Tokenization extends AbstractProcessor<CharSequence, CharSequence> {
    private static final String EMPTY_STRING = "";
    private String name;

    String process(Context context) {
        var productionSet = context.grammar().productionSet();
        return visitProductionSet(productionSet, context).toString();
    }

    String getName() {
        return name;
    }

    @Override
    public CharSequence visitProduction(Production production, Context context) {
        var subContext = context.withProduction(production);
        var value = visit(production.getRule(), subContext);
        name = production.getSymbol();
        return value;
    }

    @Override
    public CharSequence visitSequence(SequenceRule sequence, Context context) {
        if (!FirstSetMatcher.scan(sequence, context)) {
            var message = noMatchingRule(sequence, context);
            throw new ParseException(message);
        }
        var builder = new StringBuilder();
        var rules = new LinkedList<>(sequence.getRules());
        while (!rules.isEmpty()) {
            var rule = rules.removeFirst();
            var subFollowSet = FirstSet.of(rules, context);
            var subContext = context.withFollowSet(subFollowSet);
            builder.append(visit(rule, subContext));
        }
        return builder;
    }

    @Override
    public CharSequence visitPattern(PatternRule pattern, Context context) {
        if (!FirstSetMatcher.scan(pattern, context)) {
            var message = noMatchingRule(pattern, context);
            throw new ParseException(message);
        }
        return context.nextValue();
    }

    @Override
    public CharSequence visitReference(ReferenceRule reference, Context context) {
        var productionSet = reference.lookup(context.grammar());
        return visitProductionSet(productionSet, context);
    }

    @Override
    public CharSequence visitQuantifier(QuantifierRule quantfier, Context context) {
        var builder = new StringBuilder();
        long count = quantfier.stream()
                .takeWhile(e -> {
                    if (!FirstSetMatcher.scan(e, context)) {
                        return false;
                    }
                    builder.append(visit(e, context));
                    return true;
                })
                .count();
        if (count < quantfier.getMinCount()) {
            var message = noMatchingRule(quantfier, context);
            throw new ParseException(message);
        }
        return builder;
    }

    @Override
    public CharSequence visitSkip(SkipRule skip, Context context) {
        visit(skip.getRule(), context);
        return EMPTY_STRING;
    }

    @Override
    public CharSequence visitEmpty(Rule empty, Context context) {
        if (!FirstSetMatcher.scan(empty, context)) {
            var message = noMatchingRule(empty, context);
            throw new ParseException(message);
        }
        return EMPTY_STRING;
    }

}
