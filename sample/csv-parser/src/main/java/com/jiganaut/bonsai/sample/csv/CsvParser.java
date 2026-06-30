package com.jiganaut.bonsai.sample.csv;

import static com.jiganaut.bonsai.grammar.Rules.concat;
import static com.jiganaut.bonsai.grammar.Rules.empty;
import static com.jiganaut.bonsai.grammar.Rules.firstOf;
import static com.jiganaut.bonsai.grammar.Rules.matching;
import static com.jiganaut.bonsai.grammar.Rules.oneOf;
import static com.jiganaut.bonsai.grammar.Rules.pattern;
import static com.jiganaut.bonsai.grammar.Rules.reference;

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.Parser;
import com.jiganaut.bonsai.parser.TextSource;
import com.jiganaut.bonsai.parser.Tree;

public class CsvParser implements Parser<String> {

    /**
     * https://www.rfc-editor.org/rfc/rfc4180
     *
     * file = [header CRLF] record *(CRLF record) [CRLF]
     * header = name *(COMMA name)
     * record = field *(COMMA field)
     * name = field
     * field = (escaped / non-escaped)
     * escaped = DQUOTE *(TEXTDATA / COMMA / CR / LF / 2DQUOTE) DQUOTE
     * non-escaped = *TEXTDATA
     * COMMA = %x2C
     * CR = %x0D
     * DQUOTE = %x22
     * LF = %x0A
     * CRLF = CR LF
     * TEXTDATA = %x20-21 / %x23-2B / %x2D-7E
     */
    private static final Grammar<String> GRAMMAR = Grammar.<String>builder("file")
            .add("file", firstOf(
                    concat(reference("record"), reference("crlf"), reference("file")),
                    concat(reference("record"), reference("crlf")),
                    reference("record")))
            .add("record", firstOf(
                    concat(reference("field"), reference("comma"), reference("record")),
                    reference("field")))
            .add("field", firstOf(
                    reference("escaped"),
                    reference("non-escaped")))
            .add("escaped", concat(
                    reference("dquote"),
                    reference("escaped-content"),
                    reference("dquote")))
            .add("escaped-content", oneOf(
                    empty(),
                    concat(reference("escaped-char"), reference("escaped-content"))))
            .add("escaped-char", oneOf(
                    reference("textdata"),
                    reference("comma"),
                    reference("cr"),
                    reference("lf"),
                    concat(reference("dquote"), reference("dquote"))))
            .add("non-escaped", oneOf(
                    empty(),
                    concat(reference("textdata"), reference("non-escaped"))))
            .add("dquote", matching("\""))
            .add("comma", matching(","))
            .add("cr", matching("\r"))
            .add("lf", matching("\n"))
            .add("crlf", concat(reference("cr"), reference("lf")))
            .add("textdata", pattern("[\\x20-\\x21\\x23-\\x2B\\x2D-\\x7E]"))
            .build();

    private final Reader reader;

    CsvParser(Reader reader) {
        this.reader = Objects.requireNonNull(reader);
    }

    @Override
    public Tree<String> parse() {
        return TextSource.of(reader)
                .toParser(GRAMMAR)
                .parse();
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

}
