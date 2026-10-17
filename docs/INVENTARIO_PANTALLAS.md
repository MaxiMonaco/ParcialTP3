# Inventario de pantallas — VORA Coach

Referencia de cobertura (NAV01). Los nombres son los frames reales del Figma
(`Parcial-Domiciliario-7-Oct-26`). **Estado** = nivel de implementación del servicio detrás de la
pantalla: *Funcional* (Retrofit / Room) o *Demostración / Simulado* (datos de ejemplo).

Columna **UI** = estado de la maquetación: ⬜ pantalla provisoria, ✅ maquetada según Figma (con datos de ejemplo fijos).

Rutas: `navigation/AppDestination.kt` · Grafos: `navigation/*Graph.kt` · Pantallas: `ui/<flujo>/`.

| # | Flujo | Pantalla (Figma) | Ruta | Estado | UI |
|---|---|---|---|---|---|
| 1 | Login | Login - 01 SignIn | `login/01_signin` | Demostración (sin Firebase) | ✅ |
| 2 | Login | Login - 02 SignUp | `login/02_signup` | Demostración (sin Firebase) | ✅ |
| 3 | Home | Home / 01 — Dashboard | `home/01_dashboard` | Demostración | ✅ |
| 4 | Home | Home / 02 — Notifications | `home/02_notifications` | Demostración | ✅ |
| 5 | AI Voice | Voice / 01 — Idle | `voice/01_idle` | Simulado | ✅ |
| 6 | AI Voice | Voice / 02 — Listening | `voice/02_listening` | Simulado | ✅ |
| 7 | AI Voice | Voice / 03 — Processing | `voice/03_processing` | Simulado | ✅ |
| 8 | AI Voice | Voice / 04 — Speaking | `voice/04_speaking` | Simulado | ✅ |
| 9 | AI Voice | Voice / 05 — Conversation | `voice/05_conversation` | Simulado | ✅ |
| 10 | AI Voice | Voice / 06 — Error | `voice/06_error` | Simulado | ✅ |
| 11 | Workout | Workout / 01 — Dashboard | `workout/01_dashboard` | Funcional: Retrofit + Room | ✅ |
| 12 | Workout | Workout / 02 — Weekly Plan | `workout/02_weekly_plan` | Demostración | ✅ |
| 13 | Workout | Workout / 03 — Workout Details | `workout/03_details/{workoutId}` | Funcional: Retrofit + Room | ✅ |
| 14 | Workout | Workout / 04 — Pre Workout | `workout/04_pre/{workoutId}` | Demostración | ✅ |
| 15 | Workout | Workout / 05 — Active Workout | `workout/05_active/{workoutId}` | Simulado (cronómetro, series, volumen) | ✅ |
| 16 | Workout | Workout / 06 — Exercise Details | `workout/06_exercise/{exerciseId}` | Funcional: Retrofit + Room | ✅ |
| 17 | Workout | Workout / 07 — Workout Complete | `workout/07_complete` | Simulado | ✅ |
| 18 | Nutrition | Nutrition / 01 — Dashboard | `nutrition/01_dashboard` | Funcional: Room | ✅ |
| 19 | Nutrition | Nutrition / 02 — Today's Meals | `nutrition/02_todays_meals` | Funcional: Room | ✅ |
| 20 | Nutrition | Nutrition / 03 — Add Food | `nutrition/03_add_food` | Funcional: Room (carga manual) | ✅ |
| 21 | Nutrition | Nutrition / 04 — Voice Food Logging | `nutrition/04_voice_logging` | Simulado | ✅ |
| 22 | Nutrition | Nutrition / 05 — Food Analysis | `nutrition/05_food_analysis` | Simulado | ✅ |
| 23 | Nutrition | Nutrition / 06 — Photo Analysis | `nutrition/06_photo_analysis` | Simulado | ✅ |
| 24 | Nutrition | Nutrition / 07 — Food Confirmation | `nutrition/07_food_confirmation` | Funcional: Room (guarda) | ✅ |
| 25 | Nutrition | Nutrition / 08 — Meal Details | `nutrition/08_meal/{mealId}` | Funcional: Room (eliminar) | ✅ |
| 26 | Progress | Progress / 01 — Today's Overview | `progress/01_todays_overview` | Demostración | ✅ |
| 27 | Progress | Progress / 02 — Dashboard | `progress/02_dashboard` | Demostración | ✅ |
| 28 | Progress | Progress / 03 — Workout Progress | `progress/03_workout_progress` | Demostración | ✅ |
| 29 | Progress | Progress / 04 — Nutrition Progress | `progress/04_nutrition_progress` | Demostración | ✅ |
| 30 | Progress | Progress / 05 — Health Trends | `progress/05_health_trends` | Demostración | ✅ |
| 31 | Progress | Progress / 06 — Weekly Review | `progress/06_weekly_review` | Demostración | ✅ |
| 32 | Profile | Profile / 01 — Dashboard | `profile/01_dashboard` | Datos locales | ✅ |
| 33 | Profile | Profile / 02 — Edit Profile | `profile/02_edit_profile` | Datos locales | ✅ |
| 34 | Profile | Profile / 03 — Goals & Preferences | `profile/03_goals_preferences` | Datos locales | ✅ |
| 35 | Profile | Profile / 04 — Nutrition Preferences | `profile/04_nutrition_preferences` | Datos locales | ✅ |
| 36 | Profile | Profile / 05 — Voice & AI Settings | `profile/05_voice_ai_settings` | Datos locales | ✅ |
| 37 | Profile | Profile / 06 — Settings | `profile/06_settings` | Datos locales | ✅ |
| — | Nutrition | *(extra)* Nutrition / 03b — Manual | `nutrition/03b_manual_food` | Funcional: Room | ✅ |

