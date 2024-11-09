package com.jiganaut.bonsai.sample.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.jiganaut.bonsai.parser.ParseException;
import com.jiganaut.bonsai.parser.ParserFactory;

public class JsonParserMain {

    public static void main(String[] args) {
        var factory = ParserFactory.load(JsonParsesrFactory.class);

        try (var reader = new BufferedReader(new FileReader("src/main/resources/sample.json"));
                var parser = factory.createParser(reader)) {

            var tree = parser.parse();
            System.out.println(tree);

        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

}
