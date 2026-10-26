# Arquitectura de VORA Coach, explicada para defender código ajeno

Escrito leyendo el código real de la rama `santiago` (26/10/2026). Cada sección dice **qué archivo**, **qué hace**, **por qué está así** y **qué decir si preguntan**. Pensado para alguien que viene de Node/React.

---

## 0. El mapa en una imagen

```
Pantalla (Compose)  ──observa──▶  ViewModel  ──pide──▶  Repositorio  ──┬──▶  Retrofit (API)   ──▶  internet
   ui/<flujo>/                    viewmodel/              data/repository/ │     data/remote/
                                                                           └──▶  Room (SQLite)    ──▶  teléfono
                                                                                 data/local/
Hilt (di/) construye y conecta todas las cajas. Navigation (navigation/) decide qué pantalla se ve.
```

Regla de oro que atraviesa todo: **la pantalla nunca llama a la API ni a la base. Solo observa al ViewModel.** El repositorio es el único que sabe que existen Retrofit y Room.

Equivalencia web: pantalla = componente React; ViewModel = custom hook/store; repositorio = capa `services/`; Retrofit = axios; Room = Mongoose; Hilt = el `require` que te pasa las instancias ya armadas.

---

## 1. Punto de entrada

### `VoraApplication.kt`
```kotlin
@HiltAndroidApp
class VoraApplication : Application()
```
Una línea. `@HiltAndroidApp` le dice a Hilt "acá nace el contenedor de dependencias de toda la app". Sin esto, nada se inyecta. Está registrada en `AndroidManifest.xml` con `android:name=".VoraApplication"`.

### `MainActivity.kt`
```kotlin
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    setContent { ParcialTP3Theme { Scaffold { AppNavHost(...) } } }
}
```
Única Activity de la app. `@AndroidEntryPoint` habilita la inyección desde acá hacia abajo (incluidos los ViewModels de Compose). Solo pone el tema y el grafo de navegación. **Si preguntan "¿cuántas Activities tiene?": una. Todo lo demás son composables.**

---

## 2. Navegación (`navigation/`)

### `AppDestination.kt` — todas las rutas
Una `sealed class` con un `data object` por pantalla, cada uno con su `route` (string). Los nombres siguen a Figma: `"Workout / 03 — Workout Details"` → `workout/03_details/{workoutId}`.

- 7 objetos `*Flow` son **grafos**, no pantallas: contenedores por pestaña.
- Las 5 rutas con argumento (`WorkoutDetails`, `PreWorkout`, `ActiveWorkout`, `ExerciseDetails`, `MealDetails`) tienen `createRoute(id)` que arma la URL con el id codificado. Así cada entrenamiento abre **su** detalle (NAV04).
- `NavArgs` guarda los nombres de los argumentos (`workoutId`, `exerciseId`, `mealId`) para que ruta y ViewModel usen la misma clave.
- `withBottomBar` es el conjunto de rutas donde se muestra la barra inferior (solo donde Figma la dibuja).

### `AppNavHost.kt` — la raíz
Un `Scaffold` con la barra inferior (`VoraBottomBar`, visible solo si la ruta actual está en `withBottomBar`) y un `NavHost` que arranca en `LoginFlow` y registra los 7 grafos: `loginGraph`, `homeGraph`, `voiceGraph`, `workoutGraph`, `nutritionGraph`, `progressGraph`, `profileGraph`.

### `*Graph.kt` — un archivo por flujo
Cada uno es una función de extensión `NavGraphBuilder.xxxGraph(navController)` que declara `navigation(startDestination, route = XxxFlow.route) { composable(ruta) { Pantalla(...) } }`. **Las pantallas reciben lambdas (`onBackClick`, `onStartWorkoutClick`), nunca el `NavController`.** El grafo es el único que navega. Esto mantiene a las pantallas testeables y reutilizables.

### `NavExtensions.kt` — las reglas de la pila
Cinco funciones que concentran el comportamiento del botón atrás:
- `navigateToTab`: cambiar pestaña guarda el estado de la que deja (`saveState`) y restaura la nueva (`restoreState`); siempre se apila sobre Home, así atrás vuelve a Home (NAV02).
- `navigateToTabRoot`: volver a la raíz de una pestaña descartando lo de arriba (al terminar un entrenamiento o confirmar una comida).
- `navigateToHomeClearingBackStack`: tras login, limpia toda la pila (`popUpTo(graph.id) { inclusive = true }`): atrás no vuelve al login.
- `navigateToLoginClearingBackStack`: logout, lo inverso.
- `returnToVoiceIdle`: vuelve al estado Idle del asistente si está en la pila; si no, lo abre.

