# Plan de pruebas — VORA Coach (TP3)

Responsable de verificación: Santiago Schmale. Fecha de corte: 28/10/2026.
Cada caso se corresponde con un criterio de la consigna (diapositivas 5, 6, 9, 10, 13 y 15).
**Estado:** ✅ pasa · ❌ falla · ⬜ no probado · 🟡 pasa con observación. Toda falla genera un ítem en "Hallazgos".

Entorno de prueba: dispositivo físico Android, servidor `https://tp3.martinrivas.ar/`, build `assembleDebug` de la rama indicada en cada corrida.

| Corrida | Fecha | Rama / commit | Dispositivo | Android |
|---|---|---|---|---|
| 1 | | | | |

---

## A. Demostración final (diapositiva 15) — lo que el profesor va a hacer
| # | Caso | Pasos | Resultado esperado | Estado | Evidencia |
|---|---|---|---|---|---|
| A1 | Recorrido completo | Abrir la app y pasar por las 37 pantallas del inventario usando botones y barra inferior | Todas alcanzables; ninguna pantalla en blanco ni crash | ⬜ | |
| A2 | Catálogo remoto | Entrar a Workout con conexión | Se ven los 3 entrenamientos (Fuerza inicial, Entrenamiento completo, Fuerza y constancia) con nivel y duración | ⬜ | |
| A3 | Dos detalles distintos | Abrir w01 y luego w02 | Cada uno muestra su nombre y sus 3 ejercicios con series (3), reps (10), descanso (60 s) e instrucciones | ⬜ | |
| A4 | Alta de comida | Nutrition → Add Food → Log manually → completar → guardar | Aparece en el listado del día y el total diario sube | ⬜ | |
| A5 | Persistencia | Cerrar la app por completo (quitar de recientes) y reabrir | La comida sigue en el listado con el mismo total | ⬜ | |
| A6 | Baja de comida | Abrir el detalle de la comida → Delete | Pide confirmación; al confirmar desaparece y el total baja | ⬜ | |
| A7 | Detalle sin conexión | Con w01 ya abierto antes, apagar Wi-Fi y datos, volver a abrir w01 | Se ve desde Room, con aviso "sin conexión" | ⬜ | |
| A8 | Detalle nunca descargado sin conexión | Sin conexión, abrir w03 (nunca abierto antes) | Mensaje "no disponible sin conexión", sin crash | ⬜ | |
| A9 | Lista vacía | Escenario `empty` (ver sección E) | Pantalla de estado vacío con botón reintentar | ⬜ | |
| A10 | Error de servidor | Escenario `error` (503) | Pantalla de error con reintentar; si había caché, se muestra la caché con aviso | ⬜ | |
| A11 | Reintento | Desde A9/A10, restablecer conexión y tocar Reintentar | Vuelve a cargar el contenido | ⬜ | |
| A12 | Commits por integrante | `git shortlog -sn` en main | Cada integrante con al menos un commit significativo | ⬜ | |

## B. Retrofit (RF01, RF05) — 12 pts
| # | Caso | Resultado esperado | Estado | Evidencia |
|---|---|---|---|---|
| B1 | Carga inicial del catálogo muestra indicador de carga antes del contenido | Spinner/placeholder visible, luego lista | ⬜ | |
| B2 | Rotar el teléfono en el catálogo | No se vuelve a consultar la API (sin parpadeo de carga) y la lista sigue | ⬜ | |
| B3 | JSON inválido (escenario `malformed`) | Error "respuesta no válida"; la caché previa no se borra | ⬜ | |
| B4 | Demora de 3 s (`X-Mock-Delay-Ms: 3000`) | Carga visible 3 s; no hay doble pedido | ⬜ | |
| B5 | Id inexistente (`/v1/workouts/zzz`) | Error 404 controlado, sin crash | ⬜ | |

## C. Room (RF02, RF03, RF04) — 12 pts
| # | Caso | Resultado esperado | Estado | Evidencia |
|---|---|---|---|---|
| C1 | Nombre vacío en el formulario | Mensaje de error en el campo, no guarda | ⬜ | |
| C2 | Calorías negativas | Mensaje "no puede ser negativo", no guarda | ⬜ | |
| C3 | Valor con coma decimal (p. ej. 12,5) | Se acepta | ⬜ | |
| C4 | Fecha inválida (2026-02-31) | Mensaje de fecha inválida | ⬜ | |
| C5 | Cuatro tipos de comida | Desayuno, almuerzo, merienda y cena seleccionables y se muestran bien en el listado | ⬜ | |
| C6 | Listado por fecha | Cambiar de día muestra solo las comidas de ese día | ⬜ | |
| C7 | Total diario | Suma correcta de kcal/prot/carb/grasa (verificar a mano con 2 comidas) | ⬜ | |
| C8 | Caché de catálogo tras reinstalar | Tras `installDebug` nuevo, la caché puede estar vacía: primera carga desde API funciona | ⬜ | |

## D. Fidelidad con Figma (UI01–UI03) — 40 pts
Referencia: prototipo navegable
`https://www.figma.com/proto/1nvbbfYNYmKAevuaG28J85/Parcial-Domiciliario-7-Oct-26?node-id=1-673&p=f&scaling=min-zoom&content-scaling=fixed&page-id=0%3A1`

