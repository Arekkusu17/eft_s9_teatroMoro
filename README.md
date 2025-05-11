# Resumen de Pruebas Realizadas

## Ventas y Reservas
- Se validó que el sistema permite realizar reservas correctamente, solicitando nombre del cliente, edad, género y tipo de descuento.
- La asignación de asiento es precisa, y cada reserva incluye sector, número de asiento, tipo de descuento y precio final.
- El sistema evita correctamente la doble reserva de un mismo asiento.
- Se comprobó que la modificación de asiento funciona y solo se permite si el nuevo asiento está libre.
- Se imprime una boleta detallada después de cada venta, mostrando claramente el descuento aplicado y el total a pagar.

## Descuentos
- Se verificó la correcta aplicación de los descuentos:
  - 10% para niños menores de 18 años.
  - 15% para estudiantes.
  - 20% para mujeres mayores de 18 años que no sean estudiantes ni de la tercera edad.
  - 25% para personas de la tercera edad (65+ años).
- Se implementó una lista `discounts` para definir y mantener los tipos de descuentos de forma flexible y escalable.
- Se desarrolló una función que determina automáticamente el tipo de descuento en base a edad, género y categoría informada.

## Datos y Consistencia
- Las reservas se asocian correctamente con sus respectivos clientes a través del arreglo `customers` y el arreglo `saleIds`.
- Al eliminar una reserva, se libera el asiento, se elimina el cliente y su ID de venta, manteniendo la integridad de los datos.
- La actualización de asientos respeta la consistencia del sistema, evitando duplicaciones o conflictos.

## Validación de Asientos
- Se aplicaron validaciones estrictas para seleccionar únicamente filas válidas: `vip`, `palco`, `platea baja`, `platea alta`, `galeria`.
- Se valida que el número de asiento esté en el rango permitido (0 a 9).
- Se integraron controles robustos para entradas inválidas (por ejemplo, ingreso de texto en lugar de números), mejorando la estabilidad del sistema.

## Estabilidad y Optimización
- Se realizaron múltiples pruebas de venta, eliminación y actualización de reservas para asegurar el correcto funcionamiento del flujo principal.
- El sistema responde correctamente ante intentos de reserva duplicada o actualización hacia un asiento ocupado.