### `VoraBottomBar.kt`
Un solo componente para las 5 pestañas (Home, Workout, Nutrition, Progress, Profile). Resalta la pestaña según la ruta actual.

### `SampleIds.kt`
Ids fijos (`w01`, `w02`) para los accesos directos de Home y Progress que abren un entrenamiento sin pasar por el catálogo. Limitación declarada en el README (hallazgo H3).

**Si preguntan "¿cómo evitan que el botón atrás haga cosas raras?":** "Cada flujo es un grafo anidado con su `startDestination`; cambiar de pestaña hace `popUpTo(Home)` con `saveState`, y login/logout limpian la pila entera. Las reglas están centralizadas en `NavExtensions.kt`."

---

## 3. Capa de red (`data/remote/`)

### `WorkoutsApi.kt` — la interfaz de Retrofit
```kotlin
interface WorkoutsApi {
    @GET("v1/workouts")               suspend fun getWorkouts(@Query("level") level: String? = null): ApiResponse<List<WorkoutDto>>
    @GET("v1/workouts/{workoutId}")   suspend fun getWorkout(@Path("workoutId") workoutId: String): ApiResponse<WorkoutDto>
}
```
Retrofit genera la implementación en tiempo de ejecución. `suspend` = `async`. Solo los dos endpoints obligatorios. `level` se acepta pero hoy nadie lo manda (mejora posible).

### `ApiResponse.kt` — el sobre
```kotlin
data class ApiResponse<T>(val data: T?)
```
La API devuelve todo envuelto en `{ "data": … }`. `data` es nullable a propósito: Gson no respeta los tipos no-nulos de Kotlin, así que si el servidor manda basura, `data == null` y el repositorio lo detecta en vez de explotar después.

### `WorkoutDto.kt` — el calco del JSON
`WorkoutDto(id, name, level, durationMinutes, exerciseCount, exercises: List<ExerciseDto>?)` y `ExerciseDto(id, name, sets, reps, restSeconds, instructions)`. Todos los campos nullable (defensa contra JSON incompleto). `ExerciseDto` usa `@SerializedName(alternate = [...])` y `JsonElement` para tolerar variantes de nombre y tipo (p. ej. `reps` como `10` o `"8-12"`). **Verificado contra Postman: los nombres reales coinciden con los principales.**

### `JsonParsing.kt`
Tres extensiones (`asTextOrNull`, `asIntOrNull`, `asTextListOrEmpty`) que convierten `JsonElement` a texto/entero/lista de forma tolerante. Existen por lo anterior.

**Si preguntan "¿por qué DTO y no usar el modelo directo?":** "Porque el DTO es la forma del JSON y el modelo es la forma que necesita la app. Si la API cambia un nombre, toco el DTO y el mapper; las pantallas no se enteran. Y los campos del DTO son nullable porque Gson no garantiza nada; el modelo ya está limpio."

---

## 4. Capa local (`data/local/`)

### `AppDatabase.kt`
```kotlin
@Database(entities = [WorkoutEntity, WorkoutDetailEntity, ExerciseEntity, MealEntity], version = 3)
abstract class AppDatabase : RoomDatabase() { workoutDao(); mealDao() }
```
Room genera `AppDatabase_Impl` con KSP en compilación. Versión 3 porque se quitó la tabla heredada; `fallbackToDestructiveMigration()` en el módulo recrea la base si cambia el esquema (solo desarrollo, declarado como limitación).

### `WorkoutEntities.kt` — tres tablas + una relación
- `WorkoutEntity` (tabla `workouts`): fila del catálogo, con `position` para conservar el orden de la API.
- `WorkoutDetailEntity` (tabla `workout_details`): **que exista esta fila significa "este detalle ya se descargó"**. Es la clave de RF02: permite verlo sin conexión y avisar cuando nunca se descargó. Tiene `cachedAt`.
- `ExerciseEntity` (tabla `exercises`): un ejercicio de un detalle. Su `id` se arma como `"$workoutId:$exerciseId"` para que `e01` de w01 no pise `e01` de w02. Índice en `workoutId`.
- `WorkoutWithExercises`: `@Embedded` + `@Relation` para que Room devuelva detalle y ejercicios juntos.

