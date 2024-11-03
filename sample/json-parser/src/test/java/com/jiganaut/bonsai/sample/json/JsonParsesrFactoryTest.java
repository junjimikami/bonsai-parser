package com.jiganaut.bonsai.sample.json;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

import com.jiganaut.bonsai.parser.ParserFactory;

class JsonParsesrFactoryTest {

    @Test
    void test() throws Exception {
        var input = """
                {
                  "stringExample": "This is a string",
                  "escapedStringExample": "This is an escaped string: \\"Hello, World!\\"",
                  "integerExample": 123,
                  "floatExample": 123.45,
                  "exponentialExample": 1.23e4,
                  "negativeNumberExample": -789,
                  "booleanTrueExample": true,
                  "booleanFalseExample": false,
                  "nullExample": null,
                  "arrayExample": ["String 1", "String 2", "String 3"],
                  "objectExample": {
                    "nestedString": "Nested string",
                    "nestedInteger": 456
                  }
                }""";
        var factory = ParserFactory.load(JsonParsesrFactory.class);

        try (var reader = new StringReader(input);
                var parser = factory.createParser(reader)) {

            var tree = parser.parse();
            assertEquals("json", tree.getName());
            assertEquals("element", tree.getSubTrees().get(0).getName());
            assertEquals("value", tree.getSubTrees().get(0)
                    .getSubTrees().get(0).getName());
            assertEquals("object", tree.getSubTrees().get(0)
                    .getSubTrees().get(0)
                    .getSubTrees().get(0).getName());
            System.out.println(tree);
        }

    }
}
