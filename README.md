# Bonsai parser
A parsing library designed for low learning cost and ease of starting to use.

> [!NOTE]
> Documentation in progress

## Installation

## Usage

Part of the following sample code import:
```
import static com.jiganaut.bonsai.grammar.Rules.concat;
import static com.jiganaut.bonsai.grammar.Rules.matching;
import static com.jiganaut.bonsai.grammar.Rules.oneOf;
import static com.jiganaut.bonsai.grammar.Rules.pattern;
import static com.jiganaut.bonsai.grammar.Rules.reference;

import java.io.StringReader;

import com.jiganaut.bonsai.grammar.Grammar;
import com.jiganaut.bonsai.parser.ParseException;
import com.jiganaut.bonsai.parser.TextSource;
```

Sample to get a parse tree:
```
var lexicalGrammar = Grammar.<String>builder()
        .add("WORD", pattern("\\S").oneOrMore())
        .add("WS", pattern("\\s").skip())
        .build();
var syntacticGrammar = Grammar.<String>builder("HELLO")
        .add("HELLO", concat(
                matching("Hello"),
                reference("SOMETHING")))
        .add("SOMETHING", oneOf(
                matching("World"),
                matching("Bonsai")))
        .build();

try (var parser = TextSource.of(new StringReader("Hello World"))
        .addLayer(lexicalGrammar)
        .toParser(syntacticGrammar)) {

    var tree = parser.parse();
    System.out.println(tree);

} catch (ParseException ex) {
    ex.printStackTrace();
}
```

## API Documentation

## License
This project is licensed under the MIT License. See the LICENSE file for details.
