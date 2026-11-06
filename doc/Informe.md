<div style="height: 90vh; display: flex; flex-direction: column; justify-content: space-between; text-align: center;"> 
	<div> 
		<h1>Universidad Nacional General Sarmiento</h1> 
		<h3>Licenciatura en Sistemas</h3> 
	</div> 
	<div> 
		<h1 style="font-size: 2.2rem;">Conjunto dominante mínimo</h1> 
		<p style="color: #666;">Trabajo Práctico 3</p> 
	</div>
	<div style="text-align: right;"> 
		<p> <strong>Integrantes:</strong> Bruno Lisanti, Mateo Lauzan, Martín Godoy, Santino Tettamanti </p>
		<p><strong>Materia:</strong> Programación III </p> 
		<p><strong>Docentes:</strong> Daniel Bertaccini, Patricia Bagnes, Gonzalo Faccini</p>
		<p><strong>Fecha:</strong> 5 de Noviembre de 2026</p> 
	</div> 
</div> <div style="page-break-after: always;"></div>

### El problema

Un conjunto dominante de un grafo es un conjunto A de vértices tal que todo vértice que no está en A tiene un vecino en A. Encontrar el conjunto dominante más chico posible es un problema NP-completo, por lo cual no conocemos un algoritmo que lo resuelva de forma exacta en tiempo polinomial. Por eso usamos un **algoritmo goloso**, que en cada paso toma la decisión que en ese momento parece la mejor y no vuelve atrás.

El usuario carga el grafo, a mano o desde un archivo, aprieta "Calcular" y la aplicación muestra el conjunto obtenido y lo resalta en el dibujo del grafo.

### Arquitectura elegida

En los TP anteriores usamos forms and controls. Esta vez usamos **MVP (Model View Presenter)**, porque la ventana tiene que reaccionar a varios eventos (agregar un vértice, agregar una arista, cargar un archivo, empezar un grafo nuevo y calcular) y cada uno cambia lo que se muestra en más de un lugar: el dibujo, las listas de vértices, la barra de estado y el resultado. Al contar con el presenter, la lógica de "qué mostrar después de cada acción" queda fuera de Swing y la podemos testear.

- **Modelo** (paquete ```negocio```): el grafo, el algoritmo goloso y la lectura del archivo. No importa nada de ```javax.swing``` ni de ```java.awt```.
- **Presentador** (paquete ```presentacion```): ```GraphPresenter``` recibe las acciones del usuario, las aplica sobre el modelo y le dice a la vista qué mostrar. Tampoco depende de Swing.
- **Vista** (paquete ```interfaz```): ```MainForm``` arma la ventana y le pasa cada acción al presentador. Implementa la interfaz ```GraphView```, que es lo único que el presentador conoce de ella.

Por ejemplo, esto es lo que pasa al apretar "Calcular".

```java
public void calculateDominatingSet() {
    if (graph.getSize() == 0) {
        view.showError("El grafo no tiene vértices.");
        return;
    }
    Set<Integer> dominatingSet = MinimumDominatingSet.obtain(graph);
    view.showDominatingSet(dominatingSet);
}
```

Como el presentador solo habla con un ```GraphView```, en los tests le pasamos una vista falsa que guarda lo que le pidieron mostrar, y así probamos el comportamiento de la pantalla sin abrir ninguna ventana. Las dependencias van siempre de la vista al presentador y del presentador al modelo.

### Clases utilizadas

- ```Graph``` representa un grafo simple no dirigido con una matriz de adyacencia. Permite agregar vértices y aristas, consultar si existe una arista, obtener los vecinos de un vértice y contar las aristas. Valida que los vértices existan y que no haya lazos.
- ```MinimumDominatingSet``` tiene el algoritmo goloso. Recibe un ```Graph``` y devuelve el conjunto de vértices elegido. No modifica el grafo.
- ```GraphFileReader``` lee un grafo desde un archivo de texto. Si una línea tiene un error, avisa en qué línea está.
- ```GraphPresenter``` es el presentador. Guarda el grafo actual y coordina las acciones del usuario con la vista.
- ```GraphView``` es la interfaz que define lo que el presentador le puede pedir a la vista: mostrar el grafo, mostrar o borrar el resultado y mostrar un error.
- ```MainForm``` es la ventana. Arma los botones y las listas, y le pasa cada acción al presentador.
- ```GraphPanel``` dibuja el grafo con los vértices en círculo y pinta de azul los del conjunto dominante. Solo dibuja, no modifica el grafo ni calcula nada.

### Representación elegida

Para el grafo usamos una **matriz de adyacencia** de ```boolean```.

| Operación | Costo con matriz |
|---|---|
| Agregar o eliminar una arista | O(1) |
| Saber si existe una arista | O(1) |
| Obtener los vecinos de un vértice | O(n) |
| Agregar un vértice | O(n²) |
| Memoria | O(n²) |

Agregar un vértice cuesta O(n²) porque hay que copiar la matriz a una más grande. Lo aceptamos porque el usuario agrega los vértices de a uno con un botón, y para los tamaños que se pueden dibujar en la ventana la copia es instantánea. A cambio, saber si dos vértices son vecinos es O(1), y eso lo usan tanto el dibujo como la carga de aristas, que tiene que avisar si la arista ya existe.

Con listas de vecinos, obtener los vecinos costaría O(grado) en lugar de O(n), pero saber si existe una arista dejaría de ser O(1). Como los grafos de este problema son chicos porque los carga una persona, preferimos la matriz por ser más simple.

### El algoritmo goloso

