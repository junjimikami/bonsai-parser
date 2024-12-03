package com.jiganaut.bonsai.sample.json;

import static com.jiganaut.bonsai.grammar.Rules.concat;
import static com.jiganaut.bonsai.grammar.Rules.empty;
import static com.jiganaut.bonsai.grammar.Rules.firstOf;
import static com.jiganaut.bonsai.grammar.Rules.oneOf;
import static com.jiganaut.bonsai.grammar.Rules.pattern;
import static com.jiganaut.bonsai.grammar.Rules.quote;
import static com.jiganaut.bonsai.grammar.Rules.reference;

import java.io.Reader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.grammar.SingleOriginGrammar;
import com.jiganaut.bonsai.parser.Parser;
import com.jiganaut.bonsai.parser.ParserFactory;
import com.jiganaut.bonsai.parser.Tokenizer;

public class JsonParsesrFactory implements ParserFactory {

    /**
     * https://www.json.org/json-en.html
     */
    private static final Grammar GRAMMAR = SingleOriginGrammar.builder()
            .add("json", reference("element"))
            .add("value", oneOf(
                    reference("object"),
                    reference("array"),
                    reference("string"),
                    reference("number"),
                    concat(quote("t"), quote("r"), quote("u"), quote("e")),
                    concat(quote("f"), quote("a"), quote("l"), quote("s"), quote("e")),
                    concat(quote("n"), quote("u"), quote("l"), quote("l"))))
            .add("object", firstOf(
                    concat(quote("{"), reference("ws"), quote("}")),
                    concat(quote("{"), reference("members"), quote("}"))))
            .add("members", firstOf(
                    concat(reference("member"), quote(","), reference("members")),
                    concat(reference("member"))))
            .add("member", concat(
                    reference("ws"),
                    reference("string"),
                    reference("ws"),
                    quote(":"),
                    reference("element")))
            .add("array", firstOf(
                    concat(quote("["), reference("ws"), quote("]")),
                    concat(quote("["), reference("elements"), quote("]"))))
            .add("elements", firstOf(
                    concat(reference("element"), quote(","), reference("elements")),
                    concat(reference("element"))))
            .add("element", concat(
                    reference("ws"),
                    reference("value"),
                    reference("ws")))
            .add("string", concat(
                    quote("\""),
                    reference("characters"),
                    quote("\"")))
            .add("characters", oneOf(
                    empty(),
                    concat(reference("character"), reference("characters"))))
            .add("character", oneOf(
                    pattern("[\\x{0020}-\\x{10ffff}&&[^\"\\\\]]"),
                    concat(quote("\\"), reference("escape"))))
            .add("escape", oneOf(
                    quote("\""),
                    quote("\\"),
                    quote("/"),
                    quote("b"),
                    quote("f"),
                    quote("n"),
                    quote("r"),
                    quote("t"),
                    concat(quote("u"), reference("hex").exactly(4))))
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
                    concat(quote("-"), reference("onenine"), reference("digits")),
                    concat(quote("-"), reference("digit"))))
            .add("digits", firstOf(
                    concat(reference("digit"), reference("digits")),
                    concat(reference("digit"))))
            .add("digit", oneOf(
                    quote("0"),
                    reference("onenine")))
            .add("onenine", pattern("[1-9]"))
            .add("fraction", oneOf(
                    empty(),
                    concat(quote("."), reference("digits"))))
            .add("exponent", oneOf(
                    empty(),
                    concat(quote("E"), reference("sign"), reference("digits")),
                    concat(quote("e"), reference("sign"), reference("digits"))))
            .add("sign", oneOf(
                    empty(),
                    quote("+"),
                    quote("-")))
            .add("ws", oneOf(
                    empty(),
                    concat(pattern("\\x{0020}"), reference("ws")),
                    concat(pattern("\\x{000a}"), reference("ws")),
                    concat(pattern("\\x{000d}"), reference("ws")),
                    concat(pattern("\\x{0009}"), reference("ws"))))
            .build();

    @Override
    public Parser createParser(Tokenizer tokenizer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Parser createParser(Reader reader) {
        return ParserFactory.of(GRAMMAR).createParser(reader);
    }

}
