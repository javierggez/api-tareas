📖 Pasos para Probar la API
Ahora que la API está corriendo en http://localhost:8080, puedes interactuar con los endpoints. Usa un cliente HTTP como Postman, Insomnia o cURL para enviar solicitudes. A continuación, te guiaré paso a paso.

1. 📝 Registrar un Usuario
   Primero, registra un usuario para obtener credenciales.

Endpoint: POST /autenticacion/registrar
URL Completa: http://localhost:8080/autenticacion/registrar
Headers:
text

Copiar
Content-Type: application/json
Body:
json

Copiar
{
"nombreUsuario": "usuario1",
"contrasena": "password",
"correo": "usuario1@ejemplo.com",
"rol": "USER"
}
Solicitud (cURL):
bash

Copiar
curl -X POST http://localhost:8080/autenticacion/registrar \
-H "Content-Type: application/json" \
-d '{"nombreUsuario":"usuario1","contrasena":"password","correo":"usuario1@ejemplo.com","rol":"USER"}'
Respuesta Esperada:
Estado HTTP: 200 OK
Body:
text

Copiar
Usuario registrado con éxito
Errores Posibles:
Si el nombre de usuario ya existe:
Estado HTTP: 400 Bad Request
Body: "Error al registrar usuario: El nombre de usuario ya está en uso"
Usa un nombreUsuario único si encuentras este error.
2. 🔑 Iniciar Sesión y Obtener un Token
   Para interactuar con los endpoints de tareas, necesitas un token JWT. Inicia sesión con las credenciales del usuario que registraste.

Endpoint: POST /autenticacion/iniciar-sesion
URL Completa: http://localhost:8080/autenticacion/iniciar-sesion
Headers:
text

Copiar
Content-Type: application/json
Body:
json

Copiar
{
"nombreUsuario": "usuario1",
"contrasena": "password"
}
Solicitud (cURL):
bash

Copiar
curl -X POST http://localhost:8080/autenticacion/iniciar-sesion \
-H "Content-Type: application/json" \
-d '{"nombreUsuario":"usuario1","contrasena":"password"}'
Respuesta Esperada:
Estado HTTP: 200 OK
Body:
json

Copiar
{
"token": "eyJhbGciOiJIUzI1NiJ9..."
}
Errores Posibles:
Si las credenciales son incorrectas:
Estado HTTP: 401 Unauthorized
Body: "Credenciales inválidas: Bad credentials"
Acción:
Copia el valor del token (por ejemplo, eyJhbGciOiJIUzI1NiJ9...). Lo necesitarás para las siguientes solicitudes.
3. 📋 Gestionar Tareas (Operaciones CRUD)
   Con el token JWT, puedes usar los endpoints de /tareas. Todas las solicitudes a estos endpoints requieren el token en el header Authorization.

3.1 Crear una Tarea
Endpoint: POST /tareas
URL Completa: http://localhost:8080/tareas
Headers:
text

Copiar
Content-Type: application/json
Authorization: Bearer <tu_token>
Body:
json

Copiar
{
"titulo": "Hacer la compra",
"descripcion": "Comprar frutas y verduras",
"completada": false
}
Solicitud (cURL):
bash

Copiar
curl -X POST http://localhost:8080/tareas \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
-d '{"titulo":"Hacer la compra","descripcion":"Comprar frutas y verduras","completada":false}'
Respuesta Esperada:
Estado HTTP: 200 OK
Body:
json

Copiar
{
"id": 1,
"titulo": "Hacer la compra",
"descripcion": "Comprar frutas y verduras",
"completada": false,
"fechaCreacion": "2025-04-11TXX:XX:XX",
"usuario": {
"nombreUsuario": "usuario1",
"correo": "usuario1@ejemplo.com",
"rol": "USER"
}
}
Errores Posibles:
Si el token es inválido o falta:
Estado HTTP: 401 Unauthorized
3.2 Listar Tareas
Endpoint: GET /tareas
URL Completa: http://localhost:8080/tareas
Headers:
text

Copiar
Authorization: Bearer <tu_token>
Solicitud (cURL):
bash

Copiar
curl -X GET http://localhost:8080/tareas \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
Respuesta Esperada:
Estado HTTP: 200 OK
Body:
json

Copiar
{
"content": [
{
"id": 1,
"titulo": "Hacer la compra",
"descripcion": "Comprar frutas y verduras",
"completada": false,
"fechaCreacion": "2025-04-11TXX:XX:XX",
"usuario": {
"nombreUsuario": "usuario1",
"correo": "usuario1@ejemplo.com",
"rol": "USER"
}
}
],
"pageable": {
"pageNumber": 0,
"pageSize": 10,
"sort": {
"sorted": true,
"unsorted": false,
"empty": false
},
"offset": 0,
"paged": true,
"unpaged": false
},
"totalPages": 1,
"totalElements": 1,
"last": true,
"size": 10,
"number": 0,
"sort": {
"sorted": true,
"unsorted": false,
"empty": false
},
"numberOfElements": 1,
"first": true,
"empty": false
}
3.3 Obtener una Tarea Específica
Endpoint: GET /tareas/{id}
URL Completa: http://localhost:8080/tareas/1
Headers:
text

Copiar
Authorization: Bearer <tu_token>
Solicitud (cURL):
bash

Copiar
curl -X GET http://localhost:8080/tareas/1 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
Respuesta Esperada:
Estado HTTP: 200 OK
Body:
json

