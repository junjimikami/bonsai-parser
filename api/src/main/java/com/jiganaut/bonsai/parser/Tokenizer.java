package com.jiganaut.bonsai.parser;

import java.io.Closeable;
import java.util.regex.Pattern;

/**
 *
 * @author Junji Mikami
 */
public interface Tokenizer extends Closeable {

    public boolean hasNext();
    public boolean hasNext(String regex);
    public boolean hasNext(Pattern pattern);
    public String next();
    public String next(String regex);
    public String next(Pattern pattern);

    public String getValue();
    public Token getToken();

    public long getLineNumber();
    public long getIndex();

}
