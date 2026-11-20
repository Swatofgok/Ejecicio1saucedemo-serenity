# CLAUDE.md — Proyecto de automatización QA (Java)

> Reglas para Claude en este repositorio.

## 1. Contexto del proyecto

- **Objetivo:** prueba E2E de UI del flujo de compra de https://www.saucedemo.com/ (login → carrito → checkout → confirmación).
- **Lenguaje:** Java 17+ (se ejecuta con JDK 21).
- **Build:** Maven mediante Maven Wrapper (`mvnw` / `mvnw.cmd`); no requiere Maven instalado.
- **UI E2E:** Serenity BDD + Screenplay + Cucumber + JUnit 5 (Selenium/Chrome).
- **Reportes:** Serenity reports en `target/site/serenity/index.html`.
- **Fuera de alcance:** pruebas de API (SauceDemo no expone API pública).

## 2. Arquitectura

Arquitectura **Screenplay por capas, separando prueba de implementación**. Cada capa solo conoce a la inmediatamente inferior.

```
src/test/
├── java/com/saucedemo/
│   ├── runners/        # Clases ejecutoras (Cucumber/JUnit). Sin lógica.
│   ├── stepdefinitions/# Glue de Cucumber. Solo orquestan Tasks/Questions.
│   ├── tasks/          # Acciones de negocio ("Login", "AddRandomProductsToCart")
│   ├── interactions/   # Acciones técnicas reutilizables (solo si Serenity no las trae)
│   ├── questions/      # Consultas del estado de la UI para validar
│   ├── userinterfaces/ # Locators (Target) agrupados por pantalla
│   ├── models/         # Datos de prueba tipados (records)
│   └── utils/          # Helpers genéricos (config, azar con semilla)
└── resources/
    ├── features/       # Archivos .feature (Gherkin, en español) por funcionalidad
    ├── data/           # Datos de prueba (json/csv), nunca credenciales reales
    └── serenity.conf   # Configuración de navegador, timeouts, entornos y URLs
```

**Reglas de dependencia**
- `stepdefinitions` → `tasks`/`questions` → `interactions` → `userinterfaces`. Nunca al revés.
- Los `stepdefinitions` **no** contienen locators ni esperas.
- Los locators viven **solo** en `userinterfaces`.
- Los datos de prueba no se escriben dentro de los steps: van en `models`/`data`/`serenity.conf`.
- Si una decisión no está cubierta aquí, sigue el patrón del código existente y pregunta antes de inventar una estructura nueva.

## 3. Convenciones de código

- Clases en `PascalCase`, métodos y variables en `camelCase`, constantes en `UPPER_SNAKE_CASE`.
- Paquetes en minúscula, sin guiones bajos.
- Nombres de código y de clases en **inglés**; el Gherkin y los textos del reporte de negocio en español.
- Tasks con nombre de acción: `Login`, `AddRandomProductsToCart`.
- Questions con nombre descriptivo: `CartBadgeCount`, `OrderSummary`.
- Métodos cortos (≲ 20 líneas); una responsabilidad por clase.
- Sin `Thread.sleep`. Usar esperas explícitas/condicionales (Serenity `WaitUntil` o equivalentes).
- Sin números ni textos mágicos: extraer a constantes o a `serenity.conf`.
- Locators estables: preferir `id`, `data-test` o `name`; evitar XPath absoluto y selectores frágiles por posición.
- Sin código comentado ni imports sin usar. Los comentarios explican el *porqué*, no el *qué*.
- Preferir inmutabilidad (`final`, `record`) en modelos de datos.
- No usar Lombok (no está en el proyecto).

## 4. Gherkin y escenarios

- Un `.feature` por funcionalidad; un escenario = un comportamiento verificable.
- Estilo declarativo (qué hace el usuario), no imperativo (clic en X, escribir en Y).
- Escenarios independientes entre sí: no dependen del orden de ejecución.
- Etiquetas obligatorias en cada escenario: nivel (`@smoke` y/o `@regression`) + tipo (`@ui`) + funcionalidad (`@compra`, …).
- Excepción: un escenario que demuestra un defecto conocido del sitio lleva `@defecto-conocido` **en lugar** del nivel. Falla a propósito y queda excluido de la corrida normal (`junit-platform.properties`).
- Usar `Esquema del escenario` + `Ejemplos` para variaciones de datos.
- Un escenario debe tener al menos una aserción real; nada de pasos que solo "navegan".

## 5. Testing y calidad

- Las pruebas deben ser **reproducibles**: sin dependencia de hora, orden o datos residuales.
- Datos aleatorios permitidos **solo** con semilla: la semilla se registra en el reporte y `-Dseed=<n>` reproduce exactamente la misma corrida.
- Cada prueba crea y limpia sus propios datos, o usa datos únicos.
- Aserciones explícitas con mensaje claro de fallo (`.as(...)`, `because(...)` o mensajes descriptivos).
- No ocultar fallos: prohibido `try/catch` vacío o que silencie errores del test.
- Defectos del sitio bajo prueba que no impiden validar el comportamiento se registran como evidencia en el reporte Serenity (`Serenity.recordReportData()`), no solo en el log.
- Validar en UI: estado visible al usuario, no detalles internos del DOM.
- Pruebas flaky: no se reintentan a ciegas; se diagnostica la causa y se corrige.
- Un escenario que "pasa" sin ejecutar sus pasos (duración ~0 s) es un fallo de la suite, no un éxito.
- Antes de dar una tarea por terminada: ejecutar las pruebas afectadas y confirmar que pasan.

