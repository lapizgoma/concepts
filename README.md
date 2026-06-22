# enpitsu
Trabajo práctico grupal de la materia Conceptos y Paradigmas de Lenguajes de Programación.

### Integrantes del grupo:
- Lola Torres Rodriguez
- Julián Ignacio Valiño

---

La gramática que presentamos se llama `enpitsu`, que significa lápiz en japonés. La extensión de los archivos `enpitsu` es `.ept`.

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