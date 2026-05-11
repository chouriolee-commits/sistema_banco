# sistema_banco
Lee Chourio

Proyecto sistema bancario

## Implementacion FIFO

En este proyecto la politica FIFO (First In, First Out) se implemento en la clase `controlador/ColaBanco.java` usando una `Queue<Cliente>` respaldada por una `LinkedList`..

Cuando en `vista/Main.java` se agrega un cliente, el metodo `encolar()` lo coloca al final de la cola. Luego, cuando se selecciona atender cliente, el metodo `atenderSiguiente()` usa `poll()` para sacar al primer cliente que entro. De esta manera, el sistema atiende a los clientes en el mismo orden en que llegaron..

# Ejecución

- La interfaz Swing se inicia directamente desde `vista.Main`.
- También se generó un jar ejecutable en `vista/BancoSwing.jar`.

Para ejecutar desde la línea de comandos:

```bash
java -jar vista/BancoSwing.jar
```

Para usar el proyecto desde el código fuente:

```bash
javac -cp lib/gson-2.14.0.jar -d target/classes src/java/controlador/*.java src/java/modelo/*.java src/java/vista/*.java
java -cp target/classes:lib/gson-2.14.0.jar vista.Main
```

# Imagen de compilación:

![alt text](image.png)

# link de diagrama UML

https://mermaid.ai/d/53e03623-1a52-40c5-a10e-0bed6bc5c52c

