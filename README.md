# Ejecicio1saucedemo-serenity

Prueba E2E automatizada del flujo de compra en [SauceDemo](https://www.saucedemo.com/) con
Serenity BDD (Screenplay), Cucumber, JUnit 5 y Selenium (Java 17 + Maven Wrapper).

Flujo cubierto: login → agregar productos al carrito → ver carrito → checkout → confirmación
"THANK YOU FOR YOUR ORDER". Los productos y los datos del comprador se eligen al azar con una
semilla registrada en el reporte (`-Dseed=<n>` repite la misma corrida).

## Ejecución rápida

Requisitos: JDK 17 o superior y Google Chrome. No hace falta instalar Maven.

```bash
git clone https://github.com/Swatofgok/Ejecicio1saucedemo-serenity.git
cd Ejecicio1saucedemo-serenity

mvnw.cmd clean verify     # Windows
./mvnw clean verify       # Linux / macOS
```

Reporte generado: `target/site/serenity/index.html`.

## Documentación de la entrega

| Archivo | Contenido |
|---|---|
| [`readme.txt`](readme.txt) | Paso a paso completo de ejecución, opciones, etiquetas y estructura |
| [`conclusiones.txt`](conclusiones.txt) | Hallazgos y conclusiones (incluye el defecto de redondeo del sitio) |
| [`reportes/`](reportes/) | Reportes Serenity de corridas reales, listos para abrir sin ejecutar nada |
