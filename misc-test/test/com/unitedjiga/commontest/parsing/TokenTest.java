package com.unitedjiga.commontest.parsing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.unitedjiga.common.parsing.Token;

class TokenTest {

    @Test
    void test() {
        Token t1 = Token.of("123");
        Token t2 = Token.of("abc");
        Token t3 = Token.concat(t1, t2);
        Token t4 = Token.concat(t2, t2);

        assertEquals("123", t1.getValue());
        assertEquals("abc", t2.getValue());
        assertEquals("123abc", t3.getValue());
        assertEquals("abcabc", t4.getValue());
    }

    @Test
    void testNull() {
        Token t1 = Token.of(null);
        Token t2 = Token.of("123");
        Token t3 = Token.concat(t1, t1);
        Token t4 = Token.concat(t1, t2);

        assertEquals("null", t1.getValue());
        assertEquals("nullnull", t3.getValue());
        assertEquals("null123", t4.getValue());
    }
}
