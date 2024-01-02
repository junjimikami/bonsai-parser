package com.unitedjiga.commontest.parsing;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

import com.unitedjiga.common.parsing.ParserFactory;
import com.unitedjiga.common.parsing.ProductionFactory;
import com.unitedjiga.common.parsing.Tree;

class ParserFactoryTest {

    @Test
    void test01() {
        var prdFactory = ProductionFactory.newFactory();
        var prd = prdFactory.createPattern("[0-9]");
        var pserFactory = ParserFactory.newFactory(prd);
        assertNotNull(pserFactory);
        var pser = pserFactory.createParser(new StringReader("7123"));
        assertNotNull(pser);
        var s = pser.parse();
        assertNotNull(s);
        assertInstanceOf(Tree.class, s);
    }

    @Test
    void test02() {
        var prdFactory = ProductionFactory.newFactory();
        var prd = prdFactory.createAlternativeBuilder()
                .add(prdFactory.createPattern("0"))
                .add(prdFactory.createPattern("1"))
                .build();
        var pserFactory = ParserFactory.newFactory(prd);
        var pser = pserFactory.createParser(new StringReader("0"));
        var s = pser.parse();
        assertNotNull(s);
        assertInstanceOf(Tree.class, s);
    }

    @Test
    void test03() {
        var prdFactory = ProductionFactory.newFactory();
        var prd = prdFactory.createSequentialBuilder()
                .add(prdFactory.createPattern("0"))
                .add(prdFactory.createPattern("1"))
                .build();
        var pserFactory = ParserFactory.newFactory(prd);
        var pser = pserFactory.createParser(new StringReader("01"));
        var s = pser.parse();
        assertNotNull(s);
        assertInstanceOf(Tree.class, s);
    }

}
