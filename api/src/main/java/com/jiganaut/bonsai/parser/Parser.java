package com.jiganaut.bonsai.parser;

import java.io.Closeable;

public interface Parser extends Closeable {

    public Tree parse();
}