### `WorkoutDao.kt`
Clase abstracta (no interfaz) porque Room solo permite métodos con cuerpo en clases abstractas, y acá hay dos transacciones:
- `replaceCatalog(items)` = `@Transaction { clearCatalog(); insertCatalog(items) }` — o se aplica todo o nada.
- `saveDetail(detail, exercises)` = `@Transaction { deleteExercises(id); insertDetail; insertExercises }`.
- Las lecturas devuelven `Flow` (`observeCatalog`, `observeDetail`, `observeExercise`): Room vuelve a emitir solo cuando la tabla cambia.

### `MealEntity.kt` / `MealDao.kt`
Tabla `meals` con `id` autogenerado, `name`, `type` (nombre del enum como texto), `date` como `"yyyy-MM-dd"` (ordena y filtra bien como texto; índice), macros como `Double`, `createdAt`. DAO: `insert`, `deleteById`, `observeByDate(date): Flow`, `observeById(id): Flow`.

**Si preguntan "¿por qué Flow y no una función que devuelve la lista?":** "Porque con `Flow` la UI se suscribe una vez y Room le avisa cada cambio. Al agregar o borrar una comida, el listado y el total diario se actualizan solos, sin volver a consultar. Eso es RF04."

---

## 5. Modelos y mappers (`model/`)

### `Workout.kt`
Modelos limpios (`Workout`, `Exercise`, `WorkoutDetail`, sin nulls raros) y los mappers:
- Red → Room: `WorkoutDto.toCatalogEntity(position)` (descarta items sin id), `WorkoutDto.toDetailEntity(fallbackId, now)`, `ExerciseDto.toEntity(workoutId, position)`.
- Room → app: `WorkoutEntity.toWorkout()`, `WorkoutWithExercises.toDetail()`, `ExerciseEntity.toExercise()`.

### `Meal.kt`
`Meal`, `MealType` (BREAKFAST/LUNCH/SNACK/DINNER), `DayTotals` y `List<Meal>.totals()`, mappers `toMeal()`/`toEntity()`, `MealDates` (hoy, validar, sumar días, formatear; usa `SimpleDateFormat` porque `minSdk 24` no tiene `java.time`), y la validación del formulario `validateMealForm(...)` → `MealFormErrors` (un mensaje por campo). `parseMealNumber` acepta coma o punto.

**Si preguntan "¿dónde está la validación de RF03?":** "En `model/Meal.kt`, función `validateMealForm`: nombre no vacío, fecha real, números presentes y no negativos. Es lógica pura, por eso tiene test (`MealLogicTest`) que corre en JVM."

---

## 6. Repositorios (`data/repository/`)

### `WorkoutsRepository.kt` — el corazón de RF02/RF05
```kotlin
class WorkoutsRepository @Inject constructor(private val api: WorkoutsApi, private val dao: WorkoutDao)
```
- **Lecturas**: `catalog: Flow<List<Workout>>`, `observeDetail(id): Flow<WorkoutDetail?>`, `observeExercise(id)`. **Siempre desde Room.** `null` en el detalle = nunca se descargó.
- **Escrituras**: `refreshCatalog()` y `refreshDetail(id)` devuelven `Result<Unit>`. Piden a la API; si `data` viene bien, mapean y llaman a la transacción del DAO. Si algo falla (`IOException` sin red, `HttpException` 404/503, `JsonSyntaxException` JSON roto, `data == null`), capturan y devuelven `Result.failure(e)` **sin tocar Room**. `CancellationException` se relanza (si la coroutine se cancela, hay que dejarla cancelar; es un error clásico no hacerlo).

Esto es lo que `WorkoutsRepositoryTest` verifica con API y DAO falsos: 9 casos, 0 escrituras cuando hay fallo.

### `MealsRepository.kt`
Solo Room: `observeByDate`, `observeMeal`, `add`, `delete`. Las pantallas nunca tocan el DAO.

### `RepositoryErrors.kt`
`Throwable.toUserMessage()`: `IOException` → "No hay conexión", `HttpException` → "404" o "error (código)", resto → "respuesta no válida". `Throwable.isOffline()` = es `IOException`. Así las pantallas reciben texto, no excepciones de Retrofit.