En cada paso el algoritmo elige el vértice que **cubre más vértices que todavía no están cubiertos**, contando al vértice mismo y a sus vecinos. Lo agrega al conjunto, marca como cubiertos a él y a sus vecinos, y repite hasta que están todos cubiertos.

```java
while (covered.size() != graph.getSize()) {
    int vertex = mostCoveringVertex(graph, covered);
    covered.add(vertex);
    covered.addAll(graph.getNeighbours(vertex));
    mds.add(vertex);
}
```

El resultado siempre es un conjunto dominante, porque el ciclo termina recién cuando todos los vértices están cubiertos. Además el ciclo siempre avanza, mientras quede un vértice sin cubrir, ese vértice se cubre a sí mismo, así que el mejor candidato cubre por lo menos uno nuevo.

**Costo.** Elegir el mejor vértice recorre los n vértices y para cada uno pide sus vecinos, O(n) con la matriz, así que cada paso es O(n²). Hay como mucho n pasos, por lo que el algoritmo es O(n³) en el peor caso.

**No siempre da el mínimo.** Es una heurística. En el test ```greedyIsNotAlwaysOptimalTest``` armamos un grafo donde el vértice 0 es vecino de casi todos, entonces el goloso lo elige primero y termina con 3 vértices, pero {1, 2} ya es un conjunto dominante de tamaño 2. En la Figura 1 del enunciado sí encuentra un conjunto de tamaño 2, que es el mínimo.

### Formato del archivo

Es un archivo de texto plano. La primera línea es la cantidad de vértices, y cada línea siguiente es una arista con los dos vértices separados por un espacio. Los vértices se numeran desde 0. Las líneas vacías y lo que sigue a un ```#``` se ignoran. Este es el grafo de la Figura 1.

```
# Figura 1 del enunciado
6
0 1
0 4
1 4
1 2
2 3
3 4
3 5
```

Si el archivo tiene un error (una línea que no es un número, una arista con un vértice que no existe o un lazo) la aplicación muestra un cartel con el número de línea, y si está vacío avisa que está vacío.

### Buenas prácticas

- **Separated presentation.** Ninguna clase de ```negocio``` ni de ```presentacion``` importa Swing. La ventana no sabe cómo se calcula el conjunto dominante, solo muestra lo que le pasa el presentador.
- **Encapsulamiento.** La matriz de ```Graph``` es privada y solo se modifica con ```addEdge```, ```removeEdge``` y ```addVertex```. Los métodos que reciben vértices validan que existan. ```GraphPanel``` guarda una copia del conjunto dominante que recibe, así nadie lo puede cambiar desde afuera mientras se dibuja.
- **Validar en el negocio.** Los vértices inexistentes, los lazos y las cantidades negativas los rechaza ```Graph```. El presentador agrega los mensajes para el usuario, como "La arista 0 - 1 ya existe".
- **Errores con contexto.** Los errores del archivo dicen en qué línea están, por ejemplo "Línea 3: "a" no es un número".
- **Tests.** Todas las clases de negocio y el presentador tienen su clase de test con JUnit 4, en total 77 tests. Para el algoritmo probamos la Figura 1, grafos conocidos (estrella, camino, ciclo, completo, componentes separadas) y casos de borde: grafo vacío, un solo vértice, una sola arista, vértices aislados, grafo ```null``` y que el algoritmo no modifique el grafo. Además generamos 50 grafos aleatorios con semilla fija y comprobamos que el resultado siempre sea dominante.

### Interfaz

- **A la derecha están las acciones en el orden en que se usan**: primero el grafo (agregar vértice, cargar archivo, nuevo grafo), después las aristas y por último el cálculo.
- **Las aristas se cargan eligiendo los extremos de dos listas desplegables**, así no se puede pedir una arista con un vértice que no existe. Con menos de dos vértices el botón está deshabilitado.
- **El grafo se dibuja a la izquierda**, con los vértices en círculo, y el conjunto dominante se pinta de azul con una leyenda.
- **El resultado muestra el tamaño y los vértices** en notación de conjunto, por ejemplo ```{2, 4}```.
- **Si el grafo cambia, el resultado anterior se borra**, para que nunca quede a la vista un conjunto que corresponde a otro grafo.
- **"Nuevo grafo" pide confirmación** antes de borrar lo cargado.
- **Abajo hay una barra de estado** con la cantidad de vértices y de aristas.

### Decisiones tomadas durante el desarrollo

- **Implementamos las dos formas de carga.** La consigna pide una, pero cargar a mano sirve para grafos chicos y el archivo para no tener que repetir la carga cada vez.
- **Empates.** Si dos vértices cubren la misma cantidad de vértices nuevos, se elige el de menor número. La consigna no dice nada y cualquiera de los dos da un conjunto dominante válido.
- **Vértices aislados.** Un vértice sin vecinos solo se puede cubrir a sí mismo, así que siempre queda en el conjunto.
- **Grafo sin vértices.** Calcular sobre un grafo vacío muestra un aviso en lugar de un conjunto vacío, porque probablemente el usuario se olvidó de cargarlo.
- **Formato de texto plano y no JSON.** Una arista por línea es fácil de escribir a mano y no necesita ninguna biblioteca externa.
- **No se pueden borrar vértices.** Borrar uno cambiaría el número de todos los que están después. Si se carga algo mal, se empieza con "Nuevo grafo" o se corrige el archivo.

### Objetivos opcionales

De los tres objetivos opcionales implementamos uno, **visualizar el grafo y el conjunto dominante** en la interfaz. ```GraphPanel``` dibuja los vértices en círculo y las aristas como líneas, y resalta en azul los vértices del conjunto obtenido.

No implementamos el algoritmo de backtracking ni el test de stress.
