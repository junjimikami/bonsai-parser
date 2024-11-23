package com.jiganaut.bonsai.grammar;

/**
 * 
 * @author Junji Mikami
 *
 */
interface ChoiceGrammarTestCase extends GrammarTestCase {

    interface BuilderTestCase extends GrammarTestCase.BuilderTestCase {

        @Override
        ChoiceGrammar.Builder createTarget();

    }

    @Override
    ChoiceGrammar createTarget();

}
