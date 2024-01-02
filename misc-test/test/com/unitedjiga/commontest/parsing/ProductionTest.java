package com.unitedjiga.commontest.parsing;

import static com.unitedjiga.common.parsing.Expression.of;
import static com.unitedjiga.common.parsing.Expression.oneOf;
import static com.unitedjiga.common.parsing.Expression.ref;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.unitedjiga.common.parsing.NonTerminalSymbol;
import com.unitedjiga.common.parsing.ParsingException;
import com.unitedjiga.common.parsing.Expression;
import com.unitedjiga.common.parsing.Symbol;
import com.unitedjiga.common.parsing.SymbolVisitor;
import com.unitedjiga.common.parsing.TerminalSymbol;
import com.unitedjiga.common.parsing.Tokenizer;

class ProductionTest {

//    static SymbolVisitor<Void, String> printer = new SymbolVisitor<Void, String>() {
//
//        @Override
//        public Void visitTerminal(TerminalSymbol s, String p) {
//            System.out.print(p);
//            System.out.println(s.getKind() + "=[" + s + "]");
//            return null;
//        }
//
//        @Override
//        public Void visitNonTerminal(NonTerminalSymbol s, String p) {
//            System.out.print(p);
//            System.out.println(s.getKind() + "=[" + s + "]");
//            s.forEach(e -> e.accept(this, "  " + p));
//            return null;
//        }
//        
//        public Void visit(Symbol s) {
//            return visit(s, "+-");
//        }
//    };
//
//    static void testSuccess(Production p, String in) {
//        var arr = in.chars().mapToObj(Character::toString).iterator();
//        var pser = p.parser(Tokenizer.wrap(arr));
//        var smbl = pser.parse();
//        assertEquals(in, smbl.asToken().getValue());
//        printer.visit(smbl);
////        assertTrue(p.asPattern().matcher(in).matches());
//
////        TokenizerFactory tf = TokenizerFactory.newInstance();
////        try (Tokenizer tzer = tf.createTokenizer(new StringReader(in));
////                Parser pser = p.parser(tzer)) {
//////            Symbol s = p.parseRemaining(tzer.buffer());
////            Symbol s = pser.next();
////            s.accept(printer, "+-");
////        }
//    }
//
//    static void testFailure(Production p, String in) {
//        var arr = in.chars().mapToObj(Character::toString).iterator();
//        var pser = p.parser(Tokenizer.wrap(arr));
//        assertThrows(ParsingException.class, pser::parse).printStackTrace(System.out);
////        assertFalse(p.asPattern().matcher(in).matches());
//
////        TokenizerFactory tf = TokenizerFactory.newInstance();
////        try (Tokenizer tzer = tf.createTokenizer(new StringReader(in));
////                Parser pser = p.parser(tzer)) {
////            assertThrows(ParsingException.class, () -> {
////                try {
//////                    p.parseRemaining(tzer.buffer());
////                    pser.next();
////                } catch (ParsingException e) {
////                    System.out.println(e);
////                    throw e;
////                }
////            });
////        }
//    }
//
////    static void testInit(Production p) {
////        System.out.println(p);
//////        assertEquals(p.toString(), p.asPattern().pattern());
////    }
//
//    @BeforeEach
//    void beforeTest() {
//        System.out.println("_________________________________________________");
//    }
//
//    @Test
//    void test1_1() throws IOException {
//        Production A = Production.of();
//
//        testSuccess(A, "");
//        testFailure(A, "1");
//        testFailure(A, "0");
//    }
//
//    @Test
//    void test1_2() throws IOException {
//        Production A = Production.of("1");
//
//        testFailure(A, "");
//        testSuccess(A, "1");
//        testFailure(A, "0");
//        testFailure(A, "11");
//    }
//
//    @Test
//    void test1_3() throws IOException {
//        Production A = Production.of("1", "0");
//
//        testFailure(A, "");
//        testSuccess(A, "10");
//        testFailure(A, "1");
//        testFailure(A, "0");
//        testFailure(A, "01");
//        testFailure(A, "100");
//    }
//
//    @Test
//    void test1_4() throws IOException {
//        Production A = Production.of("1|0");
//
//        testFailure(A, "");
//        testSuccess(A, "1");
//        testSuccess(A, "0");
//        testFailure(A, "10");
//        testFailure(A, "01");
//    }
//
//    @Test
//    void test2_1() throws IOException {
//        Production A = Production.oneOf();
//
//        testSuccess(A, "");
//        testFailure(A, "1");
//    }
//
//    @Test
//    void test2_2() throws IOException {
//        Production A = Production.oneOf("1");
//
//        testFailure(A, "");
//        testSuccess(A, "1");
//        testFailure(A, "11");
//    }
//
//    @Test
//    void test2_3() throws IOException {
//        Production A = Production.oneOf("1", "0");
//
//        testFailure(A, "");
//        testSuccess(A, "1");
//        testSuccess(A, "0");
//        testFailure(A, "11");
//        testFailure(A, "00");
//        testFailure(A, "10");
//    }
//
//    @Test
//    void test3_1() throws IOException {
//        Production A = Production.of().opt();
//
//        testSuccess(A, "");
//        testFailure(A, "1");
//    }
//
//    @Test
//    void test3_2() throws IOException {
//        Production A = Production.of("1").opt();
//
//        testSuccess(A, "");
//        testSuccess(A, "1");
//        testFailure(A, "0");
//        testFailure(A, "11");
//    }
//
//    @Test
//    void test3_3() throws IOException {
//        Production A = Production.of("1", "0").opt();
//
//        testSuccess(A, "");
//        testSuccess(A, "10");
//        testFailure(A, "1");
//        testFailure(A, "0");
//    }
//
//    @Test
//    void test4_1() throws IOException {
//        Production A = Production.of().repeat();
//
//        testSuccess(A, "");
//        testFailure(A, "1");
//    }
//
//    @Test
//    void test4_2() throws IOException {
//        Production A = Production.oneOf().repeat();
//
//        testSuccess(A, "");
//        testFailure(A, "1");
//    }
//
//    @Test
//    void test4_3() throws IOException {
//        Production A = Production.of("1").repeat();
//
//        testSuccess(A, "");
//        testSuccess(A, "1");
//        testSuccess(A, "11");
//        testSuccess(A, "111");
//        testFailure(A, "0");
//        testFailure(A, "10");
//    }
//
//    @Test
//    void test4_4() throws IOException {
//        Production A = Production.of("1", "0").repeat();
//
//        testSuccess(A, "");
//        testSuccess(A, "10");
//        testSuccess(A, "1010");
//        testSuccess(A, "101010");
//        testFailure(A, "0");
//        testFailure(A, "1");
//        testFailure(A, "01");
//        testFailure(A, "101");
//    }
//
//    @Test
//    void test4_5() throws IOException {
//        Production A = Production.oneOf("1").repeat();
//
//        testSuccess(A, "");
//        testSuccess(A, "1");
//        testSuccess(A, "11");
//        testSuccess(A, "111");
//        testFailure(A, "0");
//        testFailure(A, "10");
//    }
//
//    @Test
//    void test4_6() throws IOException {
//        Production A = Production.oneOf("1", "0").repeat();
//
//        testSuccess(A, "");
//        testSuccess(A, "1");
//        testSuccess(A, "0");
//        testSuccess(A, "11");
//        testSuccess(A, "10");
//        testSuccess(A, "01");
//        testSuccess(A, "00");
//        testSuccess(A, Integer.toBinaryString(12));
//        testFailure(A, "2");
//    }
//
//    @Test
//    void test1_001() throws IOException {
//        Production B = Production.of();
//        Production A = Production.of(B);
//
//        testSuccess(A, "");
//        testFailure(A, "1");
//        testFailure(A, "0");
//    }
//
//    @Test
//    void test1_002() throws IOException {
//        Production B = Production.of("1");
//        Production A = Production.of(B);
//
//        testFailure(A, "");
//        testSuccess(A, "1");
//        testFailure(A, "0");
//        testFailure(A, "11");
//    }
//
//    @Test
//    void test1_003() throws IOException {
//        Production B = Production.of("1", "0");
//        Production A = Production.of(B);
//
//        testFailure(A, "");
//        testSuccess(A, "10");
//        testFailure(A, "1");
//        testFailure(A, "0");
//        testFailure(A, "01");
//    }
//
//    @Test
//    void test1_004() throws IOException {
//        Production C = Production.of();
//        Production B = Production.of(C);
//        Production A = Production.of(B);
//
//        testSuccess(A, "");
//        testFailure(A, "1");
//        testFailure(A, "0");
//    }
//
//    @Test
//    void test1_005() throws IOException {
//        Production C = Production.of("1");
//        Production B = Production.of(C.opt());
//        Production A = Production.of(B, "0");
//
//        testFailure(A, "");
//        testSuccess(A, "10");
//        testSuccess(A, "0");
//        testFailure(A, "1");
//    }
//
//    @Test
//    void test1_006() throws IOException {
//        Production C = Production.of("0");
//        Production B = Production.of("1", C.opt());
//        Production A = Production.of("0", B.repeat());
//
//        testFailure(A, "");
//        testSuccess(A, "0");
//        testSuccess(A, "01");
//        testSuccess(A, "010");
//        testSuccess(A, "0101");
//        testSuccess(A, "010110");
//        testFailure(A, "1");
//    }
//
//    @Test
//    void test1_007() throws IOException {
//        Production C = Production.of("0");
//        Production B = Production.of("1");
//        Production A = Production.of(B.opt(), C.opt());
//
//        testSuccess(A, "");
//        testSuccess(A, "1");
//        testSuccess(A, "0");
//        testSuccess(A, "10");
//        testFailure(A, "11");
//    }
//
//    class CsvParsingTest {
//        /*
//         * COMMA = %x2C
//         * CR = %x0D ;as per section 6.1 of RFC 2234 [2]
//         * DQUOTE = %x22 ;as per section 6.1 of RFC 2234 [2]
//         * LF = %x0A ;as per section 6.1 of RFC 2234 [2]
//         * CRLF = CR LF ;as per section 6.1 of RFC 2234 [2]
//         * TEXTDATA = %x20-21 / %x23-2B / %x2D-7E
//         */
//        Production COMMA = of("\\x2C");
//        Production CR = of("\\x0D");
//        Production DQUOTE = of("\\x22");
//        Production LF = of("\\x0A");
//        Production CRLF = of(CR, LF);
//        Production TEXTDATA = oneOf("[\\x20-\\x21]", "[\\x23-\\x2B]", "[\\x2D-\\x7E]");
//
//        /*
//         * file = [header CRLF] record *(CRLF record) [CRLF]
//         * header = name *(COMMA name)
//         * record = field *(COMMA field)
//         * name = field
//         * field = (escaped / non-escaped)
//         * escaped = DQUOTE *(TEXTDATA / COMMA / CR / LF / 2DQUOTE) DQUOTE
//         * non-escaped = *TEXTDATA
//         */
//        Production file() {
//            return of(of(header(), CRLF).opt(),
//                    record(),
//                    file_recoreds());
//        }
//        Production file_recoreds() {
//            return of(CRLF, oneOf(empty(),
//                    of(record(), ref(this::file_recoreds)))
//                    ).opt();
//        }
//        Production header() {
//            return of(name(), of(COMMA, name()).repeat());
//        }
//        Production record() {
//            return of(field(), of(COMMA, field()).repeat());
//        }
//        Production name() {
//            return field();
//        }
//        Production field() {
//            return oneOf(escaped(), non_escaped());
//        }
//        Production escaped() {
//            return of(DQUOTE, escaped_contents());
//        }
//        Production escaped_contents() {
//            return oneOf(of(DQUOTE, oneOf(empty(),
//                            of(DQUOTE, ref(this::escaped_contents)))),
//                    of(TEXTDATA, ref(this::escaped_contents)),
//                    of(COMMA, ref(this::escaped_contents)),
//                    of(CR, ref(this::escaped_contents)),
//                    of(LF, ref(this::escaped_contents))
//                    );
//        }
//        Production non_escaped() {
//            return TEXTDATA.repeat();
//        }
//
//    }
//
//    @Test
//    void testCsvParsing01() throws IOException {
//        StringBuilder sb = new StringBuilder();
//        sb.append("A,\"B\",C\r\n");
//        sb.append("A1,\"B1\",C1\r\n");
//        sb.append("A2,\"\"\"B2\"\",\r\n\",C2\r\n");
//        sb.append("A3,B3,C3\r\n");
//        sb.append("A4,B4,C4");
//
//        var prd = new CsvParsingTest().file();
//        testSuccess(prd, sb.toString());
//    }
//
//    @Test
//    void test5() throws IOException {
//        var prd = of("0|1");
//        var in = "00110101".chars().mapToObj(Character::toString).iterator();
//        var pser = prd.parser(Tokenizer.wrap(in));
//        var out = pser.iterativeParse()
//                .map(s -> s.asToken().getValue())
//                .peek(System.out::println)
//                .collect(Collectors.joining(","));
//        assertEquals("0,0,1,1,0,1,0,1", out);
//    }

//    @Test
//    void test1_XXX3() {
//        class TestTokenizerFactory implements TokenizerFactory {
//            // Phase1
//            // LINE_TERMINATORS
//            Production LINE_TERMINATOR = oneOf(of("\\n"), of(of("\\r"), of("\\n").opt()));
//            // WHITE_SPACES
//            Production WHITE_SPACE = oneOf("\\x20", "\\t", "\\f", LINE_TERMINATOR);
//            // COMMENTS
//            Production COMMENT = oneOf(of("/", oneOf("/", "\\*")), of("\\*", of("/").opt()));
//            // OPERATORS
//            Production OPERATOR = oneOf(of("=", of("=").opt()), of(">", of("=").opt()), of("<", of("=").opt()));
//            // IDENTIFIERS
//            Production IDENTIFIER = of("\\p{Alpha}", of("\\p{Alnum}").repeat());
//            // INPUT
//            Production INPUT = oneOf(WHITE_SPACE, COMMENT, OPERATOR, IDENTIFIER);// .repeat();
//
//            // Phase2
//            Production END_OF_LINE_COMMENT = of("//", of(".+").repeat());
//            Production TRADITIONAL_COMMNET = of("/\\*", of("(?!\\*/)(?s:.+)").repeat(), "\\*/");
//            Production INPUT2 = oneOf(END_OF_LINE_COMMENT, TRADITIONAL_COMMNET, "(?s:.+)");// .repeat();
//
//            class TestTokenizer implements Tokenizer {
//                private Tokenizer tzer;
//                private Production p;
//                private Token t;
//
//                public TestTokenizer(Tokenizer tzer, Production p) {
//                    this.tzer = tzer;
//                    this.p = p;
//                }
//
//                @Override
//                public boolean hasNext() {
//                    if (t != null) {
//                        return true;
//                    }
//                    return tzer.hasNext();
//                }
//
//                @Override
//                public Token next() {
//                    try {
//                        return peek();
//                    } finally {
//                        t = null;
//                    }
//                }
//
////                @Override
//                public Token peek() {
//                    if (t == null) {
//                        Symbol s = p.parse(tzer.buffer());
//                        t = s.asToken();
//                    }
//                    return t;
//                }
//
//                @Override
//                public void close() {
//                    tzer.close();
//                }
//
//                @Override
//                public String toString() {
//                    return tzer.toString();
//                }
//
//                @Override
//                public Buffer buffer() {
//                    // TODO Auto-generated method stub
//                    return null;
//                }
//            }
//
//            @Override
//            public Tokenizer createTokenizer(Reader r) {
//                TokenizerFactory tf = TokenizerFactory.newInstance();
//                Tokenizer tzer = tf.createTokenizer(r);
//                tzer = new TestTokenizer(tzer, INPUT);
//                tzer = new TestTokenizer(tzer, INPUT2);
//                return tzer;
//            }
//
//        }
//
//        String in = "abc=D12\r\n" + "= == < <= > >=\r" + "//Comment line\n" + "/*\n" + " *Comment block\n" + " */";
//        TokenizerFactory tf = new TestTokenizerFactory();
//        try (Tokenizer tzer = tf.createTokenizer(new StringReader(in))) {
//            tzer.forEachRemaining(t -> System.out.println("|" + t.getValue()));
//        }
//    }
}

