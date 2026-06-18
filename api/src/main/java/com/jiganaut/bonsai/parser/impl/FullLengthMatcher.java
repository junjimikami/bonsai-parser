package com.jiganaut.bonsai.parser.impl;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.EmptyRule;
import com.jiganaut.bonsai.grammar.MatchingRule;
import com.jiganaut.bonsai.grammar.ProductionRule;
import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.ReferenceRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.RuleVisitor;
import com.jiganaut.bonsai.grammar.SequenceRule;
import com.jiganaut.bonsai.grammar.SkipRule;
import com.jiganaut.bonsai.parser.Token;

/**
 * @author Junji Mikami
 *
 */
final class FullLengthMatcher<T> implements RuleVisitor<T, Token<T>, Context<T>> {

    private static final FullLengthMatcher<?> INSTANCE = new FullLengthMatcher<>();

    private FullLengthMatcher() {
    }

    static <T> Token<T> scan(Rule<T> rule, Context<T> context) {
        @SuppressWarnings("unchecked")
        var instance = (FullLengthMatcher<T>) INSTANCE;
        return instance.visit(rule, context.resetPath());
    }

    @Override
    public Token<T> visitChoiceAsShortCircuit(ChoiceRule<T> choice, Context<T> context) {
        var errorTokens = new ArrayList<Token<T>>();
        var cursor = context.startCache();
        for (var rule : choice.getChoices()) {
            var errorToken = visit(rule, context);
            if (errorToken == null) {
                return null;
            }
            errorTokens.add(errorToken);
            cursor.reset();
        }
        return errorTokens.stream()
                .max((e1, e2) -> e1.getPosition().compareTo(e2.getPosition()))
                .orElseGet(context::peek);
    }

    @Override
    public Token<T> visitChoice(ChoiceRule<T> choice, Context<T> context) {
        var candidates = choice.getChoices().stream()
                .filter(e -> FirstSet.scan(e, context))
                .toList();
        if (candidates.isEmpty()) {
            return context.peek();
        }
        if (candidates.size() == 1) {
            return visit(candidates.get(0), context);
        }
        var subContext = context.subContext(Set::of);
        candidates = candidates.stream()
                .filter(e -> FirstSet.scan(e, subContext))
                .toList();
        if (candidates.isEmpty()) {
            return context.peek();
        }
        if (1 < candidates.size()) {
            return context.peek();
        }
        return visit(candidates.get(0), context);
    }

    @Override
    public Token<T> visitSequence(SequenceRule<T> sequence, Context<T> context) {
        var rules = new ArrayListRule<>(sequence);
        while (!rules.isEmpty()) {
            var rule = rules.removeFirst();
            var subContext = context.subContext(() -> FirstSet.of(rules, context));
            var token = visit(rule, subContext);
            if (token != null) {
                return token;
            }
        }
        return null;
    }

    @Override
    public Token<T> visitMatch(MatchingRule<T> match, Context<T> context) {
        var token = context.peek();
        if (!match.test(token)) {
            return token;
        }
        context.next();
        return null;
    }

    @Override
    public Token<T> visitReference(ReferenceRule<T> reference, Context<T> context) {
        var productionChoice = reference.lookup(context.grammar());
        return visit(productionChoice, context);
    }

    @Override
    public Token<T> visitQuantifier(QuantifierRule<T> quantifier, Context<T> context) {
        long count = quantifier.stream()
                .map(e -> visit(e, context))
                .takeWhile(Objects::isNull)
                .count();
        if (count < quantifier.getMinCount()) {
            return context.peek();
        }
        return null;
    }

    @Override
    public Token<T> visitSkip(SkipRule<T> skip, Context<T> context) {
        return visit(skip.getRule(), context);
    }

    @Override
    public Token<T> visitEmpty(EmptyRule<T> empty, Context<T> context) {
        return context.followSet().stream().anyMatch(e -> e.test(context.peek()))
                ? null
                : context.peek();
    }

    @Override
    public Token<T> visitProduction(ProductionRule<T> production, Context<T> context) {
        var subContext = context.subContext(production);
        return visit(production.getRule(), subContext);
    }

}