Para cada pantalla del inventario, comparar lado a lado y anotar diferencias. Capturar las 5 de referencia (diapositiva 7) para `docs/comparaciones/`.

| # | Pantalla | Colores / tipografía | Espaciados / componentes | Íconos / imágenes | Textos cortados o superpuestos | Estado | Diferencias |
|---|---|---|---|---|---|---|---|
| D1 | Home / 01 — Dashboard | | | | | ⬜ | |
| D2 | Workout / 01 — Dashboard | | | | | ⬜ | |
| D3 | Nutrition / 01 — Dashboard | | | | | ⬜ | |
| D4 | Progress / 01 — Today's Overview | | | | | ⬜ | |
| D5 | Profile / 01 — Dashboard | | | | | ⬜ | |
| D6 | Voice / 01 — Idle | | | | | ⬜ | |
| D7 | Resto del inventario (anotar solo las que tengan diferencias notorias) | | | | | ⬜ | |
| D8 | UI03: probar en un segundo tamaño (otro celular o emulador) — contenido que no entra se desplaza | | | | | ⬜ | |

## E. Navegación (NAV01–NAV05) — 25 pts
| # | Caso | Resultado esperado | Estado | Evidencia |
|---|---|---|---|---|
| E1 | Inventario del README vs. prototipo Figma | Mismas pantallas y nombres; aclarar los 41 frames del prototipo vs. 37 del inventario | ⬜ | |
| E2 | Botón atrás del sistema en cada pestaña | Desde una pestaña vuelve a Home; desde Home sale de la app | ⬜ | |
| E3 | Login → Home | Atrás no vuelve al login | ⬜ | |
| E4 | Logout desde Settings | Vuelve a Sign In y atrás no entra de nuevo | ⬜ | |
| E5 | Volver / cerrar / cancelar en cada pantalla que los tenga | Destino coherente con Figma | ⬜ | |
| E6 | Cada entrenamiento abre su propio detalle (NAV04) | w01 ≠ w02 ≠ w03 en nombre y ejercicios | ⬜ | |
| E7 | Cada ejercicio abre su propia guía | Nombre e instrucciones del ejercicio elegido | ⬜ | |
| E8 | Controles sin pantalla (editar, duplicar, buscar…) | Diálogo de demostración, nunca un toque sin efecto | ⬜ | |
| E9 | Coach: los 6 estados alcanzables y sin pedir micrófono | Idle → Listening → Processing → Speaking → Conversation; Error accesible; sin permiso del sistema | ⬜ | |
| E10 | Foto / voz en Nutrition | Simulado sin pedir cámara ni micrófono | ⬜ | |
| E11 | Accesos directos de Home y Progress a un entrenamiento | Abren w01 (id fijo); verificar que no rompan | ⬜ | |

## F. Entrega (diapositiva 16) y código
| # | Caso | Estado | Evidencia |
|---|---|---|---|
| F1 | El proyecto compila limpio desde un clon nuevo (`gradlew assembleDebug`) | ⬜ | |
| F2 | `gradlew :app:testDebugUnitTest` en verde (MealLogicTest, WorkoutsRepositoryTest) | ⬜ | |
| F3 | README: integrantes completos y correctos | ⬜ | |
| F4 | README: link al Figma, inventario, estado por funcionalidad, servidor y URL | ⬜ | |
| F5 | README: link al video de demostración | ⬜ | |
| F6 | README: 5 comparaciones visuales | ⬜ | |
| F7 | README: declaración de uso de IA por integrante | ⬜ | |
| F8 | Sin código del TP anterior (Quotes/Favorites) | ⬜ | |
| F9 | Sin claves ni `google-services.json` en el repo | ⬜ | |
| F10 | Tag `entrega-tp3` sobre el commit final de main | ⬜ | |

## G. Cómo forzar escenarios de la API
El servidor acepta los headers `X-Mock-Scenario` (`normal`, `empty`, `error`, `malformed`) y `X-Mock-Delay-Ms`.
La app no los envía todavía. Opciones para probar B3, B4, A9, A10:
1. Apagar Wi-Fi y datos (cubre "sin conexión", no cubre `empty` ni `malformed`).
2. Agregar en debug un interceptor de OkHttp que lea el escenario de una preferencia (pendiente, ver rama `santiago`).
3. Temporalmente, en `NetworkModule`, `addInterceptor { chain -> chain.proceed(chain.request().newBuilder().addHeader("X-Mock-Scenario", "empty").build()) }` y recompilar (no commitear).

---

## Hallazgos
| # | Caso | Severidad (alta/media/baja) | Descripción | Asignado a | Estado |
|---|---|---|---|---|---|
| H1 | F3 | media | Tabla de integrantes incompleta; un apellido incorrecto | Santiago | abierto |
| H2 | F5/F6 | alta | Video y comparaciones visuales no existen (condición de entrega) | Sofía | abierto |
| H3 | E11 | baja | Accesos directos usan id fijo `w01`; si el servidor cambia ids, dejan de abrir | — | abierto |
| H4 | A3 | — | DTO de ejercicios verificado contra la colección Postman: coincide (`id, name, sets, reps, restSeconds, instructions`). `muscleGroup` y `tip` no se usan | — | cerrado |
| H5 | F8 | baja | Código del TP anterior en el repo | Santiago | en rama `santiago` |
