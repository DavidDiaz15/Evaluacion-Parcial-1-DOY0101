# Evaluacion Parcial 1 DOY0101
Este repositorio está dedicado a la evaluación parcial 1 del ramo de DevOPS.

## 🧠 Los integrantes del grupo son:
- Matias Peirano
- David Diaz

---

# Sistema de Gestión de Librería (API REST)
Esta es una aplicación backend desarrollada con Java y Spring Boot que permite gestionar un inventario de libros a través de una API REST. El sistema permite listar, buscar, agregar y eliminar libros.

*El proposito del desarrollo de este proyecto es demostrar el uso de la metodologia GitFlow*

### Tecnologías Utilizadas
- Java 21

- Spring Boot 4.0.5

- Maven (Gestor de dependencias)

- Lombok (Para reducir código boilerplate como Getters y Setters)

### Spring Web (Para la creación de endpoints REST)

### Arquitectura del Proyecto
El proyecto sigue una arquitectura de capas estándar:

- Controller: Maneja las solicitudes HTTP y define los endpoints.

- Service: Contiene la lógica de negocio.

- Repository: Gestiona el almacenamiento de datos (actualmente utiliza una lista en memoria).

- Model: Define la estructura de los objetos de datos (Libro).

### Endpoints de la API
La URL base para todos los endpoints es: http://localhost:8080/api/v1/productos

| Método | Endpoint        | Descripción                                 |
| :---:  |  :----:         |  :---:                                      |
| GET    | /               | Obtiene la lista de todos los libros.       |
| POST   | /               | Agrega un nuevo libro al sistema.           |
| GET    | /{nombre}       | Busca un libro específico por su nombre.    |
| DELETE | /{nombre}       | Elimina un libro específico por su nombre.  |
