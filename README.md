# VORA Coach — Parcial Domiciliario TP3

App Android (Kotlin + Jetpack Compose) a partir del diseño de VORA Coach en Figma.
Universidad ORT Argentina · Desarrollo de Aplicaciones Móviles · Prof. Martín Rivas · 2026.

- **Figma:** https://www.figma.com/design/1nvbbfYNYmKAevuaG28J85/Parcial-Domiciliario-7-Oct-26
- **Entrega:** 28 de octubre de 2026 (hito 1: 21 de octubre) · etiqueta de entrega: `entrega-tp3`
- **Video de demostración:** _(pendiente: pegar el enlace)_

## Integrantes

| Integrante | Parte |
|---|---|
| Maximiliano Monaco | Estructura base, navegación, tema y componentes compartidos, maquetación de las 37 pantallas según Figma, inventario de pantallas, optimización de release y README |
| Francisco Bermingham | Integracion de Retrofit y Room en las funcionalidades requeridas (workout y nutrition)|
| Santiago Schmale | Verificación (QA) contra la consigna, plan de pruebas y hallazgos, test unitario del repositorio, limpieza del código heredado, coordinación de la entrega |
| Sofía Soler | Firebase Authentication (ampliación): registro, inicio de sesión, recuperación de contraseña, sesión persistente y cierre de sesión, con repositorio, módulo de Hilt y ViewModel |

## Cómo ejecutar

- Android Studio reciente, JDK 17 o superior, dispositivo o emulador con Android 7.0 (API 24) o superior y conexión a internet.
- Abrir la carpeta del proyecto, esperar la sincronización de Gradle y ejecutar el módulo `app` (▶).
- `local.properties` es local de cada máquina (ruta del SDK) y no se sube al repositorio.
- Pruebas unitarias: `./gradlew test` (lógica de comidas: validación y totales, `MealLogicTest`).

Versiones principales (ver `gradle/libs.versions.toml`): AGP 9.3.3 · Kotlin 2.2.10 · Compose BoM 2026.02.01 ·
Navigation Compose 2.10.2 · Hilt 2.60.1 · Room 2.8.5 · Retrofit 3.0.0 (+ converter Gson) · compileSdk / targetSdk 37 · minSdk 24.

## Servidor y URL

- **Servidor usado:** VORA Mock API publicada en `https://tp3.martinrivas.ar/` (sin autenticación).
- **Dónde se configura:** constante `BASE_URL` en `app/src/main/java/com/example/parcialtp3/di/NetworkModule.kt` (tiene que terminar en `/`).
- **Servidor local (opcional):** si se levanta el mock en la PC, cambiar `BASE_URL` a `http://10.0.2.2:<puerto>/` (emulador) y habilitar tráfico HTTP solo para desarrollo.
- **Endpoints usados:** `GET /v1/workouts` (catálogo) y `GET /v1/workouts/{workoutId}` (detalle).
- **Escenarios de prueba (RF05):** el servidor acepta los headers `X-Mock-Scenario` (`normal`, `empty`, `error`, `malformed`) y `X-Mock-Delay-Ms`.

## Estado de las funcionalidades

| Funcionalidad | Estado |
|---|---|
| 37 pantallas de Figma maquetadas y navegables | ✅ Hecho |
| Tema (colores, Manrope, formas) y componentes compartidos | ✅ `ui/theme/` y `shared/` |
| Barra inferior compartida y navegación entre los 7 flujos | ✅ `navigation/` |
| Catálogo y detalle de entrenamientos con Retrofit (RF01) | ✅ Hecho |
| Caché de entrenamientos en Room, detalle visible sin conexión (RF02) | ✅ Hecho |
| Carga manual de comidas con validación (RF03) | ✅ Hecho |
| Listado por fecha, eliminación con confirmación y total diario en Room (RF04) | ✅ Hecho |
| Estados de carga, vacío y error con reintento (RF05) | ✅ Hecho |
| DTO → modelo → Entity y repositorios (RF06) | ✅ Hecho |
| ViewModel con estado observable y coroutines (RF07) | ✅ Hecho |
| Hilt: módulos, `@HiltViewModel`, repositorios por constructor (RF08) | ✅ Hecho |
| Login de demostración (sin Firebase) | ✅ Demostración |
| Coach de voz y análisis de comida (voz / foto) | 🟡 Simulado, no se piden permisos |
| Firebase y ampliaciones | ⬜ No incluidos |

