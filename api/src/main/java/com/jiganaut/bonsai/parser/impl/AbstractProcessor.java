package com.jiganaut.bonsai.parser.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.ProductionSet;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.ParseException;

/**
 * 
 * @author Junji Mikami
 */
abstract class AbstractProcessor<R0, R1> implements ProductionProcessor<R0, Context>, RuleProcessor<R1, Context> {

    @Override
    public R0 visitProductionSetAsShortCircuit(ProductionSet productionSet, Context context) {
        int position = context.mark();
        for (var production : productionSet) {
            if (FullLengthMatcher.scan(production.getRule(), context)) {
                context.reset(position);
                context.clear();
                return visitProduction(production, context);
            }
            context.reset(position);
        }
        var message = noMatchingProductionRule(productionSet, context);
        throw new ParseException(message);
    }

    @Override
    public R0 visitProductionSetAsNotShortCircuit(ProductionSet productionSet, Context context) {
        var list = productionSet.stream()
                .filter(e -> FirstSetMatcher.scan(e.getRule(), context))
                .toList();
        if (list.isEmpty()) {
            var message = noMatchingProductionRule(productionSet, context);
            throw new ParseException(message);
        }
        if (1 < list.size()) {
            var message = ambiguousGrammar(list, context);
            throw new ParseException(message);
        }
        return visitProduction(list.get(0), context);
    }

    @Override
    public R1 visitChoiceAsShortCircuit(ChoiceRule choice, Context context) {
        int position = context.mark();
        for (var rule : choice.getChoices()) {
            if (FullLengthMatcher.scan(rule, context)) {
                context.reset(position);
                context.clear();
                return visit(rule, context);
            }
            context.reset(position);
        }
        var message = noMatchingRule(choice, context);
        throw new ParseException(message);
    }

    @Override
    public R1 visitChoiceAsNotShortCircuit(ChoiceRule choice, Context context) {
        var rules = choice.getChoices().stream()
                .filter(e -> FirstSetMatcher.scan(e, context))
                .toList();
        if (rules.isEmpty()) {
            var message = noMatchingRule(choice, context);
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
            var message = noMatchingRule(choice, context);
            throw new ParseException(message);
        }
        if (1 < rules.size()) {
            var message = ambiguousChoice(rules, context);
            throw new ParseException(message);
        }
        return visit(rules.get(0), context);
    }

    private static final String EOF = "EOF";

    String noMatchingProductionRule(ProductionSet productionSet, Context context) {
        var args = new Object[4];
        args[0] = productionSet;
        args[2] = context.getLineNumber();
        args[3] = context.getIndex();
        args[1] = context.hasNext() ? Message.stringEncode(context.nextValue()) : EOF;
        return Message.NO_MATCHING_PRODUCTION_RULE.format(args);
    }

    String ambiguousGrammar(List<Production> productions, Context context) {
        var args = new Object[4];
        args[0] = productions.stream()
                .map(Production::toString)
                .collect(Collectors.joining(" | "));
        args[2] = context.getLineNumber();
        args[3] = context.getIndex();
        args[1] = context.hasNext() ? Message.stringEncode(context.nextValue()) : EOF;
        return Message.AMBIGUOUS_GRAMMAR.format(args);
    }

    String noMatchingRule(Rule rule, Context context) {
        var args = new Object[5];
        args[0] = rule;
        args[1] = Message.symbolEncode(context.production().getSymbol());
        args[3] = context.getLineNumber();
        args[4] = context.getIndex();
        args[2] = context.hasNext() ? Message.stringEncode(context.nextValue()) : EOF;
        return Message.NO_MATCHING_RULE.format(args);
    }

    String ambiguousChoice(List<? extends Rule> rules, Context context) {
        var args = new Object[5];
        args[0] = rules.stream()
                .map(Rule::toString)
                .collect(Collectors.joining(" | "));
        args[1] = Message.symbolEncode(context.production().getSymbol());
        args[3] = context.getLineNumber();
        args[4] = context.getIndex();
        args[2] = context.hasNext() ? Message.stringEncode(context.nextValue()) : EOF;
        return Message.AMBIGUOUS_CHOICE.format(args);
    }
}
