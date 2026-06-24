public class Interpreter extends EnpitsuBaseVisitor<Object> {

    private final SymbolTable symbolTable = new SymbolTable();

    @Override
    public Object visitProgram(EnpitsuParser.ProgramContext ctx) {
        for (EnpitsuParser.SentenceContext sentence : ctx.sentence()) {
            visit(sentence);
        }
        return null;
    }

    @Override
    public Object visitVar_decl(EnpitsuParser.Var_declContext ctx) {
        String nombre = ctx.ID().getText();
        String tipo = ctx.tipo().getText();
        try {
            symbolTable.declararVariable(nombre, tipo);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public Object visitVar_assign(EnpitsuParser.Var_assignContext ctx) {
        String nombre = ctx.ID().getText();
        Object valor = visit(ctx.expresion());
        try {
            symbolTable.asignarValor(nombre, null, valor);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return valor;
    }

    @Override
    public Object visitPrintln(EnpitsuParser.PrintlnContext ctx) {
        Object valor = visit(ctx.expresion());
        System.out.println(valor);
        return null;
    }

    @Override
    public Object visitCondicionalIf(EnpitsuParser.CondicionalIfContext ctx) {
        Object condition = visit(ctx.expresion());
        if (!(condition instanceof Boolean)) {
            throw new RuntimeException("La condición del 'if' debe ser booleana.");
        }

        if ((Boolean) condition) {
            if (ctx.ifSentences != null) {
                for (EnpitsuParser.SentenceContext s : ctx.ifSentences) {
                    visit(s);
                }
            }
        } else {
            if (ctx.elseSentences != null) {
                for (EnpitsuParser.SentenceContext s : ctx.elseSentences) {
                    visit(s);
                }
            }
        }
        return null;
    }

    @Override
    public Object visitEstructuraDoWhile(EnpitsuParser.EstructuraDoWhileContext ctx) {
        boolean conditionVal;
        do {
            for (EnpitsuParser.SentenceContext sentence : ctx.sentence()) {
                visit(sentence);
            }
            Object condition = visit(ctx.expresion());
            if (!(condition instanceof Boolean)) {
                throw new RuntimeException("La condición del 'do-while' debe ser de tipo boolean.");
            }
            conditionVal = (Boolean) condition;
        } while (conditionVal);

        return null;
    }

    @Override
    public Object visitExpMultDiv(EnpitsuParser.ExpMultDivContext ctx) {
        Object left = visit(ctx.expresion(0));
        Object right = visit(ctx.expresion(1));
        String op = ctx.getChild(1).getText();

        if (left instanceof Integer && right instanceof Integer) {
            int l = (Integer) left;
            int r = (Integer) right;
            if (op.equals("*")) return l * r;
            if (op.equals("/")) {
                if (r == 0) throw new ArithmeticException("División por cero.");
                return l / r;
            }
        } else if (left instanceof Number && right instanceof Number) {
            double l = ((Number) left).doubleValue();
            double r = ((Number) right).doubleValue();
            if (op.equals("*")) return l * r;
            if (op.equals("/")) {
                if (r == 0.0) throw new ArithmeticException("División por cero.");
                return l / r;
            }
        }
        throw new RuntimeException("Tipos incompatibles para el operador: " + op);
    }

    @Override
    public Object visitExpSumaResta(EnpitsuParser.ExpSumaRestaContext ctx) {
        Object left = visit(ctx.expresion(0));
        Object right = visit(ctx.expresion(1));
        String op = ctx.getChild(1).getText();

        if (op.equals("+")) {
            if (left instanceof String || right instanceof String) {
                return String.valueOf(left) + String.valueOf(right);
            }
            if (left instanceof Integer && right instanceof Integer) {
                return (Integer) left + (Integer) right;
            }
            if (left instanceof Number && right instanceof Number) {
                return ((Number) left).doubleValue() + ((Number) right).doubleValue();
            }
        } else if (op.equals("-")) {
            if (left instanceof Integer && right instanceof Integer) {
                return (Integer) left - (Integer) right;
            }
            if (left instanceof Number && right instanceof Number) {
                return ((Number) left).doubleValue() - ((Number) right).doubleValue();
            }
        }
        throw new RuntimeException("Tipos incompatibles para el operador: " + op);
    }

    @Override
    public Object visitExpRelacional(EnpitsuParser.ExpRelacionalContext ctx) {
        Object left = visit(ctx.expresion(0));
        Object right = visit(ctx.expresion(1));
        String op = ctx.getChild(1).getText();

        if (op.equals("== ")) {
            return left == null ? right == null : left.equals(right);
        }
        if (op.equals("!=")) {
            return left == null ? right != null : !left.equals(right);
        }

        if (left instanceof Number && right instanceof Number) {
            double l = ((Number) left).doubleValue();
            double r = ((Number) right).doubleValue();
            switch (op) {
                case "<": return l < r;
                case "<=": return l <= r;
                case ">": return l > r;
                case ">=": return l >= r;
            }
        }
        throw new RuntimeException("Operación relacional no soportada entre los tipos provistos.");
    }

    @Override
    public Object visitExpLogica(EnpitsuParser.ExpLogicaContext ctx) {
        String op = ctx.getChild(1).getText();
        Object left = visit(ctx.expresion(0));

        if (!(left instanceof Boolean)) {
            throw new RuntimeException("Operador lógico requiere operandos booleanos.");
        }

        if (op.equals("&&")) {
            if (!(Boolean) left) return false;
            Object right = visit(ctx.expresion(1));
            if (!(right instanceof Boolean)) {
                throw new RuntimeException("Operador lógico requiere operandos booleanos.");
            }
            return (Boolean) right;

        } else if (op.equals("||")) {
            if ((Boolean) left) return true;
            Object right = visit(ctx.expresion(1));
            if (!(right instanceof Boolean)) {
                throw new RuntimeException("Operador lógico requiere operandos booleanos.");
            }
            return (Boolean) right;
        }
        throw new RuntimeException("Operador lógico desconocido: " + op);
    }

    @Override
    public Object visitExpNot(EnpitsuParser.ExpNotContext ctx) {
        Object val = visit(ctx.expresion());
        if (!(val instanceof Boolean)) {
            throw new RuntimeException("El operador '!' requiere un operando booleano.");
        }
        return !(Boolean) val;
    }

    @Override
    public Object visitExpParentesis(EnpitsuParser.ExpParentesisContext ctx) {
        return visit(ctx.expresion());
    }

    @Override
    public Object visitExpFactor(EnpitsuParser.ExpFactorContext ctx) {
        return visit(ctx.factor());
    }

    @Override
    public Object visitFactor(EnpitsuParser.FactorContext ctx) {
        if (ctx.ID() != null) {
            String nombre = ctx.ID().getText();
            try {
                Object valor = symbolTable.obtenerValor(nombre);
                if (valor == null) {
                    throw new RuntimeException("Error: La variable " + nombre + " no ha sido inicializada.");
                }
                return valor;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        if (ctx.NUM() != null) {
            return Integer.parseInt(ctx.NUM().getText());
        }
        if (ctx.FLOAT_VAL() != null) {
            return Double.parseDouble(ctx.FLOAT_VAL().getText());
        }
        if (ctx.STRING_VAL() != null) {
            String str = ctx.STRING_VAL().getText();
            return str.substring(1, str.length() - 1);
        }
        if (ctx.BOOLEAN_VAL() != null) {
            return Boolean.parseBoolean(ctx.BOOLEAN_VAL().getText());
        }
        return null;
    }
}