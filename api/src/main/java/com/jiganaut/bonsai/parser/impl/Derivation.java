package com.jiganaut.bonsai.parser.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.jiganaut.bonsai.grammar.PatternRule;
import com.jiganaut.bonsai.grammar.Production;
import com.jiganaut.bonsai.grammar.QuantifierRule;
import com.jiganaut.bonsai.grammar.ReferenceRule;
import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.grammar.SequenceRule;
import com.jiganaut.bonsai.grammar.SkipRule;
import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.ParseException;
import com.jiganaut.bonsai.parser.Tree;

/**
 * @author Junji Mikami
 *
 */
final class Derivation extends AbstractProcessor<Tree, List<Tree>> {

    Tree process(Context context) {
        var productionSet = context.grammar().productionSet();
        var tree = visitProductionSet(productionSet, context);
        if (context.hasNext()) {
            var args = new Object[3];
            args[1] = context.getLineNumber();
            args[2] = context.getIndex();
            args[0] = Message.stringEncode(context.nextValue());
            var message = Message.TOKENS_REMAINING.format(args);
            throw new ParseException(message);
        }
        return tree;
    }

    @Override
    public Tree visitProduction(Production production, Context context) {
        var subContext = context.withProduction(production);
        var trees = visit(production.getRule(), subContext);
        return new DefaultNonTerminalNode(production.getSymbol(), trees);
    }

    @Override
    public List<Tree> visitSequence(SequenceRule sequence, Context context) {
        if (!FirstSetMatcher.scan(sequence, context)) {
            var message = noMatchingRule(sequence, context);
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
            var message = noMatchingRule(pattern, context);
            throw new ParseException(message);
        }
        var token = context.next();
        return List.of(token);
    }

    @Override
    public List<Tree> visitReference(ReferenceRule reference, Context context) {
        var productionSet = reference.lookup(context.grammar());
        var tree = visitProductionSet(productionSet, context);
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
            var message = noMatchingRule(quantfier, context);
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
            var message = noMatchingRule(empty, context);
            throw new ParseException(message);
        }
        return List.of();
    }

}
