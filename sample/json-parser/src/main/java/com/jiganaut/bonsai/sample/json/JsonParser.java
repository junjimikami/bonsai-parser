package com.jiganaut.bonsai.sample.json;

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
import com.jiganaut.bonsai.grammar.Rules;
import com.jiganaut.bonsai.parser.Parser;
import com.jiganaut.bonsai.parser.TextSource;
import com.jiganaut.bonsai.parser.Tree;

public class JsonParser implements Parser<String> {

    /**
     * https://www.json.org/json-en.html
     */
    private static final Grammar<String> GRAMMAR = Grammar.<String>builder("json")
            .add("json", reference("element"))
            .add("value", oneOf(
                    reference("object"),
                    reference("array"),
                    reference("string"),
                    reference("number"),
                    concat(matching("t"), matching("r"), matching("u"), matching("e")),
                    concat(matching("f"), matching("a"), matching("l"), matching("s"), matching("e")),
                    concat(matching("n"), matching("u"), matching("l"), matching("l"))))
            .add("object", firstOf(
                    concat(matching("{"), reference("ws"), matching("}")),
                    concat(matching("{"), reference("members"), matching("}"))))
            .add("members", firstOf(
                    concat(reference("member"), matching(","), reference("members")),
                    concat(reference("member"))))
            .add("member", concat(
                    reference("ws"),
                    reference("string"),
                    reference("ws"),
                    matching(":"),
                    reference("element")))
            .add("array", firstOf(
                    concat(matching("["), reference("ws"), matching("]")),
                    concat(matching("["), reference("elements"), matching("]"))))
            .add("elements", firstOf(
                    concat(reference("element"), matching(","), reference("elements")),
                    concat(reference("element"))))
            .add("element", concat(
                    reference("ws"),
                    reference("value"),
                    reference("ws")))
            .add("string", concat(
                    matching("\""),
                    reference("characters"),
                    matching("\"")))
            .add("characters", oneOf(
                    empty(),
                    concat(reference("character"), reference("characters"))))
            .add("character", oneOf(
                    pattern("[\\x{0020}-\\x{10ffff}&&[^\"\\\\]]"),
                    concat(matching("\\"), reference("escape"))))
            .add("escape", oneOf(
                    matching("\""),
                    matching("\\"),
                    matching("/"),
                    matching("b"),
                    matching("f"),
                    matching("n"),
                    matching("r"),
                    matching("t"),
                    concat(matching("u"), Rules.<String>reference("hex").exactly(4))))
            .add("hex", oneOf(
                    reference("digit"),
                    pattern("[a-fA-F]")))
            .add("number", concat(
                    reference("integer"),
                    reference("fraction"),
                    reference("exponent")))
            .add("integer", firstOf(
                    concat(reference("onenine"), reference("digits")),
                    concat(reference("digit")),
                    concat(matching("-"), reference("onenine"), reference("digits")),
                    concat(matching("-"), reference("digit"))))
            .add("digits", firstOf(
                    concat(reference("digit"), reference("digits")),
                    concat(reference("digit"))))
            .add("digit", oneOf(
                    matching("0"),
                    reference("onenine")))
            .add("onenine", pattern("[1-9]"))
            .add("fraction", oneOf(
                    empty(),
                    concat(matching("."), reference("digits"))))
            .add("exponent", oneOf(
                    empty(),
                    concat(matching("E"), reference("sign"), reference("digits")),
                    concat(matching("e"), reference("sign"), reference("digits"))))
            .add("sign", oneOf(
                    empty(),
                    matching("+"),
                    matching("-")))
            .add("ws", oneOf(
                    empty(),
                    concat(pattern("\\x{0020}"), reference("ws")),
                    concat(pattern("\\x{000a}"), reference("ws")),
                    concat(pattern("\\x{000d}"), reference("ws")),
                    concat(pattern("\\x{0009}"), reference("ws"))))
            .build();

    private final Reader reader;

    JsonParser(Reader reader) {
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
