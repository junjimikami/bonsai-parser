package com.jiganaut.bonsai.sample.json;

import static com.jiganaut.bonsai.grammar.Rules.concat;
import static com.jiganaut.bonsai.grammar.Rules.oneOf;
import static com.jiganaut.bonsai.grammar.Rules.pattern;
import static com.jiganaut.bonsai.grammar.Rules.quote;
import static com.jiganaut.bonsai.grammar.Rules.reference;

import java.io.Reader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.Parser;
import com.jiganaut.bonsai.parser.ParserFactory;
import com.jiganaut.bonsai.parser.Tokenizer;
import com.jiganaut.bonsai.parser.TokenizerFactory;

public class JsonParsesrFactory implements ParserFactory {

    private static final Grammar TOKEN = Grammar.builder()
            .add("token", oneOf(
                    reference("string"),
                    reference("number"),
                    reference("true"),
                    reference("false"),
                    reference("null"),
                    reference("ws")))
            .add("string", concat(quote("\""), reference("characters"), quote("\"")))
            .add("characters", reference("character").zeroOrMore())
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
                    reference("fraction").opt(),
                    reference("exponent").opt()))
            .add("integer", concat(
                    quote("-").opt(),
                    oneOf(
                            quote("0"),
                            concat(reference("onenine"), reference("digits").opt()))))
            .add("digits", reference("digit").oneOrMore())
            .add("digit", oneOf(
                    quote("0"),
                    reference("onenine")))
            .add("onenine", pattern("[1-9]"))
            .add("fraction", concat(quote("."), reference("digits")))
            .add("exponent", oneOf(
                    concat(quote("E"), reference("sign"), reference("digits")),
                    concat(quote("e"), reference("sign"), reference("digits"))))
            .add("sign", oneOf(
                    quote("+"),
                    quote("-")).opt())
            .add("true", concat(quote("t"), quote("r"), quote("u"), quote("e")))
            .add("false", concat(quote("f"), quote("a"), quote("l"), quote("s"), quote("e")))
            .add("null", concat(quote("n"), quote("u"), quote("l"), quote("l")))
            .add("ws", pattern("\\x{0020}").skip())
            .add("ws", pattern("\\x{000a}").skip())
            .add("ws", pattern("\\x{000d}").skip())
            .add("ws", pattern("\\x{0009}").skip())
            .build();

    private static final Grammar GRAMMAR = Grammar.builder()
            .add("json", reference("element"))
            .add("value", oneOf(
                    reference("object"),
                    reference("array"),
                    reference("string"),
                    reference("number"),
                    pattern("true"),
                    pattern("false"),
                    pattern("null")))
            .add("object", concat(
                    quote("{"),
                    reference("members").opt(),
                    quote("}")))
            .add("members", concat(
                    reference("member"),
                    concat(quote(","), reference("member")).zeroOrMore()))
            .add("member", concat(
                    reference("string"),
                    quote(":"),
                    reference("element")))
            .add("array", concat(
                    quote("["),
                    reference("elements").opt(),
                    quote("]")))
            .add("elements", concat(
                    reference("element"),
                    concat(quote(","), reference("element")).zeroOrMore()))
            .add("element", reference("value"))
            .add("string", pattern("\"([^\"]|[\\\\][\"])*\""))
            .add("number", pattern("[-0-9][\\S]*"))
            .build();

    @Override
    public Parser createParser(Tokenizer tokenizer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Parser createParser(Reader reader) {
        var tokenizer = TokenizerFactory.of(TOKEN).createTokenizer(reader);
        return ParserFactory.of(GRAMMAR).createParser(tokenizer);
    }

}
