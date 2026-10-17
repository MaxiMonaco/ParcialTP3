# VORA Coach — Parcial Domiciliario TP3

App Android (Kotlin + Jetpack Compose) a partir del diseño de VORA Coach en Figma.
Universidad ORT Argentina · Desarrollo de Aplicaciones Móviles · Prof. Martín Rivas · 2026.

- **Figma:** https://www.figma.com/design/1nvbbfYNYmKAevuaG28J85/Parcial-Domiciliario-7-Oct-26
- **Entrega:** 28 de octubre de 2026 (hito 1: 21 de octubre) · etiqueta prevista: `entrega-tp3`

## Integrantes

| Integrante | Parte |
|---|---|
| Francisco Negri | Estructura base, navegación, inventario, tema y componentes compartidos, maquetación inicial |
| _(completar)_ | _(completar)_ |
| _(completar)_ | _(completar)_ |
| _(completar)_ | _(completar)_ |

## Cómo ejecutar

- Android Studio reciente, JDK 17 o superior, dispositivo o emulador con Android 7.0 (API 24) o superior.
- Abrir la carpeta del proyecto, esperar la sincronización de Gradle y ejecutar el módulo `app` (▶).
- `local.properties` es local de cada máquina (ruta del SDK) y no se sube al repositorio.

Versiones principales (ver `gradle/libs.versions.toml`): AGP 9.3.3 · Kotlin 2.2.10 · Compose BoM 2026.02.01 ·
Navigation Compose 2.10.2 · Hilt 2.60.1 · Room 2.8.5 · Retrofit 3.0.0 · compileSdk / targetSdk 37 · minSdk 24.

## Estado actual del proyecto

| Área | Estado |
|---|---|
| 37 pantallas de Figma maquetadas y navegables | ✅ Hecho (con datos de ejemplo fijos) |
| Tema (colores, Manrope, formas) y componentes compartidos | ✅ `ui/theme/` y `shared/VoraComponents.kt` |
| Barra inferior compartida y navegación entre los 7 flujos | ✅ `navigation/` |
| Inventario de pantallas (NAV01) | ✅ [`docs/INVENTARIO_PANTALLAS.md`](docs/INVENTARIO_PANTALLAS.md) |
| Retrofit: catálogo y detalle de entrenamientos (RF01) | ⬜ Pendiente |
| Room: caché de entrenamientos (RF02) | ⬜ Pendiente |
| Room: carga, listado y eliminación de comidas (RF03, RF04) | ⬜ Pendiente |
| Estados de carga, vacío y error con reintento (RF05) | ⬜ Pendiente |
| Repositorios, ViewModel e inyección con Hilt (RF06 a RF08) | ⬜ Pendiente |

Los datos que hoy muestran las pantallas son **de ejemplo y están escritos en el código**: no vienen
de la API ni de la base de datos.

## Inventario de pantallas

El inventario completo (37 pantallas, ruta, estado y supuestos) está en
[`docs/INVENTARIO_PANTALLAS.md`](docs/INVENTARIO_PANTALLAS.md). Resumen:

| Flujo | Pantallas | Implementación objetivo |
|---|---|---|
| Acceso (Login) | 2 | Demostración sin Firebase |
| Inicio (Home) | 2 | Datos de ejemplo |
| Asistente VORA (Voice) | 6 | Simulado |
| Entrenamiento (Workout) | 7 | Funcional: Retrofit + Room |
| Nutrición (Nutrition) | 8 (+1 extra) | Funcional: Room |
| Progreso (Progress) | 6 | Datos de demostración |
| Perfil (Profile) | 6 | Datos locales |

La pantalla extra **Nutrition / 03b Manual** no está en el diseño: se agregó porque *Log Manually* necesita un formulario.

## Navegación

- Un grafo anidado por flujo (`navigation/*Graph.kt`) y rutas centralizadas en `AppDestination`.
- Las rutas con argumentos (`workoutId`, `exerciseId`, `mealId`) hacen que cada entrenamiento, ejercicio y comida
  abra su propio detalle (NAV04).
- La barra inferior es un único componente (`VoraBottomBar`) y aparece solo donde Figma la muestra.
- Las pantallas no reciben el `NavController`: reciben lambdas. Quien conecte datos reales no toca la navegación.
- Los controles sin pantalla propia en Figma (editar, duplicar, ayuda, ajustes) abren un diálogo de demostración.

## Funciones simuladas

- **Login:** acceso de demostración, sin autenticación real ni Firebase.
- **Asistente de voz y análisis de comida:** estados con texto de ejemplo. No se pide micrófono ni cámara.
- **Entrenamiento activo:** las series avanzan con un botón; el cronómetro y el volumen no se calculan.
- **Progreso y Perfil:** valores fijos de ejemplo.

## Estructura del código

```
app/src/main/java/com/example/parcialtp3/
├── navigation/   rutas, grafos por flujo, barra inferior, extensiones
├── shared/       componentes visuales reutilizables (VoraComponents.kt)
├── ui/theme/     colores, tipografía (Manrope), formas
└── ui/<flujo>/   pantallas de cada flujo
```

El código del proyecto anterior (app de frases: Hilt, Room, Retrofit) sigue en el repositorio como base técnica
para las integraciones y todavía no se usa en VORA.

## Uso de IA generativa

Se usó **Claude (Anthropic)** como asistente de programación para: armar la estructura de navegación y el
inventario, definir el tema y los componentes a partir del sistema de diseño de Figma, y maquetar las pantallas a partir
de los PDF exportados. Todo el código fue revisado, ejecutado y probado por los integrantes del equipo.
_(Completar con el uso de IA de cada integrante en sus partes.)_

## Video de demostración y comparaciones visuales

- Video: _(pendiente)_
- Cinco comparaciones Figma vs app: _(pendientes)_