**Si preguntan "¿qué pasa exactamente cuando falla la red?":** "`refreshCatalog` captura la excepción y devuelve `Result.failure`. Como la UI lee de `dao.observeCatalog()`, que no se tocó, sigue viendo lo último válido. El ViewModel combina ese Flow con el estado del refresh y muestra los datos con un aviso."

---

## 7. ViewModels (`viewmodel/`)

Todos: `@HiltViewModel`, reciben el repositorio por `@Inject constructor`, exponen `StateFlow` con `stateIn(viewModelScope, WhileSubscribed(5_000), initial)`. Ese `stateIn` es lo que hace que la rotación no repita consultas: el ViewModel sobrevive y el Flow sigue compartido (RF07). `WhileSubscribed(5000)` mantiene la suscripción 5 s después de que la pantalla deja de observar (cubre la rotación sin reiniciar).

### `WorkoutsViewModel.kt`
Estado `sealed interface WorkoutsUiState { Loading; Empty; Error(msg); Content(workouts, isRefreshing, warning) }`. Se construye con `combine(repository.catalog, refreshState)`:
- hay datos en Room → `Content` (con `warning` si el último refresh falló);
- no hay datos y el refresh está cargando → `Loading`;
- no hay datos y falló → `Error`;
- no hay datos y terminó bien → `Empty`.
`init { load() }` dispara el refresh una vez por instancia; `retry()` lo repite.

### `WorkoutDetailViewModel.kt`
Igual, pero lee `workoutId` de `SavedStateHandle` (Hilt se lo pasa desde los argumentos de la ruta, con la clave `NavArgs.WORKOUT_ID`). Mensajes distintos según `isOffline()`: con caché "Sin conexión: mostrando datos guardados"; sin caché "no está disponible sin conexión".

### `ExerciseViewModel.kt`
Solo lee de Room (`observeExercise`): el ejercicio se guardó cuando se descargó su entrenamiento. Estados `Loading / NotFound / Content`.

### `MealsViewModel.kt`
`selectedDate` (StateFlow) → `flatMapLatest { repository.observeByDate(it) }` → `MealsUiState(date, isToday, meals, totals, isLoading)`. `previousDay()` / `nextDay()` cambian la fecha y el Flow se re-suscribe solo. El total se calcula con `meals.totals()`.

### `AddMealViewModel.kt`
`MealFormState` con todos los campos como texto y `errors`. Un `onXxxChange` por campo que además borra el error de ese campo. `save(onSaved)`: valida con `validateMealForm`; si hay errores los muestra; si no, `repository.add(meal)` y llama `onSaved` (la pantalla navega). Guarda contra doble toque con `isSaving`.

### `MealDetailViewModel.kt`
Lee `mealId` de `SavedStateHandle`, observa `repository.observeMeal(id)`, expone `delete()`.

**Si preguntan "¿qué es `stateIn` y por qué `WhileSubscribed`?":** "Convierte un Flow frío en un `StateFlow` compartido con valor actual. `WhileSubscribed(5000)` lo mantiene vivo 5 segundos sin observadores, que es más que lo que tarda una rotación: la pantalla nueva se suscribe al mismo estado sin repetir la consulta."

---

## 8. Inyección de dependencias (`di/`)

### `NetworkModule.kt`
```kotlin
@Module @InstallIn(SingletonComponent::class) object NetworkModule {
    @Provides @Singleton fun provideOkHttpClient(): OkHttpClient   // timeouts 15 s
    @Provides @Singleton fun provideRetrofit(client): Retrofit     // baseUrl + Gson
    @Provides @Singleton fun provideWorkoutsApi(retrofit): WorkoutsApi
}
```
`BASE_URL = "https://tp3.martinrivas.ar/"` (termina en `/`, obligatorio para Retrofit). Timeouts de 15 s porque el servidor puede demorar hasta 10 s con `X-Mock-Delay-Ms`.

### `DatabaseModule.kt`
Provee `AppDatabase` (`Room.databaseBuilder(context, AppDatabase::class.java, "vora.db").fallbackToDestructiveMigration()`) como `@Singleton`, y los DAO a partir de ella (sin `@Singleton`: la base ya lo es).

