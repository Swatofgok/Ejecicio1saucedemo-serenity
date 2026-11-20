PRUEBA E2E - FLUJO DE COMPRA EN SAUCEDEMO
Serenity BDD + Screenplay + Cucumber + JUnit 5 + Selenium (Chrome)
=============================================================================

1. DESCRIPCION
--------------
Prueba funcional automatizada de punta a punta sobre https://www.saucedemo.com/
que cubre:
  1. Autenticacion con standard_user / secret_sauce
  2. Agregar productos al carrito (2 en el caso solicitado)
  3. Visualizar el carrito
  4. Completar el formulario de compra
  5. Finalizar la compra hasta el mensaje "THANK YOU FOR YOUR ORDER"

La prueba es DINAMICA y REPRODUCIBLE:
  - Los productos se eligen AL AZAR del inventario leido en vivo desde la pagina
    (no hay nombres ni precios fijos en el codigo).
  - La cantidad de productos se parametriza en el .feature
    (Esquema del escenario con 2, 1 y 3 productos).
  - Los datos del comprador (nombre, apellido, codigo postal) se generan al azar.
  - Todo el azar sale de una SEMILLA que se muestra en el reporte. Con
    -Dseed=<numero> se repite exactamente la misma corrida.
  - Se valida que el carrito contenga exactamente los productos elegidos y que
    el total del resumen = suma de precios + impuestos.


2. REQUISITOS PREVIOS
---------------------
  - Java JDK 17 o superior          -> verificar con: java -version
  - Google Chrome (version reciente)
  - Conexion a internet (descarga Maven, dependencias y accede a saucedemo.com)
  - Git (solo para clonar el repositorio)

  NO hace falta instalar Maven: el proyecto incluye Maven Wrapper (mvnw), que
  descarga la version correcta de Maven la primera vez que se ejecuta.


3. PASO A PASO PARA EJECUTAR
----------------------------
Paso 1. Clonar el repositorio y entrar a la carpeta:

          git clone https://github.com/Swatofgok/Ejecicio1saucedemo-serenity.git
          cd Ejecicio1saucedemo-serenity

Paso 2. Ejecutar las pruebas y generar el reporte:

          Windows (CMD o PowerShell):   mvnw.cmd clean verify
          Linux / macOS:                ./mvnw clean verify

        La primera ejecucion tarda mas porque descarga Maven y las dependencias.
        Se abre Chrome y se ejecutan 3 compras (2, 1 y 3 productos).
        Al final debe aparecer:  BUILD SUCCESS
        Si algun escenario falla aparece BUILD FAILURE (el reporte se genera igual).

Paso 3. Abrir el reporte en el navegador:

          target/site/serenity/index.html

        Incluye cada paso con capturas de pantalla, los productos elegidos, los
        datos del comprador, la semilla usada y la evidencia de defectos del sitio.

Si ya tiene Maven instalado y en el PATH, puede usar "mvn" en lugar de "mvnw".


4. OPCIONES UTILES
------------------
(En Linux/macOS reemplace "mvnw.cmd" por "./mvnw")

  Sin abrir la ventana de Chrome (modo headless):
      mvnw.cmd clean verify -Denvironment=headless

  Solo el caso solicitado (2 productos):
      mvnw.cmd clean verify -Dcucumber.filter.tags="@smoke"

  Solo las variaciones de cantidad (1 y 3 productos):
      mvnw.cmd clean verify -Dcucumber.filter.tags="@variacion"

  Repetir una corrida aleatoria concreta (semilla tomada del reporte):
      mvnw.cmd clean verify -Dseed=12345

  Demostrar el defecto de redondeo del sitio (FALLA A PROPOSITO, ver punto 6):
      mvnw.cmd clean verify -Dcucumber.filter.tags="@defecto-conocido"


5. REPORTES INCLUIDOS EN EL REPOSITORIO
---------------------------------------
La carpeta "reportes" contiene reportes de Serenity de corridas reales, para
revisarlos sin ejecutar nada. Abrir en el navegador:

  reportes/suite-completa/index.html     Corrida normal: 3 escenarios en verde.
  reportes/defecto-conocido/index.html   Escenario que demuestra el defecto de
                                         redondeo (falla a proposito).


6. DEFECTO CONOCIDO DEL SITIO
-----------------------------
Con algunas combinaciones de productos, SauceDemo muestra el resumen como
"Item total: $57.980000000000004" en lugar de "$57.98" (error de punto flotante
del sitio).

  - Las compras al azar redondean a 2 decimales para validar el total y dejan la
    evidencia en el reporte ("Defecto del sitio: item total sin redondear").
  - El escenario @defecto-conocido compra siempre Sauce Labs Fleece Jacket +
    Sauce Labs Onesie y exige "57.98" exacto: falla mientras el sitio no se
    corrija. Por eso esta EXCLUIDO de la corrida normal y solo se ejecuta si se
    pide con la etiqueta.

