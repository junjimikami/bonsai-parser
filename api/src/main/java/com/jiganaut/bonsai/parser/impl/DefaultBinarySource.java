package com.jiganaut.bonsai.parser.impl;

import java.io.InputStream;

import com.jiganaut.bonsai.parser.BinarySource;
import com.jiganaut.bonsai.parser.Tokenizer;

/**
 *
 * @author Junji Mikami
 */
class DefaultBinarySource extends DefaultSource<Byte> implements BinarySource {

    DefaultBinarySource(InputStream inputStream) {
        super(new InputStreamTokenizer(inputStream));
    }

    DefaultBinarySource(Tokenizer<Byte> tokenizer) {
        super(tokenizer);
    }

}
