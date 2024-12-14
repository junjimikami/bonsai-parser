# Bonsai parser (under development)
A parsing library designed for low learning cost and ease of starting to use.


## Usage

Excerpt from the import section:
```
import static com.jiganaut.bonsai.grammar.Rules.concat;
import static com.jiganaut.bonsai.grammar.Rules.oneOf;
import static com.jiganaut.bonsai.grammar.Rules.pattern;
import static com.jiganaut.bonsai.grammar.Rules.reference;

import com.jiganaut.bonsai.grammar.ChoiceGrammar;
import com.jiganaut.bonsai.grammar.SingleOriginGrammar;
import com.jiganaut.bonsai.parser.Parser;
import com.jiganaut.bonsai.parser.Tokenizer;
```

Sample to get a parse tree:
```
var lexicalGrammar = ChoiceGrammar.builder()
        .add("WORD", pattern("\\S").oneOrMore())
        .add("WS", pattern("\\s").skip())
        .build();
var syntacticGrammar = SingleOriginGrammar.builder()
        .add("HELLO", concat(
                pattern("Hello"),
                reference("SOMETHING")))
        .add("SOMETHING", oneOf(
                pattern("World"),
                pattern("Bonsai")))
        .build();

try (var reader = new StringReader("Hello World");
        var tokenizer = Tokenizer.of(lexicalGrammar, reader);
        var parser = Parser.of(syntacticGrammar, tokenizer)) {

    var tree = parser.parse();
    System.out.println(tree);

} catch (IOException ex) {
    ex.printStackTrace();
}
```
