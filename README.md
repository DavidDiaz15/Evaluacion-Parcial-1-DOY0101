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

- Spring Web (Para la creación de endpoints REST)

### Arquitectura del Proyecto
El proyecto sigue una arquitectura de capas estándar:

- Controller: Maneja las solicitudes HTTP y define los endpoints.

- Service: Contiene la lógica de negocio.

- Repository: Gestiona el almacenamiento de datos (actualmente utiliza una lista en memoria).

- Model: Define la estructura de los objetos de datos (Libro).

### Endpoints de la API
La URL base para todos los endpoints es: http://localhost:8080/api/v1/productos

| Método | Endpoint        | Descripción                                                              |
| :---:  |  :----:         |  :---:                                                                   |
| GET    | /               | Obtiene la lista de todos los libros.                                    |
| GET    | /buscar/{nombre}       | Busca un libro específico por su nombre.                          |
| GET    | /categoria/{categoria} | Busca y retorna todos los libros pertenecientes a una categoría.  |
| POST   | /               | Agrega un nuevo libro al sistema.                                        |
| DELETE | /borrar/{nombre}       | Elimina un libro específico por su nombre.                        |

---

## Flujos de Merge utilizado: GitFlow

Se utilizó GitFlow dado a las branchs intuitivas, que permiten un excelente trabajo colaborativo, ya que las features se desarrollan en su rama propia y siempre se integran a develop mediante un Pull Request el cual es revisado por los colaboradores.

De igual manera, la branch Hotfixes nos va a permitir parchar los errores críticos que surgen.

Por último, elegimos este flujo dado que los lanzamientos de versiones se realizan mediante un Pull Request desde develop hacia main una vez que el código esté probado.
