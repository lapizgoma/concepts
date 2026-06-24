import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer extends EnpitsuBaseVisitor<String> {

    private SymbolTable tablaSimbolos;
    private List<String> errores;

    public SemanticAnalyzer() {
        tablaSimbolos = new SymbolTable();
        errores = new ArrayList<>();
    }

    public boolean hayErrores() {
        return !errores.isEmpty();
    }
    
    public List<String> errores() {
    	return errores;
    }

    private void error(org.antlr.v4.runtime.ParserRuleContext ctx, String mensaje) {
        int linea = ctx.getStart().getLine();
        int columna = ctx.getStart().getCharPositionInLine();
        errores.add(String.format("ERROR Semántico en [%d:%d]: %s", linea, columna, mensaje));
    }
    
    @Override
    public String visitEstructuraDoWhile (EnpitsuParser.EstructuraDoWhileContext ctx) {
    	String condicion = visit(ctx.expresion());
    	
    	if (!"boolean".equals(condicion)) {
    		error (ctx.expresion(), "se espera una valor de tipo booleano para la condición del bucle.");
    	}

    	for (EnpitsuParser.SentenceContext s : ctx.sentence()) {
            visit(s);
        }
    	
    	return null;
    }
    
    @Override
    public String visitCondicionalIf(EnpitsuParser.CondicionalIfContext ctx) {
        String condicion = visit(ctx.expresion());
        
        if (condicion != null && !"boolean".equals(condicion)) {
            error(ctx.expresion(), "la condición del 'if' debe ser de tipo 'boolean'. Se obtuvo '" + condicion + "'.");
        }

        if (ctx.ifSentences != null) {
            for (EnpitsuParser.SentenceContext s : ctx.ifSentences) {
                visit(s);
            }
        }

        if (ctx.elseSentences != null) {
            for (EnpitsuParser.SentenceContext s : ctx.elseSentences) {
                visit(s);
            }
        }

        return null;
    }

    @Override
    public String visitVar_decl(EnpitsuParser.Var_declContext ctx) {
        String nombreVar = ctx.ID().getText();
        String tipo = ctx.tipo().getText();

        try {
            tablaSimbolos.declararVariable(nombreVar, tipo);
        } catch (Exception e) {
            error(ctx, e.getMessage());
        }

        return null;
    }

    @Override
    public String visitVar_assign(EnpitsuParser.Var_assignContext ctx) {
        String nombreVar = ctx.ID().getText();

        if (ctx.expresion() == null) {
            error(ctx, "expresión de asignación inválida para la variable '" + nombreVar + "'.");
            return null;
        }
        
        String tipoExpresion = visit(ctx.expresion());

        try {
            String tipoVar = tablaSimbolos.obtenerTipo(nombreVar);
            if (tipoExpresion != null && !sonCompatibles(tipoVar, tipoExpresion)) {
                error(ctx, "no se puede asignar '" + tipoExpresion + "' a la variable '" + nombreVar + "' de tipo '" + tipoVar + "'.");
            }
        } catch (Exception e) {
            error(ctx, e.getMessage());
        }

        return null;
    }

    @Override
    public String visitFactor(EnpitsuParser.FactorContext ctx) {
        if (ctx.ID() != null) {
            String nombreVar = ctx.ID().getText();
            try {
                return tablaSimbolos.obtenerTipo(nombreVar);
            } catch (Exception e) {
                error(ctx, e.getMessage());
                return null;
            }
        }
        if (ctx.NUM_VAL() != null) return "int";
        if (ctx.FLOAT_VAL() != null) return "float";
        if (ctx.STRING_VAL() != null) return "string";
        if (ctx.BOOLEAN_VAL() != null) return "boolean";

        return null;
    }

    @Override
    public String visitExpMultDiv(EnpitsuParser.ExpMultDivContext ctx) {
        String izq = visit(ctx.expresion(0));
        String der = visit(ctx.expresion(1));

        if (ctx.DIV() != null) {
            String textoDer = ctx.expresion(1).getText();
            if (textoDer.equals("0") || textoDer.equals("0.0")) {
                error(ctx, "división por cero.");
                return null;
            }
        }

        if (!esNumerico(izq) || !esNumerico(der)) {
            error(ctx, "operación aritmética requiere tipos numéricos, se obtuvo '" + izq + "' y '" + der + "'.");
            return null;
        }

        return (izq.equals("float") || der.equals("float")) ? "float" : "int";
    }

    @Override
    public String visitExpSumaResta(EnpitsuParser.ExpSumaRestaContext ctx) {
        String izq = visit(ctx.expresion(0));
        String der = visit(ctx.expresion(1));

        if (!esNumerico(izq) || !esNumerico(der)) {
            error(ctx, "operación aritmética requiere tipos numéricos, se obtuvo '" + izq + "' y '" + der + "'.");
            return null;
        }

        return (izq.equals("float") || der.equals("float")) ? "float" : "int";
    }

    @Override
    public String visitExpRelacional(EnpitsuParser.ExpRelacionalContext ctx) {
        String izq = visit(ctx.expresion(0));
        String der = visit(ctx.expresion(1));

        if (!sonCompatibles(izq, der)) {
            error(ctx, "comparación entre tipos incompatibles '" + izq + "' y '" + der + "'.");
        }

        return "boolean";
    }

    @Override
    public String visitExpLogica(EnpitsuParser.ExpLogicaContext ctx) {
        String izq = visit(ctx.expresion(0));
        String der = visit(ctx.expresion(1));

        if (!"boolean".equals(izq) || !"boolean".equals(der)) {
            error(ctx, "operador lógico requiere booleanos, se obtuvo '" + izq + "' y '" + der + "'.");
        }

        return "boolean";
    }

    @Override
    public String visitExpNot(EnpitsuParser.ExpNotContext ctx) {
        String tipo = visit(ctx.expresion());
        if (!"boolean".equals(tipo)) {
            error(ctx, "'!' requiere un booleano, se obtuvo '" + tipo + "'.");
        }
        return "boolean";
    }

    @Override
    public String visitExpParentesis(EnpitsuParser.ExpParentesisContext ctx) {
        return visit(ctx.expresion());
    }

    private boolean esNumerico(String tipo) {
        return "int".equals(tipo) || "float".equals(tipo);
    }

    private boolean sonCompatibles(String t1, String t2) {
        if (t1 == null || t2 == null) return true;
        if (t1.equals(t2)) return true;
        if (esNumerico(t1) && esNumerico(t2)) return true;
        return false;
    }
}