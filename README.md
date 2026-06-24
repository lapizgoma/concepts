# enpitsu - Intérprete ANTLR
Trabajo práctico grupal de la materia Conceptos y Paradigmas de Lenguajes de Programación.

### Integrantes del grupo:
- Lola Torres Rodriguez
- Julián Ignacio Valiño

---

**Variante asignada:**
- Variante 3: Ciclo `do-while` (Iteración con condición al final).

---

La gramática que presentamos se llama `enpitsu`, que significa lápiz en japonés. La extensión de los archivos `enpitsu` es `.ept`.
Es un lenguaje de programación imperativo simple y de tipado estático.
Soporta tipos de datos básicos (enteros, flotantes, cadenas y booleanos), declaración y asignación de variables, y expresiones aritméticas, relacionales y lógicas.

---
**Decisiones de diseño tomadas:**
1. **Separación de responsabilidades:** Se decidió no inyectar código Java en el archivo `.g4` para mantener la gramática pura. Toda la lógica se delegó a clases Java usando Patrón Visitor autogenerado por ANTLR.
2. **Tabla de Símbolos unificada:** Se implementó un único mapa en `SymbolTable.java` que asocia cada variable a una clase que contiene tanto su *tipo* como su *valor actual*.
3. **Fases de ejecución aisladas:** El Análisis Semántico (`SemanticAnalyzer.java`) y el Intérprete (`Interpreter.java`) realizan recorridos independientes sobre el árbol. Garantiza que el programa se detenga ante errores semánticos antes de iniciar la ejecución real.

---
### Setup del proyecto:

Secuencia de comandos:

```
git clone https://github.com/lapizgoma/concepts.git
cd concepts

mvn clean
mvn generate-sources
mvn compile
```

Luego, en Eclipse:

```
(click derecho en proyecto) Propreties -> Java Build Path -> Add folder
```

y añadir `src/main/` y `target/generated-sources/antlr4`.


Para ejecutar en programa, use:
```
# en CMD:
mvn compile exec:java -Dexec.args="ejemplos/<archivo>.ept"

# en PowerShell:
mvn compile exec:java '-Dexec.args=ejemplos/<archivo>.ept'
```

Ejemplo en PowerShell:
```
mvn exec:java '-Dexec.args=ejemplos/hello_world.ept'
```

---

Ejemplo de uso:

```java
var contador : int;
contador = 0;

do {
    println(contador);
    contador = contador + 1;
} while (contador < 5);

```
***