**Cómo encaja todo (la pregunta "expliquen Hilt"):**
1. `@HiltAndroidApp` crea el contenedor.
2. Los `@Module` enseñan a fabricar lo que no tiene constructor inyectable (Retrofit, Room, DAO).
3. `WorkoutsRepository @Inject constructor(api, dao)`: Hilt sabe fabricar `api` y `dao` por los módulos, entonces sabe fabricar el repositorio.
4. `@HiltViewModel class WorkoutsViewModel @Inject constructor(repository)`: Hilt fabrica el ViewModel con el repositorio adentro.
5. En la pantalla, `hiltViewModel()` pide ese ViewModel; Hilt lo arma o lo reutiliza.

**Hilt vs. Dagger:** Dagger es el motor que genera en compilación el código que construye y conecta objetos. Hilt es una capa encima con los componentes de Android ya definidos (`SingletonComponent` = vive toda la app, `ViewModelComponent`, etc.). Con Dagger puro habría que escribir esos componentes a mano.

---

## 9. UI (`ui/`, `shared/`)

### `ui/theme/`
`Color.kt` (paleta VORA: verde oscuro, lima, menta), `Type.kt` (Manrope en 4 pesos, `res/font/`), `Theme.kt` (`ParcialTP3Theme` con `MaterialTheme` y formas). Una corrección acá se aplica a todas las pantallas (UI02).

### `shared/VoraComponents.kt`
Librería de componentes reutilizables: `VoraScreen`, `VoraAppBar`, `VoraCard`, `VoraDarkCard`, `VoraPrimaryButton`, `VoraLimeButton`, `VoraOutlinedButton`, `VoraChip`, `VoraStatusPill`, `VoraOrb`, `VoraTitle`, `VoraOverline`, `VoraTipCard`, etc. Todas las pantallas se arman con esto.
`StateViews.kt`: `VoraLoadingBlock`, `VoraErrorBlock(msg, onRetry)`, `VoraEmptyBlock(msg, onRetry)`, `VoraWarningBlock(msg, onRetry)` — las vistas de los estados de RF05. `VoraFormField.kt`: campo de formulario con error.
`ScreenPlaceholder.kt`: pantalla provisoria usada al principio; ya no se usa en producción.

### `ui/<flujo>/*Screens.kt`
Un archivo por flujo con todas sus pantallas como `@Composable`. Patrón de cada pantalla funcional:
```kotlin
@Composable fun WorkoutDashboardScreen(onStartWorkoutClick: (String) -> Unit, ..., viewModel: WorkoutsViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    when (state) { Loading -> VoraLoadingBlock(); Error -> VoraErrorBlock(...); Empty -> ...; Content -> lista }
}
```
Las pantallas de demostración (Home, Progress, Profile, Voice) no tienen ViewModel: datos fijos de Figma. Los controles sin pantalla propia abren un diálogo de demostración (33 usos; NAV03/NAV05).

---

## 10. Tests (`app/src/test/`)
- `MealLogicTest`: validación del formulario y totales. Lógica pura.
- `WorkoutsRepositoryTest`: API y DAO falsos; respuesta válida escribe, cualquier fallo no escribe. 9 casos.
Ambos corren con `./gradlew :app:testDebugUnitTest`, sin emulador.

---

## 11. Decisiones que conviene saber defender
| Decisión | Por qué |
|---|---|
| UI lee siempre de Room | Una sola fuente de verdad; funciona sin conexión; la API solo refresca |
| `Result<Unit>` en vez de excepciones | El ViewModel decide cómo mostrar el fallo; el repositorio no sabe de UI |
| DTO con campos nullable | Gson no respeta non-null de Kotlin; mejor detectar `null` que crashear |
| `WorkoutDetailEntity` separada del catálogo | "Existe la fila" = "ya se descargó" (RF02) |
| Id de ejercicio prefijado con el del workout | Evita colisiones entre entrenamientos |
| `WhileSubscribed(5000)` | Sobrevive rotación sin repetir consulta (RF07) |
| `SimpleDateFormat` y no `java.time` | `minSdk 24`; `java.time` requiere API 26 |
| `fallbackToDestructiveMigration` | Desarrollo; declarado como limitación |
| Pantallas reciben lambdas, no `NavController` | Desacopla UI de navegación; testeable |
| Reglas R8 en `keepRules/` | Gson usa reflexión; sin esto release rompe el mapeo |
