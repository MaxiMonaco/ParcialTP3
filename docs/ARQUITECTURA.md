# Arquitectura

Resumen de cómo está organizado el código y por qué. Complementa el README.

## Idea general

```
Pantalla (Compose) -> ViewModel -> Repositorio -> Retrofit (API)
                                               -> Room (base local)
```

La pantalla no llama a la API ni a la base: observa el estado del ViewModel. El ViewModel le pide datos al repositorio. El repositorio es el único que conoce Retrofit y Room. Hilt crea y conecta todas las piezas.

## Paquetes

- `navigation/` — rutas (`AppDestination`), un grafo por flujo (`*Graph.kt`), barra inferior, y las reglas de la pila en `NavExtensions.kt`.
- `ui/<flujo>/` — pantallas de cada flujo. `ui/theme/` tiene colores, tipografía (Manrope) y formas. `shared/` tiene los componentes reutilizables (`VoraComponents.kt`) y las vistas de estado (`StateViews.kt`: carga, error, vacío, aviso).
- `viewmodel/` — un ViewModel por pantalla con datos. Exponen `StateFlow`.
- `data/remote/` — `WorkoutsApi` (Retrofit), `WorkoutDto` y `ApiResponse` (forma del JSON).
- `data/local/` — `AppDatabase`, entidades y DAO de Room.
- `data/repository/` — `WorkoutsRepository` (API + caché) y `MealsRepository` (solo Room).
- `model/` — modelos que usa la app y los mappers DTO → modelo → Entity.
- `di/` — módulos de Hilt para Retrofit, Room y DAO.

## Navegación

- `AppDestination` tiene una ruta por pantalla de Figma, con el mismo nombre (`workout/03_details/{workoutId}`). Los `*Flow` son grafos anidados, uno por pestaña.
- Las pantallas reciben lambdas (`onBackClick`, `onStartWorkoutClick`…), nunca el `NavController`. Solo el grafo navega.
- Las rutas con argumento usan `createRoute(id)`, así cada entrenamiento, ejercicio y comida abre su propio detalle.
- `NavExtensions.kt`: cambiar de pestaña apila sobre Home y guarda/restaura estado (atrás vuelve a Home); login y logout limpian toda la pila.
- Controles que en Figma no tienen pantalla abren un diálogo de demostración.

## Workout: Retrofit + Room

- `WorkoutsApi`: `GET v1/workouts` y `GET v1/workouts/{id}`. Respuestas envueltas en `ApiResponse<T>` (`{ "data": … }`). Los campos del DTO son nullable porque Gson no respeta los tipos no-nulos de Kotlin.
- Tablas: `workouts` (catálogo, con `position` para conservar el orden), `workout_details` (un detalle ya descargado) y `exercises` (id = `workoutId:exerciseId` para que no choquen entre entrenamientos). `WorkoutWithExercises` une detalle y ejercicios con `@Relation`.
- `WorkoutsRepository`: la UI lee siempre de Room (`catalog`, `observeDetail`, `observeExercise` devuelven `Flow`). `refreshCatalog()` y `refreshDetail(id)` piden a la API y, solo si la respuesta es válida, reemplazan la caché en una transacción. Si falla (sin red, 503, JSON inválido, 404, `data` nulo) devuelven `Result.failure` y Room no se toca. Por eso un detalle ya visto se ve sin conexión, y uno nunca descargado avisa que no está disponible.
- `WorkoutsViewModel` combina el `Flow` de Room con el estado del último refresh: con datos muestra `Content` (y un aviso si el refresh falló); sin datos muestra `Loading`, `Error` o `Empty`. `retry()` repite el refresh.
- `WorkoutDetailViewModel` toma el `workoutId` de `SavedStateHandle` (llega desde la ruta).

## Nutrition: Room

- `MealEntity` con fecha como texto `yyyy-MM-dd` (ordena y filtra bien) y macros como `Double`.
- `MealDao.observeByDate(date)` devuelve `Flow`: cuando se agrega o borra una comida, el listado y el total diario se actualizan solos.
- Validación en `model/Meal.kt` (`validateMealForm`): nombre no vacío, fecha real, números no negativos. Es lógica pura y tiene test.
- `AddMealViewModel` valida y guarda; `MealsViewModel` lista por fecha y calcula totales; `MealDetailViewModel` muestra y elimina (con confirmación en la pantalla).

## ViewModel y estado

Todos los ViewModels exponen `StateFlow` con `stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), …)`. El ViewModel sobrevive a la rotación y el Flow sigue compartido, así girar el teléfono no repite consultas ni pierde lo cargado. Las pantallas lo observan con `collectAsStateWithLifecycle()`.

## Hilt

- `@HiltAndroidApp` en `VoraApplication` crea el contenedor.
- `@AndroidEntryPoint` en `MainActivity` habilita la inyección en la actividad y en Compose.
- `NetworkModule` provee `OkHttpClient`, `Retrofit` y `WorkoutsApi`; `DatabaseModule` provee `AppDatabase` y los DAO. Retrofit y la base son `@Singleton`.
- Repositorios y ViewModels se construyen con `@Inject constructor`; los ViewModels llevan `@HiltViewModel` y se piden en la pantalla con `hiltViewModel()`.
- Dagger genera en compilación el código que crea y conecta los objetos. Hilt es una capa sobre Dagger con los componentes de Android ya definidos, así no hay que armar el grafo a mano.

## Tests

- `MealLogicTest`: validación del formulario y totales.
- `WorkoutsRepositoryTest`: con API y DAO falsos, verifica que una respuesta válida reemplaza la caché y que cualquier fallo no escribe en Room.

Corren en JVM: `./gradlew :app:testDebugUnitTest`.

## Decisiones

- La UI lee de Room y la API solo refresca: una sola fuente de verdad, y funciona sin conexión.
- `Result` en vez de excepciones en el repositorio: el ViewModel decide cómo mostrar el fallo.
- `SimpleDateFormat` en vez de `java.time` porque `minSdk` es 24.
- `fallbackToDestructiveMigration()` es solo para desarrollo; está declarado como limitación.
- Reglas de R8 en `keepRules/` para que Gson pueda leer los DTO por reflexión en release.
