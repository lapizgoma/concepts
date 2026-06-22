import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.FileInputStream;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Ejecute con: java Main <nombre-del-archivo>");
            System.exit(1);
        }

        String inputFile = args[0];
        if (!inputFile.endsWith(".ept")) {
            System.err.println("El archivo de entrada debe tener la extensión '.ept'");
            System.exit(1);
        }

        InputStream is = new FileInputStream(inputFile);
        CharStream input = CharStreams.fromStream(is);

        EnpitsuLexer lexer = new EnpitsuLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        EnpitsuParser parser = new EnpitsuParser(tokens);

        ParseTree tree = parser.program();
        System.out.println("== ARBOL ============================");
        System.out.println("");
        System.out.println(tree.toStringTree(parser));;
        System.out.println("");
        System.out.println("=====================================");
    }
}
