
public class SemanticAnalyzer extends EnpitsuBaseVisitor<Object> {
	//enpitsubasevisitor-> clase creada x antlr
	
	private SymbolTable tablaSimbolos;
	
	public SemanticAnalyzer() {
		this.tablaSimbolos = new SymbolTable();
	}
	
	@Override
	public Object visitVar_decl(EnpitsuParser.Var_declContext ctx) {
		String nombreVar = ctx.ID().getText();
		
		try {
			tablaSimbolos.declararVariable(nombreVar);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public Object visitVar_assign(EnpitsuParser.Var_assignContext ctx) {
		String nombreVar = ctx.ID().getText();
		
		try {
			tablaSimbolos.asignarValor(nombreVar, null);
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return visit(ctx.expresion());
	}

	@Override
	public Object visitFactor(EnpitsuParser.FactorContext ctx) {
		if(ctx.ID() != null) {
			String nombreVar = ctx.ID().getText();
		
			try {
				tablaSimbolos.obtenerValor(nombreVar);
			}catch(Exception e) {
				e.printStackTrace();
			}
		
		}
	  return null;
	}
}
