package com.unitedjiga.commontest.parsing.sp;

import static com.unitedjiga.common.parsing.Expression.of;
import static com.unitedjiga.common.parsing.Expression.oneOf;
import static com.unitedjiga.common.parsing.Expression.ref;

import java.io.Reader;

import com.unitedjiga.common.parsing.AlternativeProduction;
import com.unitedjiga.common.parsing.Parser;
import com.unitedjiga.common.parsing.Expression;
import com.unitedjiga.common.parsing.SequentialProduction;
import com.unitedjiga.common.parsing.Tokenizer;
import com.unitedjiga.common.parsing.Tokenizer.Builder;
import com.unitedjiga.common.parsing.TokenizerFactory;

public class JavaTokenizerFactory implements TokenizerFactory {

    private class Layer1 {
        Expression preInputElement() {
            return oneOf(
                    lineTerminator(),
                    inputElement(),
                    ".");
        }
        /*
         * Line Terminators
         */
        Expression lineTerminator() {
            return oneOf(
                    "\\n",
                    of("\\r", of("\\n").opt()));
        }
        Expression inputElement() {
            return oneOf(
                    commentOrSlash(),
                    token());
        }
        /*
         * Comments or Slash
         */
        Expression commentOrSlash() {
            return of("/", oneOf(
                    traditionalComment(),
                    endOfLineComment()).opt());
        }
        Expression traditionalComment() {
            return of("\\*", commentTail());
        }
        Expression commentTail() {
            return oneOf(
                    of("\\*", commentTailStar()),
                    of(notStar(), ref(this::commentTail)));
        }
        Expression commentTailStar() {
            return oneOf(
                    "/",
                    of("\\*", ref(this::commentTailStar)),
                    of(notStarNotSlash(), ref(this::commentTail)));
        }
        Expression notStar() {
            return oneOf("[^*]", "[\r\n]");
        }
        Expression notStarNotSlash() {
            return oneOf("[^*/]", "[\r\n]");
        }
        Expression endOfLineComment() {
            return of("/", of("[^\r\n]").repeat());
        }
        /*
         * 
         */
        Expression token() {
            return oneOf(literal());
        }
        Expression literal() {
            return oneOf(
                    characterLiteral(),
                    stringLiteral());
        }
        /*
         * Character Literals
         */
        Expression characterLiteral() {
            return of("'", oneOf(
                    singleCharacter(),
                    escapeSequence()), "'");
        }
        Expression singleCharacter() {
            return of("[^'\\\\\r\n]");
        }
        /*
         * String Literals
         */
        Expression stringLiteral() {
            return of("\"", stringCharacter().repeat(), "\"");
        }
        Expression stringCharacter() {
            return oneOf(
                    "[^\"\\\\\r\n]",
                    escapeSequence());
        }
        /*
         * Escape Sequences for Character and String Literals
         */
        Expression escapeSequence() {
            return of("\\\\", oneOf("b", "t", "n", "f", "r", "\"", "'", "\\\\", octalEscape()));
        }
        Expression octalEscape() {
            return oneOf(
                    of("[0-3]", of("[0-7]").opt(), of("[0-7]").opt()),
                    of("[0-7]", of("[0-7]").opt()));
        }
    }
    private class Layer2 {
        /*
         * Line Terminators
         */
        Expression lineTerminator() {
            return of("\\n|\\r|\\r\\n");
        }
        /*
         * Input Elements
         */
        Expression inputElement() {
            return oneOf(
                    whiteSpace(),
//                    comment(),
                    token(),
                    "(?s:.)+");
        }
        /*
         * White Space
         */
        Expression whiteSpace() {
            return oneOf("[ ]|\\t|\\f", lineTerminator());
        }
//        /*
//         * Comments
//         */
//        Production comment() {
//            return oneOf(traditionalComment(), endOfLineComment());
//        }
//        Production traditionalComment() {
//            return of("/\\*(?s:.)*");
//        }
//        Production commentTail() {
//            return oneOf(
//                    of("\\*", commentTailStar()),
//                    of(notStar(), ref(this::commentTail)));
//        }
//        Production commentTailStar() {
//            return oneOf(
//                    "/",
//                    of("\\*", ref(this::commentTailStar)),
//                    of(notStarNotSlash(), ref(this::commentTail)));
//        }
//        Production notStar() {
//            return oneOf("[^*]", lineTerminator());
//        }
//        Production notStarNotSlash() {
//            return oneOf("[^*/]", lineTerminator());
//        }
//        Production endOfLineComment() {
//            return of("//.*");
//        }
        /*
         * 
         */
        Expression token() {
            return oneOf(
                    literal(),
                    separator(),
                    operator());
        }
        /*
         * Literals
         */
        Expression literal() {
            return oneOf(
                    integerLiteral(),
                    floatingPointLiteral());
        }
        // Integer Literals
        Expression integerLiteral() {
            return oneOf(
                    decimalIntegerLiteral(),
                    hexIntegerLiteral(),
                    ocatlIntegerLiteral(),
                    binaryIntegerLiteral());
        }
        Expression decimalIntegerLiteral() {
            return of(decimalNumeral(), integerTypeSuffix().opt());
        }
        Expression hexIntegerLiteral() {
            return of(hexNumeral(), integerTypeSuffix().opt());
        }
        Expression ocatlIntegerLiteral() {
//            return of(octalNumeral(), integerTypeSuffix().opt());
            return null;
        }
        Expression binaryIntegerLiteral() {
            return null;
        }
        Expression integerTypeSuffix() {
            return of("l|L");
        }
        Expression decimalNumeral() {
            return oneOf(
                    "0",
                    of("[1-9]", oneOf(
                            digits().opt(),
                            of(underscores(), digits()))));
        }
        Expression digits() {
            return of("[0-9]", of(digitsAndUnderscores().opt(), "[0-9]").opt());
        }
        Expression digitsAndUnderscores() {
            return of(digitsOrUnderscore(), digitsOrUnderscore().repeat());
        }
        Expression digitsOrUnderscore() {
            return oneOf(
                    "[0-9]",
                    underscores());
        }
        Expression underscores() {
            return of("_", of("_").repeat());
        }
        Expression hexNumeral() {
            return of("0", "[xX]", hexDigits());
        }
        Expression hexDigits() {
            return of("[0-9a-fA-F]", hexDigitsAndUnderscores(), "[0-9a-fA-F]");
        }
        Expression hexDigitsAndUnderscores() {
            return of(hexDigitOrUnderscore(), hexDigitOrUnderscore().repeat());
        }
        Expression hexDigitOrUnderscore() {
            return oneOf("[0-9a-fA-F]", "_");
        }
        /*
         * Separators
         */
        Expression separator() {
            return oneOf(
                  of("\\.", of("\\.", "\\.").opt()),
                  of(":", of(":").opt()));
        }
        /*
         * Operators
         */
        Expression operator() {
            return oneOf(
                  of("-", oneOf(
                          "-",
                          "=",
                          ">").opt()),
                  of("!", of("=").opt()),
                  of("%", of("=").opt()),
                  of("&", oneOf(
                          "&",
                          "=").opt()),
                  of("\\*", of("=").opt()),
                  of("/", of("=").opt()),
                  of("\\^", of("=").opt()),
                  of("\\|", oneOf(
                          "\\|",
                          "=").opt()),
                  of("\\+", oneOf(
                          "\\+",
                          "=").opt()),
                  of("<", of("<").opt(), of("=").opt()),
                  of("=", of("=").opt()),
                  of(">", of(">").opt(), of(">").opt(), of("=").opt()));
        }
        /*
         * 
         */
//        Production preToken() {
//            return oneOf(
////                    of("'", "[^']+", "'"),
////                    of("\"", "[^\"]+", "\""),
//                    of("\\.", of("\\.", "\\.").opt()),
//                    of(":", of(":").opt()),
//                    of("-", oneOf(
//                            "-",
//                            "=",
//                            ">").opt()),
//                    of("!", of("=").opt()),
//                    of("%", of("=").opt()),
//                    of("&", oneOf(
//                            "&",
//                            "=").opt()),
//                    of("\\*", of("=").opt()),
//                    of("/", of("=").opt()),
//                    of("\\^", of("=").opt()),
//                    of("\\|", oneOf(
//                            "\\|",
//                            "=").opt()),
//                    of("\\+", oneOf(
//                            "\\+",
//                            "=").opt()),
//                    of("<", of("<").opt(), of("=").opt()),
//                    of("=", of("=").opt()),
//                    of(">", of(">").opt(), of(">").opt(), of("=").opt()),
//                    of(".").repeat()
//                    );
//        }
    }

//    /*
//     * 
//     */
//    Production preLineTerminator = of("\\r", of("\\n").opt());
//    Production preSeparator1 = of(".", of(".", ".").opt());
//    Production preSeparator2 = of(":", of(":").opt());
//    Production preIdentifier = of();
//    Production preJavaLetter = of("[A-Za-z$_]");
//    Production preJavaLetterOrDigit = of("[0-9]");

//    /*
//     * Line Terminators
//     */
//    Production lineTerminator() {
//        return oneOf("\\n|\\r|\\r\\n");
//    }
//    /*
//     * Input Elements and Tokens
//     */
//    Production input() {
////        return inputElements().repeat();
//        return inputElements();
//    }
//    Production inputElements() {
//        return oneOf(whiteSpace(), token());
//    }
//    Production token() {
//        return oneOf(identifier(), keyword(), literal(), separator(), operator());
//    }
//    /*
//     * White Space
//     */
//    Production whiteSpace() {
//        return oneOf("[ ]|\\t|\\f", lineTerminator());
//    }
    /*
     * Identifiers
     */
    Expression identifier() {
        return identifierChars();//but not a Keyword or BooleanLiteral or NullLiteral
    }
    Expression identifierChars() {
        return of(javaLetter(), javaLetterOrDigit().repeat());
    }
    Expression javaLetter() {
        return of("[A-Za-z$_]");//any Unicode character that is a "Java letter"
    }
    Expression javaLetterOrDigit() {
        return oneOf("[0-9]", javaLetter());//any Unicode character that is a "Java letter-or-digit"
    }
    /*
     * Keywords
     */
    Expression keyword() {
        return oneOf("abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
                "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "for",
                "if", "goto", "implements", "import", "instanceof", "int", "interface", "long", "native", "new",
                "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch",
                "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while", "_");
    }
//    /*
//     * Literals
//     */
//    Production literal() {
//        return oneOf(integerLiteral(), floatingPointLiteral(), booleanLiteral(), characterLiteral(), stringLiteral(), nullLiteral());
//    }
//    // Integer Literals
//    Production integerLiteral() {
//        return oneOf(decimalIntegerLiteral(), hexIntegerLiteral(), ocatlIntegerLiteral(), binaryIntegerLiteral());
//    }
//    Production decimalIntegerLiteral() {
//        return null;
//    }
//    Production hexIntegerLiteral() {
//        return null;
//    }
//    Production ocatlIntegerLiteral() {
//        return null;
//    }
//    Production binaryIntegerLiteral() {
//        return null;
//    }
//    Production integerTypeSuffix() {
//        return of("l|L");
//    }
    // Floating-Point Literals
    Expression floatingPointLiteral() {
        return null;
    }
    Expression decimalFloatingPointLiteral() {
        return null;
    }
    Expression hexadecimalFloatingPointLiteral() {
        return null;
    }
    // Boolean Literals
    Expression booleanLiteral() {
        return oneOf("true", "false");
    }
    // Character Literals
    Expression characterLiteral() {
        return null;
    }
    // String Literals
    Expression stringLiteral() {
        return null;
    }
    // The Null Literal
    Expression nullLiteral() {
        return of("null");
    }
//    /*
//     * Separators
//     */
//    Production separator() {
//        return oneOf("(", ")", "{", "}", "[", "]", ";", ",", ".", "...", "@", "::");
//    }
//    /*
//     * Operators
//     */
//    Production operator() {
//        return oneOf("=", ">", "<", "!", "~", "?", ":", "->", "==", ">=", "<=", "!=", "&&", "||", "++", "--", "+", "-",
//                "*", "/", "&", "|", "^", "%", "<<", ">>", ">>>", "+=", "-=", "*=", "/=", "&=", "|=", "^=", "%=", "<<=",
//                ">>=", ">>>=");
//    }

    @Override
    public Tokenizer createTokenizer(Reader r) {
        Expression layer1 = new Layer1().preInputElement();
        Expression layer2 = new Layer2().inputElement();
        return TokenizerFactory.newFactory(layer1, layer2).createTokenizer(r);
    }
    @Override
    public Builder createTokenizerBuilder() {
        // TODO Auto-generated method stub
        return null;
    }

}
