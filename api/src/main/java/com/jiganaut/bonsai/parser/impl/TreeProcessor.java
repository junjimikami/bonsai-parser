package com.jiganaut.bonsai.parser.impl;

import com.jiganaut.bonsai.parser.ParseException;
import com.jiganaut.bonsai.parser.Tree;

/**
 * @author Junji Mikami
 *
 */
class TreeProcessor<T> extends Processor<T, Tree<T>> {

    Tree<T> process(Context<T> context) {
        var productionChoice = context.grammar().toChoiceRule();
        var trees = visit(productionChoice, context).findFirst().get();
        if (!FirstSet.scan(context.endOfRule(), context)) {
            var errorNode = noMatchingRule(context.endOfRule(), context);
            throw new ParseException(errorNode);
        }
        return trees;
    }

}
