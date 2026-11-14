# pong_project

## Tabla de Diagnóstico - Actividad 1

| ID | Problema | Ubicación | Impacto | Propuesta de Solución |
| :--- | :--- | :--- | :--- | :--- |
| **1** | **Espera activa (*busy waiting*)** | `BallRunner.java` (`run`) | Consumo innecesario de recursos durante las pausas. | Reemplazar el ciclo con `Thread.yield()` por un temporizador (`javax.swing.Timer`) o un mecanismo de coordinación (`wait/notify`). |
| **2** | **Violación de SRP** | `Board.java` | Acoplamiento excesivo: la clase maneja interfaz, eventos y bucle de juego a la vez. | Desacoplar responsabilidades y separar el control de entrada usando *Key Bindings*. |
| **3** | **Acceso concurrente no sincronizado** | `Board.java` y `BallRunner.java` | Riesgo de condiciones de carrera y actualización de la UI. | Sincronizar el acceso al estado compartido y centralizar el renderizado. |
| **4** | **Colisiones imprecisas** | `BallRunner.java` (`DX/DY = 20`) | Desplazamientos muy grandes que provocan que la pelota atraviese límites u objetos. | Reducir el paso de incremento y refinar la lógica de detección de colisiones. |
| **5** | **Hilos no controlados** | `Board.java` y `BallRunner.java` | Bucles `while(true)` infinitos sin un cierre limpio al salir. | Implementar un control adecuado del método `stop()` para asegurar la terminación segura de los hilos. |
