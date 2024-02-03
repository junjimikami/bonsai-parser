package com.unitedjiga.common.parsing.impl;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.regex.Pattern;

import com.unitedjiga.common.parsing.Tokenizer;

abstract class AbstractTokenizer implements Tokenizer {

    @Override
    public Tokenizer skip(String regex) {
        Objects.requireNonNull(regex, Message.NULL_PARAMETER.format());
        var pattern = Pattern.compile(regex);
        return skip(pattern);
    }

    @Override
    public Tokenizer skip(Pattern pattern) {
        if (!hasNext(pattern)) {
            throw new NoSuchElementException();
        }
        next(pattern);
        return this;
    }
}