## Inventario de pantallas (NAV01)

Los nombres son los frames reales de Figma. **Estado:** *Funcional* = usa Retrofit y/o Room;
*Demostración / Simulado / Datos locales* = datos de ejemplo o estados simulados. También está en
[`docs/INVENTARIO_PANTALLAS.md`](docs/INVENTARIO_PANTALLAS.md).

| # | Flujo | Pantalla (Figma) | Ruta / destino | Estado |
|---|---|---|---|---|
| 1 | Login | Login - 01 SignIn | `login/01_signin` | Demostración (sin Firebase) |
| 2 | Login | Login - 02 SignUp | `login/02_signup` | Demostración (sin Firebase) |
| 3 | Home | Home / 01 — Dashboard | `home/01_dashboard` | Demostración |
| 4 | Home | Home / 02 — Notifications | `home/02_notifications` | Demostración |
| 5 | AI Voice | Voice / 01 — Idle | `voice/01_idle` | Simulado |
| 6 | AI Voice | Voice / 02 — Listening | `voice/02_listening` | Simulado |
| 7 | AI Voice | Voice / 03 — Processing | `voice/03_processing` | Simulado |
| 8 | AI Voice | Voice / 04 — Speaking | `voice/04_speaking` | Simulado |
| 9 | AI Voice | Voice / 05 — Conversation | `voice/05_conversation` | Simulado |
| 10 | AI Voice | Voice / 06 — Error | `voice/06_error` | Simulado |
| 11 | Workout | Workout / 01 — Dashboard | `workout/01_dashboard` | Funcional: Retrofit + Room |
| 12 | Workout | Workout / 02 — Weekly Plan | `workout/02_weekly_plan` | Mixta: entrenamientos del catálogo (Retrofit + Room), días del plan de ejemplo |
| 13 | Workout | Workout / 03 — Workout Details | `workout/03_details/{workoutId}` | Funcional: Retrofit + Room |
| 14 | Workout | Workout / 04 — Pre Workout | `workout/04_pre/{workoutId}` | Funcional: Retrofit + Room (el resto, demostración) |
| 15 | Workout | Workout / 05 — Active Workout | `workout/05_active/{workoutId}` | Ejercicios reales (Retrofit + Room); cronómetro, descanso y volumen simulados |
| 16 | Workout | Workout / 06 — Exercise Details | `workout/06_exercise/{exerciseId}` | Funcional: Retrofit + Room |
| 17 | Workout | Workout / 07 — Workout Complete | `workout/07_complete` | Simulado |
| 18 | Nutrition | Nutrition / 01 — Dashboard | `nutrition/01_dashboard` | Funcional: Room |
| 19 | Nutrition | Nutrition / 02 — Today's Meals | `nutrition/02_todays_meals` | Funcional: Room |
| 20 | Nutrition | Nutrition / 03 — Add Food | `nutrition/03_add_food` | Funcional: Room (carga manual) |
| 21 | Nutrition | Nutrition / 04 — Voice Food Logging | `nutrition/04_voice_logging` | Simulado |
| 22 | Nutrition | Nutrition / 05 — Food Analysis | `nutrition/05_food_analysis` | Simulado |
| 23 | Nutrition | Nutrition / 06 — Photo Analysis | `nutrition/06_photo_analysis` | Simulado |
| 24 | Nutrition | Nutrition / 07 — Food Confirmation | `nutrition/07_food_confirmation` | Funcional: Room (guarda) |
| 25 | Nutrition | Nutrition / 08 — Meal Details | `nutrition/08_meal/{mealId}` | Funcional: Room (eliminar) |
| 26 | Progress | Progress / 01 — Today's Overview | `progress/01_todays_overview` | Demostración |
| 27 | Progress | Progress / 02 — Dashboard | `progress/02_dashboard` | Demostración |
| 28 | Progress | Progress / 03 — Workout Progress | `progress/03_workout_progress` | Demostración |
| 29 | Progress | Progress / 04 — Nutrition Progress | `progress/04_nutrition_progress` | Demostración |
| 30 | Progress | Progress / 05 — Health Trends | `progress/05_health_trends` | Demostración |
| 31 | Progress | Progress / 06 — Weekly Review | `progress/06_weekly_review` | Demostración |
| 32 | Profile | Profile / 01 — Dashboard | `profile/01_dashboard` | Datos locales |
| 33 | Profile | Profile / 02 — Edit Profile | `profile/02_edit_profile` | Datos locales |
| 34 | Profile | Profile / 03 — Goals & Preferences | `profile/03_goals_preferences` | Datos locales |
| 35 | Profile | Profile / 04 — Nutrition Preferences | `profile/04_nutrition_preferences` | Datos locales |
| 36 | Profile | Profile / 05 — Voice & AI Settings | `profile/05_voice_ai_settings` | Datos locales |
| 37 | Profile | Profile / 06 — Settings | `profile/06_settings` | Datos locales |
| — | Nutrition | *(extra)* Nutrition / 03b — Manual | `nutrition/03b_manual_food` | Funcional: Room |

