import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

	private Map<String, Object> tabla;
	
	public SymbolTable() {
		this.tabla = new HashMap<>();
	}
	
	
	public void declararVariable(String nombre) throws Exception {
		if(tabla.containsKey(nombre)) {
			throw new Exception("Error semántico: La variable "+ nombre + 
					" ya fue declarada.");
		}
		tabla.put(nombre, null);
	}
	
	public void asignarValor(String nombre, Object valor) throws Exception{
		if(!tabla.containsKey(nombre)) {
			throw new Exception("Error semántico: La variable "+ nombre +
					" no fue declarada.");
		}
		tabla.put(nombre, valor);
	}
	
	public Object obtenerValor(String nombre) throws Exception{
		if(!tabla.containsKey(nombre)) {
			throw new Exception("Error semántico: la variable "+ nombre + 
					" no fue declarada.");
		}
		return tabla.get(nombre);
	}
	
}

