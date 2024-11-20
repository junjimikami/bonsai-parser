package com.jiganaut.bonsai.parser.impl;

import java.util.LinkedList;
import java.util.Set;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.ReferenceRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.RuleVisitor;
import com.jiganaut.bonsai.grammar.SequenceRule;
import com.jiganaut.bonsai.grammar.ShortCircuitChoiceRule;
import com.jiganaut.bonsai.grammar.SkipRule;
import com.jiganaut.bonsai.parser.ParseException;
import com.jiganaut.bonsai.parser.Token;

/**
 * @author Junji Mikami
 *
 */
final class Tokenization implements RuleVisitor<CharSequence, Context> {
    private static final Tokenization INSTANCE = new Tokenization();
    private static final String EMPTY_STRING = "";

    private Tokenization() {
    }

    static Token run(Context context) {
        var list = context.productionSet().scope()
                .filter(e -> FirstSetMatcher.scan(e.getRule(), context))
                .toList();
        if (list.isEmpty()) {
            context.next();
            return context.getToken();
        }
        if (1 < list.size()) {
            var message = MessageSupport.ambiguousProductionSet(list);
            throw new ParseException(message);
        }
        var production = list.get(0);
        var subContext = context.withProduction(production);
        var value = INSTANCE.visit(production.getRule(), subContext).toString();
        return new DefaultToken(production.getSymbol(), value);
    }

    @Override
    public CharSequence visitChoice(ChoiceRule choice, Context context) {
        var rules = choice.getChoices().stream()
                .filter(e -> FirstSetMatcher.scan(e, context))
                .toList();
        if (rules.isEmpty()) {
            var message = MessageSupport.tokenNotMatchRule(choice, context);
            throw new ParseException(message);
        }
        if (rules.size() == 1) {
            return visit(rules.get(0), context);
        }
        var subContext = context.withFollowSet(Set.of());
        rules = rules.stream()
                .filter(e -> FirstSetMatcher.scan(e, subContext))
                .toList();
        if (rules.isEmpty()) {
            return EMPTY_STRING;
        }
        if (1 < rules.size()) {
            var message = MessageSupport.ambiguousChoice(choice, context);
            throw new ParseException(message);
        }
        return visit(rules.get(0), context);
    }

    @Override
    public CharSequence visitShortCircuitChoice(ShortCircuitChoiceRule choice, Context context) {
        int position = context.mark();
        for (var rule : choice.getChoices()) {
            if (FullLengthMatcher.scan(rule, context)) {
                context.reset(position);
                context.clear();
                return visit(rule, context);
            }
            context.reset(position);
        }
        var message = MessageSupport.tokenNotMatchRule(choice, context);
        throw new ParseException(message);
    }

    @Override
    public CharSequence visitSequence(SequenceRule sequence, Context context) {
        if (!FirstSetMatcher.scan(sequence, context)) {
            var message = MessageSupport.tokenNotMatchRule(sequence, context);
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
            var message = MessageSupport.tokenNotMatchRule(pattern, context);
            throw new ParseException(message);
        }
        context.next();
        return context.getValue();
    }

    @Override
    public CharSequence visitReference(ReferenceRule reference, Context context) {
        var productionSet = context.productionSet();
        var production = reference.lookup(productionSet);
        var subContext = context.withProduction(production);
        return visit(production.getRule(), subContext);
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
            var message = MessageSupport.tokenCountOutOfRange(quantfier, context, count);
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
            var message = MessageSupport.tokenNotMatchRule(empty, context);
            throw new ParseException(message);
        }
        return EMPTY_STRING;
    }

}
