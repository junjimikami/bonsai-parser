import com.jiganaut.bonsai.parser.ParserFactory;
import com.jiganaut.bonsai.sample.json.JsonParsesrFactory;

module com.jiganaut.bonsai.sample.json {
    requires com.jiganaut.bonsai;

    provides ParserFactory with JsonParsesrFactory;
}