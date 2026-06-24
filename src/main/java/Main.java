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
        System.out.println("== ARBOL =========================================");
        System.out.println("");
        System.out.println(tree.toStringTree(parser));;
        System.out.println("");
        System.out.println("==================================================");
        System.out.println("");

        System.out.println("== ANALISIS SEMANTICO ============================");
        System.out.println("");
        SemanticAnalyzer sa = new SemanticAnalyzer();
        try {
            sa.visit(tree);
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            System.exit(1);
        }

        if (sa.hayErrores()) {
        	System.out.println("Hubo errores en el analisis semantico: ");
        	for (String error : sa.errores()) {
                System.out.println(error);
        	}
        }
        else {
        	System.out.println ("No hubo errores en el analisis semantico.");
        }
        System.out.println("");
        System.out.println("==================================================");
        System.out.println("");

        System.out.println("== INTERPRETER ===================================");
        System.out.println("");
        try {
            new Interpreter().visit(tree);
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("");
        System.out.println("===================================================");
    }
}