Las 37 filas numeradas son los frames de Figma. La fila "extra" **no está en el diseño**: se agregó
porque "Log Manually" en *Add Food* necesita un formulario y la carga manual es obligatoria (RF03).

## Navegación

- Un grafo anidado por flujo (`navigation/*Graph.kt`) y rutas centralizadas en `AppDestination`.
- Las rutas con argumentos (`workoutId`, `exerciseId`, `mealId`) hacen que cada entrenamiento, ejercicio y comida
  abra su propio detalle (NAV04).
- La barra inferior es un único componente (`VoraBottomBar`) y aparece solo donde Figma la muestra.
- Las pantallas reciben lambdas de navegación, no el `NavController`; los datos los pide cada pantalla a su ViewModel.
- Los controles sin pantalla propia en Figma (editar, duplicar, ayuda, ajustes) abren un diálogo de demostración (NAV03).
- Login → Home y Cerrar sesión limpian la pila de navegación; atrás desde una pestaña vuelve a Home.

## Arquitectura

```
app/src/main/java/com/example/parcialtp3/
├── navigation/   rutas, grafos por flujo, barra inferior
├── shared/       componentes visuales reutilizables y vistas de estado (carga, error, vacío)
├── ui/theme/     colores, tipografía (Manrope), formas
├── ui/<flujo>/   pantallas de cada flujo
├── viewmodel/    estado de cada pantalla (StateFlow) y eventos
├── data/
│   ├── remote/       DTO y API de Retrofit
│   ├── local/        Entity, DAO y base de datos de Room
│   └── repository/   coordina red y caché; único punto de acceso a los datos
├── model/        modelos de la app (+ conversiones DTO → modelo → Entity)
└── di/           módulos de Hilt (red y base de datos)
```

**Flujo de datos (RF06).** La pantalla observa un ViewModel; el ViewModel pide datos al repositorio; el repositorio
coordina Retrofit y Room. La UI nunca llama a Retrofit ni a un DAO. Cada capa tiene su tipo:
DTO (JSON de la API) → modelo (lo que usa la app) → Entity (fila de Room).

- **Workout:** la UI siempre lee de Room. Al entrar se consulta la API y, **solo si la respuesta es válida**, se
  reemplaza la caché. Si la red falla (sin conexión, 503, JSON inválido), la última caché válida se conserva y se
  muestra un aviso. Un detalle que nunca se descargó informa que no está disponible sin conexión (RF02, RF05).
- **Nutrition:** las comidas se guardan en Room; el listado por fecha es un `Flow`, así que el listado y el total
  diario cambian solos al agregar o eliminar. Eliminar pide confirmación (RF03, RF04).

## Hilt y Dagger (RF08)

Hilt es una capa de Android construida sobre **Dagger**: Dagger genera en compilación el código que crea y conecta
los objetos; Hilt agrega los componentes ya definidos para Android (aplicación, actividad, ViewModel), de modo que no
hace falta armar el grafo a mano. En este proyecto:

- `@HiltAndroidApp` (`VoraApplication`) crea el contenedor de dependencias de la aplicación.
- `@AndroidEntryPoint` (`MainActivity`) habilita la inyección en la actividad y en las pantallas Compose.
- `@HiltViewModel` + `hiltViewModel()` construyen cada ViewModel; Hilt le pasa el `SavedStateHandle` con los
  argumentos de la ruta (por eso `workoutId` y `mealId` llegan solos).