Copiar
{
"id": 1,
"titulo": "Hacer la compra",
"descripcion": "Comprar frutas y verduras",
"completada": false,
"fechaCreacion": "2025-04-11TXX:XX:XX",
"usuario": {
"nombreUsuario": "usuario1",
"correo": "usuario1@ejemplo.com",
"rol": "USER"
}
}
Errores Posibles:
Si la tarea no existe:
Estado HTTP: 404 Not Found
Body: "Tarea no encontrada o no pertenece al usuario"
3.4 Actualizar una Tarea
Endpoint: PUT /tareas/{id}
URL Completa: http://localhost:8080/tareas/1
Headers:
text

Copiar
Content-Type: application/json
Authorization: Bearer <tu_token>
Body:
json

Copiar
{
"titulo": "Hacer la compra (actualizado)",
"descripcion": "Comprar frutas, verduras y carne",
"completada": true
}
Solicitud (cURL):
bash

Copiar
curl -X PUT http://localhost:8080/tareas/1 \
-H "Content-Type: application/json" \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
-d '{"titulo":"Hacer la compra (actualizado)","descripcion":"Comprar frutas, verduras y carne","completada":true}'
Respuesta Esperada:
Estado HTTP: 200 OK
Body:
json

Copiar
{
"id": 1,
"titulo": "Hacer la compra (actualizado)",
"descripcion": "Comprar frutas, verduras y carne",
"completada": true,
"fechaCreacion": "2025-04-11TXX:XX:XX",
"usuario": {
"nombreUsuario": "usuario1",
"correo": "usuario1@ejemplo.com",
"rol": "USER"
}
}
3.5 Eliminar una Tarea
Endpoint: DELETE /tareas/{id}
URL Completa: http://localhost:8080/tareas/1
Headers:
text

Copiar
Authorization: Bearer <tu_token>
Solicitud (cURL):
bash

Copiar
curl -X DELETE http://localhost:8080/tareas/1 \
-H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
Respuesta Esperada:
Estado HTTP: 200 OK
Body:
text

Copiar
Tarea eliminada con éxito
4. 📚 Explorar la Documentación con Swagger
   La API está documentada con Swagger, lo que te permite probar los endpoints directamente desde el navegador.

URL de Swagger: http://localhost:8080/swagger-ui.html
Instrucciones:
Abre la URL en tu navegador.
Haz clic en el endpoint /autenticacion/registrar y prueba registrar un usuario.
Usa el endpoint /autenticacion/iniciar-sesion para obtener un token.
Haz clic en el botón "Authorize" en Swagger e ingresa tu token JWT (por ejemplo, Bearer eyJhbGciOiJIUzI1NiJ9...).
Prueba los endpoints de /tareas directamente desde Swagger.
⚠️ Notas Importantes
Tokens JWT: Los tokens tienen una duración de 1 hora (3600 segundos). Si tu token expira, inicia sesión nuevamente para obtener uno nuevo.
Base de Datos: Asegúrate de que PostgreSQL esté corriendo y que las credenciales en application.properties sean correctas.
Errores Comunes:
401 Unauthorized: Verifica que el token sea válido y esté incluido en el header Authorization.
400 Bad Request: Revisa que el body de la solicitud tenga el formato correcto.
Si la API no inicia, verifica los logs en la consola para identificar problemas (por ejemplo, conexión a la base de datos fallida).
📬 Contacto
Si tienes problemas para probar la API localmente o necesitas ayuda, contáctame:

Correo: tu_correo@ejemplo.com
GitHub: tu_usuario
✨ ¡Gracias por probar ApiTareas en tu máquina local! Espero que disfrutes explorando esta API y que sea útil para tu aprendizaje o evaluación.

text

Copiar

**Acción**:
- Crea un archivo `MANUAL-DE-USO-LOCAL.md` en la raíz de tu proyecto y pega el contenido anterior.
- Personaliza las siguientes secciones:
  - **Enlace al Repositorio**: Reemplaza `https://github.com/tu-usuario/apitareas.git` con el enlace real de tu repositorio.
  - **Contacto**: Ajusta el correo y el enlace de GitHub con tu información real.
- Si lo deseas, puedes agregar capturas de pantalla (por ejemplo, de Postman o Swagger) para ilustrar cómo enviar las solicitudes y las respuestas obtenidas.

---

### **Conclusión**

- **Manual de Uso Local**:
  - El archivo `MANUAL-DE-USO-LOCAL.md` proporciona una guía clara y práctica para que cualquier persona pueda configurar, ejecutar y probar tu API en su máquina local.
  - Incluye instrucciones detalladas para instalar las dependencias, configurar la base de datos, ejecutar la aplicación, y probar los endpoints con ejemplos prácticos (usando cURL).
- **Proyecto**:
  - Ahora tienes tres archivos de documentación:
    - `README.md`: Documentación general del proyecto (descripción, tecnologías, configuración, despliegue).
    - `MANUAL-DE-USO.md`: Manual para probar la API en producción (AWS) y localmente.
    - `MANUAL-DE-USO-LOCAL.md`: Manual específico para probar la API en un entorno local.
  - La aplicación está desplegada en AWS Elastic Beanstalk, y las pruebas automatizadas y manuales han sido completadas con éxito.

**Proyecto Terminado**:
- Tu API REST para gestión de tareas está completamente documentada y lista para ser compartida en tu portafolio. 🎉
- Este proyecto demuestra habilidades sólidas en desarrollo backend (Java, Spring Boot), autenticación (JWT), documentación (Swagger), pruebas (JUnit, MockMvc), y despliegue en la nube (AWS).

Si deseas agregar más funcionalidades (como CI/CD con GitHub Actions, monitoreo con CloudWatch, o una interfaz frontend), o necesitas ayuda para compartir el proyecto en tu portafolio, no dudes en pedírmelo. ¡Felicidades por tu gran trabajo! 🚀