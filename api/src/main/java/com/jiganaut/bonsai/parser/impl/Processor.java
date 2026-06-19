package com.jiganaut.bonsai.parser.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

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
import com.jiganaut.bonsai.impl.Message;
import com.jiganaut.bonsai.parser.ErrorNode;
import com.jiganaut.bonsai.parser.ParseException;
import com.jiganaut.bonsai.parser.Token;
import com.jiganaut.bonsai.parser.Tree;

/**
 *
 * @author Junji Mikami
 */
abstract class Processor<T, R> implements RuleVisitor<T, Stream<Tree<T>>, Context<T>> {

    abstract R process(Context<T> context);

    @Override
    public Stream<Tree<T>> visitChoiceAsShortCircuit(ChoiceRule<T> choice, Context<T> context) {
        var errorTokens = new ArrayList<Token<T>>();
        var cursor = context.startCache();
        for (var rule : choice.getChoices()) {
            var errorToken = FullLengthMatcher.scan(rule, context);
            if (errorToken == null) {
                cursor.clear();
                return visit(rule, context);
            }
            errorTokens.add(errorToken);
            cursor.reset();
        }
        var errorToken = errorTokens.stream()
                .max((e1, e2) -> e1.getPosition().compareTo(e2.getPosition()))
                .orElseGet(context::peek);
        var errorNode = noMatchingRule(choice, context, errorToken);
        throw new ParseException(errorNode);
    }

    @Override
    public Stream<Tree<T>> visitChoice(ChoiceRule<T> choice, Context<T> context) {
        var candidates = choice.getChoices().stream()
                .filter(e -> FirstSet.scan(e, context))
                .toList();
        if (candidates.isEmpty()) {
            var errorNode = noMatchingRule(choice, context);
            throw new ParseException(errorNode);
        }
        if (candidates.size() == 1) {
            return visit(candidates.get(0), context);
        }
        var subContext = context.subContext(Set::of);
        candidates = candidates.stream()
                .filter(e -> FirstSet.scan(e, subContext))
                .toList();
        if (candidates.isEmpty()) {
            var errorNode = noMatchingRule(choice, context);
            throw new ParseException(errorNode);
        }
        if (1 < candidates.size()) {
            var errorNode = ambiguousChoice(choice, candidates, context);
            throw new ParseException(errorNode);
        }
        return visit(candidates.get(0), context);
    }

    @Override
    public Stream<Tree<T>> visitSequence(SequenceRule<T> sequence, Context<T> context) {
        var builder = Stream.<Tree<T>>builder();
        var rules = new ArrayListRule<>(sequence);
        while (!rules.isEmpty()) {
            var rule = rules.removeFirst();
            var subContext = context.subContext(() -> FirstSet.of(rules, context));
            if (!FirstSet.scan(rule, subContext)) {
                var errorNode = noMatchingRule(rule, context);
                throw new ParseException(errorNode);
            }
            var trees = visit(rule, subContext);
            trees.forEach(builder::add);
        }
        return builder.build();
    }

    @Override
    public Stream<Tree<T>> visitMatch(MatchingRule<T> match, Context<T> context) {
        if (match instanceof EndOfRule) {
            return Stream.empty();
        }
        return Stream.of(context.next());
    }

    @Override
    public Stream<Tree<T>> visitReference(ReferenceRule<T> reference, Context<T> context) {
        var productionChoice = reference.lookup(context.grammar());
        return visit(productionChoice, context);
    }

    @Override
    public Stream<Tree<T>> visitQuantifier(QuantifierRule<T> quantifier, Context<T> context) {
        var builder = Stream.<Tree<T>>builder();
        long count = quantifier.stream()
                .takeWhile(e -> {
                    if (!FirstSet.scan(e, context)) {
                        return false;
                    }
                    visit(e, context).forEach(builder::add);
                    return true;
                })
                .count();
        if (count < quantifier.getMinCount()) {
            var errorNode = noMatchingRule(quantifier, context);
            throw new ParseException(errorNode);
        }
        return builder.build();
    }

    @Override
    public Stream<Tree<T>> visitSkip(SkipRule<T> skip, Context<T> context) {
        visit(skip.getRule(), context);
        return Stream.empty();
    }

    @Override
    public Stream<Tree<T>> visitEmpty(EmptyRule<T> empty, Context<T> context) {
        return Stream.empty();
    }

    @Override
    public Stream<Tree<T>> visitProduction(ProductionRule<T> production, Context<T> context) {
        var builder = new DefaultNonTerminalNode.Builder<T>(production.getSymbol());
        var rule = production.getRule();
        var subContext = context.subContext(production);
        var trees = visit(rule, subContext);
        trees.forEach(builder::add);
        return Stream.of(builder.build());
    }

    ErrorNode<T> noMatchingRule(Rule<T> rule, Context<T> context, Token<T> token) {
        return ErrorNode.<T>builder()
                .setMessage(Message.PARSER_NO_MATCHING_RULE.format(rule, token))
                .setGrammar(context.grammar())
                .setProductionPath(context.productionPath())
                .setExpectedRule(rule)
                .setFoundToken(token)
                .build();
    }

    ErrorNode<T> noMatchingRule(Rule<T> rule, Context<T> context) {
        return noMatchingRule(rule, context, context.peek());
    }

    ErrorNode<T> ambiguousChoice(Rule<T> rule, List<Rule<T>> candidates, Context<T> context) {
        return ErrorNode.<T>builder()
                .setMessage(Message.PARSER_AMBIGUOUS_CHOICE.format(candidates, context.peek()))
                .setGrammar(context.grammar())
                .setProductionPath(context.productionPath())
                .setExpectedRule(rule)
                .setFoundToken(context.peek())
                .build();
    }

}
