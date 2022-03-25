package com.unitedjiga.commontest.parsing.sp;

import static com.unitedjiga.common.parsing.Production.of;
import static com.unitedjiga.common.parsing.Production.oneOf;
import static com.unitedjiga.common.parsing.Production.ref;

import java.io.Reader;

import com.unitedjiga.common.parsing.AlternativeProduction;
import com.unitedjiga.common.parsing.Parser;
import com.unitedjiga.common.parsing.Production;
import com.unitedjiga.common.parsing.SequentialProduction;
import com.unitedjiga.common.parsing.Tokenizer;
import com.unitedjiga.common.parsing.TokenizerFactory;

public class JavaTokenizerFactory implements TokenizerFactory {

    private class Layer1 {
        Production preInputElement() {
            return oneOf(
                    lineTerminator(),
                    inputElement(),
                    ".");
        }
        /*
         * Line Terminators
         */
        Production lineTerminator() {
            return oneOf(
                    "\\n",
                    of("\\r", of("\\n").opt()));
        }
        Production inputElement() {
            return oneOf(
                    commentOrSlash(),
                    token());
        }
        /*
         * Comments or Slash
         */
        Production commentOrSlash() {
            return of("/", oneOf(
                    traditionalComment(),
                    endOfLineComment()).opt());
        }
        Production traditionalComment() {
            return of("\\*", commentTail());
        }
        Production commentTail() {
            return oneOf(
                    of("\\*", commentTailStar()),
                    of(notStar(), ref(this::commentTail)));
        }
        Production commentTailStar() {
            return oneOf(
                    "/",
                    of("\\*", ref(this::commentTailStar)),
                    of(notStarNotSlash(), ref(this::commentTail)));
        }
        Production notStar() {
            return oneOf("[^*]", "[\r\n]");
        }
        Production notStarNotSlash() {
            return oneOf("[^*/]", "[\r\n]");
        }
        Production endOfLineComment() {
            return of("/", of("[^\r\n]").repeat());
        }
        /*
         * 
         */
        Production token() {
            return oneOf(literal());
        }
        Production literal() {
            return oneOf(
                    characterLiteral(),
                    stringLiteral());
        }
        /*
         * Character Literals
         */
        Production characterLiteral() {
            return of("'", oneOf(
                    singleCharacter(),
                    escapeSequence()), "'");
        }
        Production singleCharacter() {
            return of("[^'\\\\\r\n]");
        }
        /*
         * String Literals
         */
        Production stringLiteral() {
            return of("\"", stringCharacter().repeat(), "\"");
        }
        Production stringCharacter() {
            return oneOf(
                    "[^\"\\\\\r\n]",
                    escapeSequence());
        }
        /*
         * Escape Sequences for Character and String Literals
         */
        Production escapeSequence() {
            return of("\\\\", oneOf("b", "t", "n", "f", "r", "\"", "'", "\\\\", octalEscape()));
        }
        Production octalEscape() {
            return oneOf(
                    of("[0-3]", of("[0-7]").opt(), of("[0-7]").opt()),
                    of("[0-7]", of("[0-7]").opt()));
        }
    }
    private class Layer2 {
        /*
         * Line Terminators
         */
        Production lineTerminator() {
            return of("\\n|\\r|\\r\\n");
        }
        /*
         * Input Elements
         */
        Production inputElement() {
            return oneOf(
                    whiteSpace(),
//                    comment(),
                    token(),
                    "(?s:.)+");
        }
        /*
         * White Space
         */
        Production whiteSpace() {
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
        Production token() {
            return oneOf(
                    literal(),
                    separator(),
                    operator());
        }
        /*
         * Literals
         */
        Production literal() {
            return oneOf(
                    integerLiteral(),
                    floatingPointLiteral());
        }
        // Integer Literals
        Production integerLiteral() {
            return oneOf(
                    decimalIntegerLiteral(),
                    hexIntegerLiteral(),
                    ocatlIntegerLiteral(),
                    binaryIntegerLiteral());
        }
        Production decimalIntegerLiteral() {
            return of(decimalNumeral(), integerTypeSuffix().opt());
        }
        Production hexIntegerLiteral() {
            return of(hexNumeral(), integerTypeSuffix().opt());
        }
        Production ocatlIntegerLiteral() {
//            return of(octalNumeral(), integerTypeSuffix().opt());
            return null;
        }
        Production binaryIntegerLiteral() {
            return null;
        }
        Production integerTypeSuffix() {
            return of("l|L");
        }
        Production decimalNumeral() {
            return oneOf(
                    "0",
                    of("[1-9]", oneOf(
                            digits().opt(),
                            of(underscores(), digits()))));
        }
        Production digits() {
            return of("[0-9]", of(digitsAndUnderscores().opt(), "[0-9]").opt());
        }
        Production digitsAndUnderscores() {
            return of(digitsOrUnderscore(), digitsOrUnderscore().repeat());
        }
        Production digitsOrUnderscore() {
            return oneOf(
                    "[0-9]",
                    underscores());
        }
        Production underscores() {
            return of("_", of("_").repeat());
        }
        Production hexNumeral() {
            return of("0", "[xX]", hexDigits());
        }
        Production hexDigits() {
            return of("[0-9a-fA-F]", hexDigitsAndUnderscores(), "[0-9a-fA-F]");
        }
        Production hexDigitsAndUnderscores() {
            return of(hexDigitOrUnderscore(), hexDigitOrUnderscore().repeat());
        }
        Production hexDigitOrUnderscore() {
            return oneOf("[0-9a-fA-F]", "_");
        }
        /*
         * Separators
         */
        Production separator() {
            return oneOf(
                  of("\\.", of("\\.", "\\.").opt()),
                  of(":", of(":").opt()));
        }
        /*
         * Operators
         */
        Production operator() {
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
    Production identifier() {
        return identifierChars();//but not a Keyword or BooleanLiteral or NullLiteral
    }
    Production identifierChars() {
        return of(javaLetter(), javaLetterOrDigit().repeat());
    }
    Production javaLetter() {
        return of("[A-Za-z$_]");//any Unicode character that is a "Java letter"
    }
    Production javaLetterOrDigit() {
        return oneOf("[0-9]", javaLetter());//any Unicode character that is a "Java letter-or-digit"
    }
    /*
     * Keywords
     */
    Production keyword() {
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
    Production floatingPointLiteral() {
        return null;
    }
    Production decimalFloatingPointLiteral() {
        return null;
    }
    Production hexadecimalFloatingPointLiteral() {
        return null;
    }
    // Boolean Literals
    Production booleanLiteral() {
        return oneOf("true", "false");
    }
    // Character Literals
    Production characterLiteral() {
        return null;
    }
    // String Literals
    Production stringLiteral() {
        return null;
    }
    // The Null Literal
    Production nullLiteral() {
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
        Production layer1 = new Layer1().preInputElement();
        Production layer2 = new Layer2().inputElement();
        return TokenizerFactory.newFactory(layer1, layer2).createTokenizer(r);
    }

}