## 6. Comandos habituales

```bash
./mvnw clean verify                                       # Toda la suite + reporte Serenity (Windows: mvnw.cmd)
./mvnw clean verify -Dcucumber.filter.tags="@smoke"       # Solo smoke
./mvnw clean verify -Denvironment=headless                # Sin ventana de Chrome (CI)
./mvnw clean verify -Dseed=12345                          # Repetir una corrida aleatoria concreta
./mvnw clean verify -Dcucumber.filter.tags="@defecto-conocido"   # Demostrar defectos conocidos (fallan)
```

Reporte: `target/site/serenity/index.html`. Los reportes de entrega se copian a `reportes/` (versionado), que el enunciado exige.
El enunciado exige además los nombres exactos `readme.txt` y `conclusiones.txt`: no renombrarlos.

## 7. Git y commits

- Ramas en inglés: `feature/<short-description>`, `fix/<short-description>`, `chore/<short-description>`, `docs/<short-description>`.
- Commits con **Conventional Commits + Gitmoji**, siempre **en inglés**:

  ```
  <type>(<scope>): <emoji> <imperative description>

  <optional body: what and why, not how>
  ```

  | Emoji | Tipo | Uso |
  |---|---|---|
  | 🎉 | `chore` | Inicio del repositorio |
  | ✨ | `feat` | Nueva capacidad de automatización (tasks, questions, UI) |
  | ✅ | `test` | Escenarios, features y step definitions |
  | 🐛 | `fix` | Corrección de un fallo de la suite |
  | ♻️ | `refactor` | Cambio de estructura sin cambiar comportamiento |
  | ⬆️ | `build` | Dependencias y versiones (`pom.xml`) |
  | 📦 | `build` | Herramientas de build (Maven Wrapper, plugins) |
  | 🔧 | `chore` | Configuración (`serenity.conf`, properties, logging) |
  | 🙈 | `chore` | `.gitignore` / `.gitattributes` |
  | 📝 | `docs` | Documentación (`readme.txt`, `conclusiones.txt`, `CLAUDE.md`) |
  | 📊 | `docs` | Reportes de ejecución versionados (`reportes/`) |
  | 🔀 | `merge` | Merge de una rama |

  Alcances habituales: `purchase`, `screenplay`, `config`, `deps`, `wrapper`, `readme`, `conclusions`, `reports`.
  Ejemplo: `fix(config): 🐛 disable Chrome password leak warning`.
- **Nunca** incluir autoría de IA en commits ni PRs: sin `Co-Authored-By` de Claude, sin "Generated with Claude Code" ni similares. El único autor es el dueño del repositorio.
- Mensaje en imperativo, primera línea ≤ 72 caracteres.
- Commits pequeños y atómicos; un cambio lógico por commit.
- No hacer commit directo a `main` (única excepción: el commit 🎉 inicial). Con remoto, todo cambio entra por Pull Request usando `.github/pull_request_template.md`; sin remoto, por merge `--no-ff` de una rama local.
- Antes de abrir un PR: `./mvnw clean verify` en verde y, si cambia el comportamiento, actualizar `reportes/`.
- No hacer `push --force` ni reescribir historial compartido sin que yo lo pida.
- No hacer commit ni push a menos que yo lo solicite explícitamente.

## 8. Seguridad y límites

**Nunca**
- Escribir credenciales reales, tokens, API keys o URLs internas en el código ni en archivos versionados.
- Leer o modificar archivos `.env`, `*.pem`, `*.key` o similares.
- Ejecutar comandos destructivos (`rm -rf`, `git reset --hard`, `git clean -fd`) sin confirmación.
- Apuntar las pruebas a entornos de producción.
- Instalar o cambiar versiones de dependencias sin avisar y justificar.

**Siempre**
- URLs y credenciales por entorno en `serenity.conf`, sobreescribibles con variables de entorno. Las credenciales **públicas de demo** de SauceDemo (`standard_user`) pueden ir en `serenity.conf`; nunca en `.feature` ni en código.
- Mantener `.gitignore` con `target/`, `.idea/`, `*.log` y reportes generados.
- Pedir confirmación antes de borrar archivos, renombrar paquetes o hacer refactors grandes.

## 9. Cómo quiero que trabajes

- Para cambios grandes: primero propón un plan breve y espera mi visto bueno.
- Antes de crear una clase nueva, busca si ya existe una equivalente que reutilizar.
- Haz cambios mínimos y enfocados; no refactorices código ajeno a la tarea.
- Si algo es ambiguo, pregunta; no asumas.
- Responde en **español**. Al terminar, resume en pocas líneas qué cambiaste y qué verificaste.
