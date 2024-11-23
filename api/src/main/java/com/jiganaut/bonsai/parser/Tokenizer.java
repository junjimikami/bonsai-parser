package com.jiganaut.bonsai.parser;

import java.io.Closeable;
import java.util.regex.Pattern;

/**
 *
 * @author Junji Mikami
 */
public interface Tokenizer extends Closeable {

    public boolean hasNext();
    public boolean hasNextName(String name);
    public boolean hasNextValue(String regex);
    public boolean hasNextValue(Pattern pattern);
    public Token next();
    public String nextName();
    public String nextValue();

    public String getName();
    public String getValue();

    public long getLineNumber();
    public long getIndex();

}
