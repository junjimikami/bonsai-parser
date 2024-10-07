package com.jiganaut.bonsai.grammar.impl;

import com.jiganaut.bonsai.grammar.Rule;

/**
 *
 * @author Junji Mikami
 */
abstract class AbstractRule implements Rule {

    static abstract class Builder extends BaseBuilder implements Rule.Builder {
    }

}
