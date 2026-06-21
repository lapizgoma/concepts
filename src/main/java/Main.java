import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromStream(System.in);

        EnpitsuLexer lexer = new EnpitsuLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        EnpitsuParser parser = new EnpitsuParser(tokens);

        ParseTree tree = parser.program();

        System.out.println(tree.toStringTree(parser));
    }
}

