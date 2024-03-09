package com.jiganaut.bonsai.parser.impl;

import java.util.stream.Collectors;

import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.Rule.Kind;
import com.jiganaut.bonsai.grammar.SimpleRuleVisitor;

/**
 * @author Junji Mikami
 *
 */
final class AnyMatcher implements SimpleRuleVisitor<Boolean, Context> {
    private static final AnyMatcher instance = new AnyMatcher();

    private AnyMatcher() {
    }

    static boolean scan(Rule rule, Context context) {
        if (instance.visit(rule, context)) {
            return true;
        }
        context.skip();
        return instance.visit(rule, context);
    }

    @Override
    public Boolean visitPattern(PatternRule pattern, Context context) {
        var tokenizer = context.tokenizer();
        return tokenizer.hasNext(pattern.getPattern());
    }

    @Override
    public Boolean defaultAction(Rule rule, Context context) {
        var firstSet = FirstSet.of(rule, context.followSet());
        if (firstSet.isEmpty()) {
            return !context.tokenizer().hasNext();
        }
        var map = firstSet.stream()
                .collect(Collectors.partitioningBy(e -> e.getKind() == Kind.PATTERN));
        return map.get(true).stream().anyMatch(e -> visit(e, context)) ||
                map.get(false).stream().anyMatch(e -> visit(e, context));
    }
}
