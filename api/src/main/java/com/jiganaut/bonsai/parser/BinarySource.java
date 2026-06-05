package com.jiganaut.bonsai.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.jiganaut.bonsai.parser.spi.ParserProvider;

/**
 *
 * @author Junji Mikami
 */
public interface BinarySource extends Source<Byte> {

    public static BinarySource of(InputStream inputStream) {
        return ParserProvider.load().createBinarySource(inputStream);
    }

    public static BinarySource of(byte... bytes) {
        return of(new ByteArrayInputStream(bytes));
    }

}
