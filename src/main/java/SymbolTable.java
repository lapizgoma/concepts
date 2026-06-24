import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    private Map<String, Symbol> tablaSimbolos;

    public SymbolTable() {
        this.tablaSimbolos = new HashMap<>();
    }

    public void declararVariable(String nombre, String tipo) throws Exception {
        if(tablaSimbolos.containsKey(nombre)) {
            throw new Exception("Error semántico: La variable "+ nombre +
                    " ya fue declarada.");
        }
        if (tipo == null) {
            throw new Exception("Error semántico: El tipo no puede ser null.");
        }
        tablaSimbolos.put(nombre, new Symbol(null, tipo));
    }

    public void asignarValor(String nombre, String tipo, Object valor) throws Exception{
        if(!tablaSimbolos.containsKey(nombre)) {
            throw new Exception("Error semántico: La variable "+ nombre +
                    " no fue declarada.");
        }
        Symbol symbol = tablaSimbolos.get(nombre);
        if (tipo != null) {
            symbol.type = tipo;
        }
        symbol.value = valor;
    }

    public Object obtenerValor(String nombre) throws Exception{
        if(!tablaSimbolos.containsKey(nombre)) {
            throw new Exception("Error semántico: la variable "+ nombre +
                    " no fue declarada.");
        }
        return tablaSimbolos.get(nombre).value;
    }

    public String obtenerTipo(String nombre) throws Exception{
        if(!tablaSimbolos.containsKey(nombre)) {
            throw new Exception("Error semántico: la variable "+ nombre +
                    " no fue declarada.");
        }
        return tablaSimbolos.get(nombre).type;
    }

    public static class Symbol {
        public Object value;
        public String type;

        public Symbol(Object value, String type) {
            if (type == null) {
                throw new IllegalArgumentException("ERROR: El símbolo no puede no tener un tipo.");
            }
            this.value = value;
            this.type = type;
        }
    }
}


