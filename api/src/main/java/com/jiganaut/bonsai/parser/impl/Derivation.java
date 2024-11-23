package com.jiganaut.bonsai.parser.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.jiganaut.bonsai.grammar.ChoiceGrammar;
import com.jiganaut.bonsai.grammar.ChoiceRule;
import com.jiganaut.bonsai.grammar.GrammarVisitor;
import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.ReferenceRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.RuleVisitor;
import com.jiganaut.bonsai.grammar.SequenceRule;
import com.jiganaut.bonsai.grammar.ShortCircuitChoiceRule;
import com.jiganaut.bonsai.grammar.SingleOriginGrammar;
import com.jiganaut.bonsai.grammar.SkipRule;
import com.jiganaut.bonsai.parser.ParseException;
import com.jiganaut.bonsai.parser.Tree;

/**
 * @author Junji Mikami
 *
 */
final class Derivation implements GrammarVisitor<Tree, Context>, RuleVisitor<List<Tree>, Context> {

    Tree process(Context context) {
        var tree = visit(context.grammar(), context);
        if (context.hasNext()) {
            var message = MessageSupport.tokensRemained(context);
            throw new ParseException(message);
        }
        return tree;
    }

    private Tree derive(Production production, Context context) {
        var subContext = context.withProduction(production);
        var trees = visit(production.getRule(), subContext);
        return new DefaultNonTerminalNode(production.getSymbol(), trees);
    }

    @Override
    public Tree visitSingleOrigin(SingleOriginGrammar grammar, Context context) {
        var origin = grammar.scope().findFirst();
        if (origin.isEmpty()) {
            throw new ParseException();
        }
        return derive(origin.get(), context);
    }

    @Override
    public Tree visitChoice(ChoiceGrammar grammar, Context context) {
        var list = grammar.scope()
                .filter(e -> FirstSetMatcher.scan(e.getRule(), context))
                .toList();
        if (list.isEmpty()) {
            throw new ParseException();
        }
        if (1 < list.size()) {
            var message = MessageSupport.ambiguousProductionSet(list);
            throw new ParseException(message);
        }
        return derive(list.get(0), context);
    }

    @Override
    public List<Tree> visitChoice(ChoiceRule choice, Context context) {
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
            return List.of();
        }
        if (1 < rules.size()) {
            var message = MessageSupport.ambiguousChoice(choice, context);
            throw new ParseException(message);
        }
        return visit(rules.get(0), context);
    }

    @Override
    public List<Tree> visitShortCircuitChoice(ShortCircuitChoiceRule choice, Context context) {
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
    public List<Tree> visitSequence(SequenceRule sequence, Context context) {
        if (!FirstSetMatcher.scan(sequence, context)) {
            var message = MessageSupport.tokenNotMatchRule(sequence, context);
            throw new ParseException(message);
        }
        var trees = new ArrayList<Tree>();
        var rules = new LinkedList<>(sequence.getRules());
        while (!rules.isEmpty()) {
            var rule = rules.removeFirst();
            var subFollowSet = FirstSet.of(rules, context);
            var subContext = context.withFollowSet(subFollowSet);
            trees.addAll(visit(rule, subContext));
        }
        return trees;
    }

    @Override
    public List<Tree> visitPattern(PatternRule pattern, Context context) {
        if (!FirstSetMatcher.scan(pattern, context)) {
            var message = MessageSupport.tokenNotMatchRule(pattern, context);
            throw new ParseException(message);
        }
        context.next();
        var token = context.getToken();
        return List.of(token);
    }

    @Override
    public List<Tree> visitReference(ReferenceRule reference, Context context) {
        var productionSet = context.grammar();
        var production = reference.lookup(productionSet);
        var tree = derive(production, context);
        return List.of(tree);
    }

    @Override
    public List<Tree> visitQuantifier(QuantifierRule quantfier, Context context) {
        var trees = new ArrayList<Tree>();
        long count = quantfier.stream()
                .takeWhile(e -> {
                    if (!FirstSetMatcher.scan(e, context)) {
                        return false;
                    }
                    return trees.addAll(visit(e, context));
                })
                .count();
        if (count < quantfier.getMinCount()) {
            var message = MessageSupport.tokenCountOutOfRange(quantfier, context, count);
            throw new ParseException(message);
        }
        return trees;
    }

    @Override
    public List<Tree> visitSkip(SkipRule skip, Context context) {
        visit(skip.getRule(), context);
        return List.of();
    }

    @Override
    public List<Tree> visitEmpty(Rule empty, Context context) {
        if (!FirstSetMatcher.scan(empty, context)) {
            var message = MessageSupport.tokenNotMatchRule(empty, context);
            throw new ParseException(message);
        }
        return List.of();
    }

}
