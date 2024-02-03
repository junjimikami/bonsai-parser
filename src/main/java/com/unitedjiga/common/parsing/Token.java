package com.unitedjiga.common.parsing;

/**
 *
 * @author Junji Mikami
 */
public interface Token extends Terminal {

    public String getValue();

    @Override
    public String toString();
}