Las 37 filas numeradas son los frames de Figma. La fila "extra" **no está en el diseño**: se agregó
porque "Log Manually" en *Add Food* necesita un formulario y la carga manual es obligatoria (RF03).

## Camino de navegación

- **Barra inferior** (Home · Workout · Nutrition · Progress · Profile): visible solo en las pantallas
  donde Figma la muestra (`AppDestination.withBottomBar`). Atrás desde una pestaña vuelve a Home.
- **Login → Home:** "Sign In" / "Sign Up" limpian la pila; atrás ya no vuelve al login.
  **Profile / 06 → Log out** limpia todo y vuelve a *Login - 01 SignIn*.
- **Asistente (AI Voice)** no es una pestaña: se abre desde Home ("Start voice session"),
  Workout (Weekly Plan, Pre Workout) y Progress (Weekly Review). Atrás vuelve a quien lo abrió.
  *Cancel* y *Try again* regresan a *Voice / 01 Idle*.
- **Entrenamientos:** 01 → 03 (detalle) → 04 (preparación) → 05 (activo) → 07 (completado) → *Done* vuelve
  al Dashboard. Cada entrenamiento y cada ejercicio viajan por id en la ruta (NAV04).
- **Comidas:** voz (04 → 05 → 07), foto (06 → 07) o manual (03b). Al confirmar se vuelve al Dashboard.

## Supuestos (el Figma no los define)

1. **Progress / 01 Today's Overview** es la raíz de la pestaña Progress; el acceso a **Progress / 02 Dashboard**
   es un botón agregado.
2. **Profile / 05 Voice & AI Settings** se abre desde *Profile / 06 Settings* (en el diseño no hay botón que lo abra).
3. **Voice / 06 Error** se alcanza con un acceso de demostración desde *Voice / 01 Idle*.
4. Los controles sin pantalla propia (Search Database, Units, Appearance,
   Connected Data, Allow Access / Not Now) se resuelven como diálogo o ajuste dentro de la vista, o se
   marcan como demostración. No se piden permisos de micrófono ni cámara.
