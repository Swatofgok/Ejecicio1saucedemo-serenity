## ¿Qué cambia?

<!-- Resumen en 1-3 líneas. Título del PR con la convención: tipo(alcance): emoji descripción -->

## ¿Por qué?

<!-- Problema o necesidad que resuelve. -->

## Tipo de cambio

- [ ] ✨ feat — nueva capacidad de automatización
- [ ] ✅ test — escenarios / step definitions
- [ ] 🐛 fix — corrección de la suite
- [ ] ♻️ refactor — sin cambio de comportamiento
- [ ] ⬆️📦 build — dependencias o herramientas
- [ ] 🔧 chore — configuración
- [ ] 📝📊 docs — documentación o reportes

## Verificación

- [ ] `./mvnw clean verify` termina en `BUILD SUCCESS` (pegar resumen de Serenity abajo)
- [ ] Sin locators en step definitions, sin `Thread.sleep`, sin credenciales en código ni en `.feature`
- [ ] `reportes/` actualizado si cambió el comportamiento
- [ ] `readme.txt` / `conclusiones.txt` actualizados si aplica

```
Tests passed | 
Tests failed | 
```
