# Tabley

Tabley es una aplicación Android para organizar información mediante cuadernos y tablas personalizables. Permite crear cuadernos, agregar tablas y definir filas, columnas y celdas con distintos tipos de datos. Toda la información se guarda localmente para que la aplicación funcione sin conexión.

## Problema que resuelve

Muchas personas organizan información sencilla en notas, documentos o planillas que resultan incómodas desde el celular. Tabley ofrece una estructura clara y rápida para registrar información cotidiana sin depender de una computadora ni de una conexión a Internet.

## Funcionalidades implementadas

- Crear, renombrar y eliminar cuadernos.
- Elegir un color para cada cuaderno.
- Crear y listar tablas dentro de un cuaderno.
- Crear columnas de texto, número, fecha, sí/no y lista o notas.
- Crear y eliminar filas.
- Editar y guardar celdas.
- Eliminar columnas y sus celdas asociadas.
- Navegar entre cuadernos, tablas y detalle de tabla.
- Persistir toda la información localmente con Room.
- Mostrar estados de carga, contenido vacío y errores de operación.

## Tecnologías

- Kotlin.
- Android Studio.
- Jetpack Compose y Material 3.
- Navigation Compose.
- ViewModel, Coroutines y StateFlow.
- Room.
- Hilt.
- MVVM y principios de Clean Architecture.

## Arquitectura

El proyecto separa responsabilidades en las siguientes capas:

```text
presentation
  Compose UI -> ViewModel -> UiState
                         |
domain                   v
  modelos <- casos de uso <- contratos de repositorio
                         |
data                     v
  repositorios -> DAO -> Room
```

- **Presentation:** representa el estado y comunica las acciones del usuario.
- **ViewModel:** coordina los casos de uso y expone un estado observable e inmutable.
- **Domain:** contiene modelos, contratos y reglas sin depender de Android.
- **Data:** implementa los repositorios y persiste la información mediante Room.

La interacción sigue un flujo unidireccional: la interfaz envía una acción al ViewModel, el ViewModel ejecuta un caso de uso y Room emite los datos actualizados. El nuevo `UiState` vuelve a Compose para actualizar la pantalla.

## Modelo de datos

- `NotebookEntity`: cuadernos.
- `TableEntity`: tablas de cada cuaderno.
- `ColumnEntity`: columnas y sus tipos.
- `RowEntity`: filas de cada tabla.
- `CellEntity`: valores de cada combinación fila-columna.

Las claves foráneas usan borrado en cascada. Al eliminar un cuaderno se eliminan sus tablas y, al eliminar filas o columnas, también se eliminan las celdas relacionadas.

## Estrategia offline

Room es la fuente local de datos y permite utilizar las funcionalidades principales sin conectividad. La versión actual no necesita Retrofit porque no consume servicios remotos. Esta decisión evita agregar una dependencia que no resuelve una necesidad del alcance definido.

## Ejecución

1. Clonar el repositorio:

   ```bash
   git clone https://github.com/candearbe/tabley-app.git
   cd tabley-app
   ```

2. Abrir la carpeta del proyecto en Android Studio.
3. Esperar la sincronización de Gradle.
4. Seleccionar un emulador o dispositivo con Android 7.0 (API 24) o superior.
5. Ejecutar la configuración `app`.

También puede generarse un APK de depuración desde **Build > Build APK(s)**.

## Recorrido sugerido para la demo

1. Crear un cuaderno y seleccionar un color.
2. Crear una tabla dentro del cuaderno.
3. Agregar columnas con distintos tipos de datos.
4. Agregar filas y editar sus celdas.
5. Cerrar y volver a abrir la aplicación para comprobar la persistencia.
6. Eliminar una fila, columna o cuaderno y verificar el borrado relacionado.

## Pruebas

El proyecto incluye pruebas unitarias para comprobar que el guardado de una celda crea un registro nuevo o actualiza el existente sin duplicarlo.

```bash
./gradlew testDebugUnitTest
```

## Limitaciones conocidas

- No existe sincronización con un backend.
- Los tipos fecha y lista se cargan mediante un editor de texto simple.
- No se implementó colaboración entre usuarios.
- No se incluyen plantillas prediseñadas en esta versión.

## Integrantes y responsabilidades

responsabilidades principales de cada integrante:

