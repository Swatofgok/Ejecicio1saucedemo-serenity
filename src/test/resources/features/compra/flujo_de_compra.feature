# language: es
@ui @e2e @compra
Característica: Flujo de compra de punta a punta en SauceDemo
  Como cliente de la tienda SauceDemo
  Quiero comprar productos desde el inventario hasta la confirmación
  Para validar que el flujo principal de compra funciona correctamente

  Antecedentes:
    Dado que el usuario abre la tienda SauceDemo

  Esquema del escenario: Compra de <cantidad> productos elegidos al azar
    Cuando inicia sesión como usuario estándar
    Entonces visualiza la página de productos
    Cuando agrega <cantidad> productos aleatorios al carrito
    Entonces el contador del carrito indica <cantidad>
    Cuando visualiza el carrito
    Entonces el carrito contiene exactamente los productos seleccionados
    Cuando completa el formulario de compra con datos generados dinámicamente
    Y revisa el resumen de la compra
    Entonces el total del resumen coincide con la suma de los productos más los impuestos
    Cuando finaliza la compra
    Entonces visualiza el mensaje de confirmación "THANK YOU FOR YOUR ORDER"

    @requerido @smoke @regression
    Ejemplos: Caso solicitado en la prueba (dos productos)
      | cantidad |
      | 2        |

    @variacion @regression
    Ejemplos: Variaciones dinámicas de cantidad
      | cantidad |
      | 1        |
      | 3        |

  # Defecto conocido del sitio: con estos productos el "Item total" se muestra como $57.980000000000004
  # (error de punto flotante). Falla mientras SauceDemo no lo corrija; se excluye de la corrida normal.
  @defecto-conocido
  Escenario: El resumen muestra el item total con dos decimales exactos
    Cuando inicia sesión como usuario estándar
    Y agrega al carrito los productos:
      | Sauce Labs Fleece Jacket |
      | Sauce Labs Onesie        |
    Y visualiza el carrito
    Y completa el formulario de compra con datos generados dinámicamente
    Y revisa el resumen de la compra
    Entonces el item total se muestra exactamente con dos decimales