- Los **módulos** le enseñan a Hilt a construir lo que no puede crear por constructor:
  `NetworkModule` provee `OkHttpClient`, `Retrofit` y `WorkoutsApi` (Retrofit genera su implementación);
  `DatabaseModule` provee `AppDatabase` (Room) y los DAO.
- Los repositorios (`WorkoutsRepository`, `MealsRepository`) y los ViewModels llevan `@Inject constructor`: Hilt
  construye todo el árbol, de la base de datos a la pantalla, y reutiliza una sola instancia (`@Singleton`) de
  Retrofit y de la base de datos.

## Firebase (ampliación)

**Qué se implementó (Authentication con email y contraseña):**

- **Registro** (*Sign Up*) e **inicio de sesión** (*Sign In*) con validación de campos antes de llamar a Firebase:
  email con formato válido, contraseña de 8 caracteres o más en el registro (lo que indica Figma) y nombre no vacío.
- **Indicador de carga** mientras Firebase responde y **errores en español** en un Snackbar: credenciales incorrectas,
  email ya registrado, contraseña débil, sin conexión y demasiados intentos.
- **Recuperación de contraseña:** *Forgot your password?* (Sign In) y *Recover* (Sign Up) envían el email de reseteo
  de Firebase al email escrito en el campo.
- **Sesión persistente:** si el usuario no cerró sesión, al volver a abrir la app entra directo a Home
  (`MainActivity` recibe el `AuthRepository` por inyección de campo y elige el `startDestination`).
- **Cierre de sesión:** *Profile → Settings → Log out* cierra la sesión, limpia la pila y vuelve al login; atrás no
  regresa a la app.

**Cómo está armado:** las pantallas de login observan `AuthViewModel` (`@HiltViewModel`), que usa la interfaz
`AuthRepository`. `FirebaseModule` (Hilt) provee `FirebaseAuth` y entrega `FirebaseAuthRepository` si Firebase está
configurado o `DemoAuthRepository` si no; usa `Provider<T>` para que la implementación de Firebase solo se construya
si hace falta. La UI nunca llama a Firebase.

**Sin Firebase configurado** (sin `app/google-services.json`), la app compila y usa `DemoAuthRepository`: se entra sin
completar los datos, como en la versión de demostración. La pantalla de login lo indica con un texto aclaratorio.

**Cómo configurarlo:**

