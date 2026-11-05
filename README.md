# TP3 - Programación III: Conjunto dominante mínimo

Aplicación Swing que carga un grafo y encuentra un conjunto dominante con un algoritmo goloso.

## Cómo ejecutar

- Aplicación: correr `interfaz.MainForm`.
- Tests: correr las clases del paquete `tests` (JUnit 4, los jars están en `lib/`).

## Organización (MVP)

| Paquete        | Contenido |
|----------------|-----------|
| `negocio`      | Modelo: `Graph`, `MinimumDominatingSet`, `GraphFileReader` |
| `presentacion` | `GraphPresenter` y la interfaz `GraphView` |
| `interfaz`     | Vista Swing: `MainForm` y `GraphPanel` |
| `tests`        | Tests unitarios de negocio y del presentador |

## Formato del archivo del grafo

Texto plano. La primera línea es la cantidad de vértices y cada línea siguiente es una arista.
Los vértices se numeran desde 0. Las líneas vacías y lo que sigue a `#` se ignoran.

```
# Figura 1 del enunciado
6
0 1
0 4
1 4
```

Hay un ejemplo en `ejemplos/figura1.txt`.
