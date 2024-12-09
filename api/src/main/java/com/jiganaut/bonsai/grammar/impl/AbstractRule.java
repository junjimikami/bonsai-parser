package com.jiganaut.bonsai.grammar.impl;

import com.jiganaut.bonsai.grammar.Rule;
import com.jiganaut.bonsai.impl.BaseBuilder;

/**
 *
 * @author Junji Mikami
 */
abstract class AbstractRule implements Rule {

    /**
     * 
     * @author Junji Mikami
     */
    static abstract class Builder extends BaseBuilder implements Rule.Builder {
    }

}