Detalle en conclusiones.txt.


7. ETIQUETAS
------------
  @ui @e2e @compra      Tipo de prueba y funcionalidad (toda la feature)
  @smoke @requerido     Caso solicitado: compra de 2 productos
  @regression           Escenarios de la corrida normal
  @variacion            Variaciones de cantidad (1 y 3 productos)
  @defecto-conocido     Demuestra el defecto de redondeo; excluido por defecto


8. CONFIGURACION
----------------
Archivo src/test/resources/serenity.conf:
  - webdriver.base.url por entorno (default, headless).
  - Credenciales publicas de demo en users.standard. Se pueden sobreescribir con
    las variables de entorno SAUCEDEMO_USERNAME y SAUCEDEMO_PASSWORD.
  - Preferencias de Chrome que desactivan el aviso de contrasenas de Google
    (bloquea los clics de la prueba).


9. ESTRUCTURA DEL PROYECTO (patron Screenplay)
----------------------------------------------
  pom.xml                       Dependencias y plugins (Serenity, Failsafe)
  mvnw, mvnw.cmd, .mvn/         Maven Wrapper
  src/test/resources/
    features/compra/            Escenarios Gherkin (en espanol)
    serenity.conf               Navegador, entornos, URL y credenciales
    junit-platform.properties   Excluye @defecto-conocido de la corrida normal
  src/test/java/com/saucedemo/
    runners/                    CucumberTestSuite (JUnit 5 + reporter de Serenity)
    stepdefinitions/            Pasos Gherkin: solo orquestan tasks y questions.
                                ScenarioHooks prepara el actor y la semilla
    tasks/                      Acciones del actor: OpenTheStore, Login,
                                AddRandomProductsToCart, AddProductsToCart,
                                ViewCart, FillCheckoutInformation,
                                ContinueToOrderSummary, FinishPurchase
    questions/                  Lo que el actor verifica: PageTitle,
                                CartBadgeCount, ProductsListed,
                                SelectedProducts, OrderSummary,
                                ConfirmationMessage
    userinterfaces/             Selectores de cada pantalla
    models/                     Product, BuyerData
    utils/                      TestConfig, SeededRandom, ActorMemory
  reportes/                     Reportes de corridas reales (punto 5)
  .github/                      Plantilla de Pull Request
  conclusiones.txt              Hallazgos y conclusiones
  CLAUDE.md                     Reglas de trabajo del repositorio


10. CONVENCION DE COMMITS Y PULL REQUESTS
------------------------------------------
Ramas:   en ingles: feature/<description>, fix/<description>, chore/<description>,
         docs/<description>
Commits: Conventional Commits + Gitmoji, EN INGLES y en imperativo:

           <type>(<scope>): <emoji> <description>

         Ejemplos:
           feat(screenplay): ✨ add purchase flow tasks
           fix(config): 🐛 disable Chrome password leak warning
           docs(readme): 📝 document execution with Maven Wrapper

         Sin lineas de autoria de herramientas de IA (Co-Authored-By, etc.).

         ✨ feat   ✅ test   🐛 fix   ♻️ refactor   ⬆️ build (dependencias)
         📦 build (herramientas)   🔧 chore (configuracion)   🙈 chore (gitignore)
         📝 docs   📊 docs (reportes)   🔀 merge

Pull Requests:
  1. Crear una rama desde main:      git switch -c feature/<description>
  2. Hacer commits pequenos con la convencion anterior.
  3. Verificar en verde:             mvnw.cmd clean verify
  4. Subir la rama:                  git push -u origin feature/<description>
  5. Abrir el PR hacia main en GitHub. La descripcion se completa con la
     plantilla .github/pull_request_template.md (checklist de verificacion).
  6. Nunca hacer commit directo a main.

Detalle completo de reglas: CLAUDE.md, seccion 7.


11. SOLUCION DE PROBLEMAS
-------------------------
  - "No se encuentra Chrome / driver": actualice Chrome. Serenity descarga el
    driver compatible automaticamente (webdriver.autodownload = true).
  - Error de version de Java: "java -version" debe mostrar 17 o superior.
  - mvnw no descarga Maven: revise la conexion o el proxy corporativo
    (configurar el proxy de Maven en ~/.m2/settings.xml).
  - "./mvnw: Permission denied" en Linux/macOS: ejecute  chmod +x mvnw
  - Un escenario fallo con datos aleatorios: copie la semilla del reporte y
    ejecute  mvnw.cmd clean verify -Dseed=<semilla>