1. Crear un proyecto en [console.firebase.google.com](https://console.firebase.google.com) (Google Analytics no es necesario).
2. Agregar una app Android con el paquete `com.example.parcialtp3` (el SHA-1 no hace falta para email y contraseña).
3. Descargar `google-services.json` y copiarlo en la carpeta `app/`. **No se sube al repositorio** (está en `.gitignore`).
4. En **Authentication → Método de acceso**, habilitar **Correo electrónico/contraseña**.
5. Sincronizar Gradle y ejecutar: el plugin de Google Services se aplica solo si el archivo existe (`app/build.gradle.kts`).

**Archivos:** `auth/AuthState.kt`, `auth/AuthValidation.kt`, `data/repository/AuthRepository.kt` (interfaz),
`FirebaseAuthRepository.kt`, `DemoAuthRepository.kt`, `di/FirebaseModule.kt`, `viewmodel/AuthViewModel.kt`,
`shared/VoraAuthField.kt`, `ui/login/LoginScreens.kt`, `navigation/LoginGraph.kt`, `navigation/ProfileGraph.kt`
(Log out), `navigation/AppNavHost.kt` y `MainActivity.kt` (sesión persistente).

**No incluido:** perfil en Firestore, reglas de seguridad y acceso con Apple, Google o Face ID (esos botones muestran
un aviso de demostración). Home, Profile y Settings siguen mostrando el usuario de ejemplo de Figma (*Alex Morgan*), y el
nombre ingresado en *Sign Up* se valida pero no se guarda.

## Funciones simuladas

- **Login:** acceso de demostración, sin autenticación real ni Firebase.
- **Asistente de voz y análisis de comida (voz / foto):** estados con texto de ejemplo. No se pide micrófono ni cámara.
  "Confirmar" en Food Confirmation guarda de verdad una comida de ejemplo en Room.
- **Entrenamiento activo:** los ejercicios son los reales; el cronómetro, el descanso, el avance de series y el volumen están simulados.
- **Plan semanal, Progreso y Perfil:** valores fijos de ejemplo.
- **Controles sin servicio** (editar entrenamiento, duplicar comida, buscar en base de datos, idioma, etc.): diálogo que informa el modo demostración.

## Limitaciones conocidas

- La base de datos usa `fallbackToDestructiveMigration()` (solo desarrollo): si cambia el esquema, Room recrea la base y se pierden las comidas guardadas.
- Los accesos directos de Home y Progress usan los ids `w01` / `w02` de la API (`SampleIds`); si el servidor cambia esos ids, dejan de abrir el detalle.
- Los íconos de acciones y las fotos de ejercicios, comidas y perfil todavía no son los de Figma (emojis y bloques de color).

## Supuestos (el Figma no los define)

1. **Progress / 01 Today's Overview** es la raíz de la pestaña Progress; el acceso a **Progress / 02 Dashboard**
   es un botón agregado.
2. **Profile / 05 Voice & AI Settings** se abre desde *Profile / 06 Settings* (en el diseño no hay botón que lo abra).
3. **Voice / 06 Error** se alcanza con un acceso de demostración desde *Voice / 01 Idle*.
4. Los controles sin pantalla propia (Search Database, Units, Appearance,
   Connected Data, Allow Access / Not Now) se resuelven como diálogo o ajuste dentro de la vista, o se
   marcan como demostración. No se piden permisos de micrófono ni cámara.

## Uso de IA generativa

Se usó **Claude (Anthropic)** como asistente de programación. Cada integrante declara su uso; todo el código fue revisado, ejecutado y probado por el equipo.

- **Maximiliano Monaco:** usé Claude (Anthropic) como asistente de programación para la estructura de navegación y el inventario, el tema y los componentes a partir del sistema de diseño de Figma, la maquetación de las pantallas a partir de los PDF exportados, la optimización de release, el README y la revisión de los pull requests del equipo. Revisé, compilé y probé el código en un dispositivo físico (Samsung SM-A536E).
- **Francisco Bermingham:** guia para la integracion de funcionalidades con retrofit y room.
- **Santiago Schmale:** análisis del repositorio contra la consigna, redacción del plan de pruebas, test unitario `WorkoutsRepositoryTest` (dobles de prueba a mano) y limpieza del código heredado. Verificación manual de la API contra la colección Postman del docente y pruebas en dispositivo físico.
- **Sofía Soler:** guía paso a paso para integrar Firebase Authentication (repositorio con implementación de demostración, módulo de Hilt, ViewModel, conexión de las pantallas de acceso y sesión persistente). Cada paso se implementó, compiló y probó en emulador (registro, login, errores, recuperación de contraseña, sesión persistente y cierre de sesión) antes de su commit.

## Comparaciones visuales Figma vs app

Referencia: [prototipo navegable en Figma](https://www.figma.com/proto/1nvbbfYNYmKAevuaG28J85/Parcial-Domiciliario-7-Oct-26?node-id=1-673&p=f&scaling=min-zoom&content-scaling=fixed&page-id=0%3A1).
Capturas en `docs/comparaciones/` (Figma a la izquierda, app a la derecha).

| Pantalla | Figma | App | Diferencias |
|---|---|---|---|
| Home / 01 — Dashboard | `docs/comparaciones/01-home-figma.png` | `docs/comparaciones/01-home-app.png` | _(pendiente)_ |
| Workout / 01 — Dashboard | `docs/comparaciones/02-workout-figma.png` | `docs/comparaciones/02-workout-app.png` | _(pendiente)_ |
| Nutrition / 01 — Dashboard | `docs/comparaciones/03-nutrition-figma.png` | `docs/comparaciones/03-nutrition-app.png` | _(pendiente)_ |
| Progress / 01 — Today's Overview | `docs/comparaciones/04-progress-figma.png` | `docs/comparaciones/04-progress-app.png` | _(pendiente)_ |
| Profile / 01 — Dashboard | `docs/comparaciones/05-profile-figma.png` | `docs/comparaciones/05-profile-app.png` | _(pendiente)_ |

## Verificación (QA)

Plan de pruebas trazable a los criterios de la consigna y a la demostración final: [`docs/QA-PLAN.md`](docs/QA-PLAN.md).
Pruebas unitarias en JVM: `./gradlew :app:testDebugUnitTest` (`MealLogicTest`, `WorkoutsRepositoryTest`).
