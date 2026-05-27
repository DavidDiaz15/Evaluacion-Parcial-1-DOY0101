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

---

# EVALUACION PARCIAL 2

# README.md — Ejemplo de Pipeline DevSecOps para Microservicio

## Descripción General

Este proyecto muestra un ejemplo de implementación de un flujo **CI/CD DevSecOps** para un microservicio contenerizado utilizando:

- Docker
- GitHub Actions
- SonarQube
- Snyk
- Dependabot
- Docker Compose
- Kubernetes (opcional)

El objetivo es garantizar:

- Automatización de despliegues
- Calidad del código
- Seguridad de dependencias
- Trazabilidad de cambios
- Integración y entrega continua

---

# 1. Contenerización del Microservicio (IE1)

## Dockerfile

Ejemplo de `Dockerfile` para una aplicación Python:

```dockerfile
FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 5000

CMD ["python", "app.py"]
```

## Construcción de la Imagen

```bash
docker build -t microservicio-demo .
```

## Ejecución del Contenedor

```bash
docker run -p 5000:5000 microservicio-demo
```

---

# 2. Pipeline CI/CD con GitHub Actions

## Estructura del Workflow

Archivo:

```bash
.github/workflows/pipeline.yml
```

## Pipeline Completo

```yaml
name: DevSecOps Pipeline

on:
  push:
    branches:
      - main

  pull_request:

jobs:

  test:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout del código
        uses: actions/checkout@v4

      - name: Configurar Python
        uses: actions/setup-python@v5
        with:
          python-version: 3.11

      - name: Instalar dependencias
        run: |
          pip install -r requirements.txt
          pip install pytest

      - name: Ejecutar pruebas unitarias
        run: pytest

  docker:
    needs: test
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Construir imagen Docker
        run: docker build -t microservicio-demo .

  security:
    needs: docker
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Análisis con SonarQube
        uses: SonarSource/sonarqube-scan-action@master
        env:
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}

      - name: Escaneo con Snyk
        uses: snyk/actions/python@master
        env:
          SNYK_TOKEN: ${{ secrets.SNYK_TOKEN }}

  deploy:
    needs: security
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Despliegue con Docker Compose
        run: docker compose up -d
```

---

# 3. Pruebas Unitarias (IE2)

## Ejemplo con PyTest

Archivo:

```python
# test_app.py

def test_suma():
    assert 2 + 2 == 4
```

## Ejecución Manual

```bash
pytest
```

Las pruebas se ejecutan automáticamente en cada `push` y `pull request`.

---

# 4. Seguridad y Análisis de Código (IE3)

## Dependabot

Archivo:

```yaml
# .github/dependabot.yml

version: 2

updates:
  - package-ecosystem: "pip"
    directory: "/"
    schedule:
      interval: "daily"
```

Dependabot permite:

- Detectar vulnerabilidades
- Actualizar dependencias automáticamente
- Generar Pull Requests de actualización

---

## SonarQube

Se utiliza para:

- Detectar bugs
- Code smells
- Cobertura de pruebas
- Vulnerabilidades

Ejemplo de configuración:

```properties
sonar.projectKey=microservicio-demo
sonar.sources=.
```

---

## Snyk

Snyk analiza:

- Dependencias vulnerables
- Librerías inseguras
- Riesgos de seguridad

---

# 5. Bloqueo por Fallos de Seguridad (IE3)

El pipeline está configurado para detener el despliegue si:

- Fallan las pruebas unitarias
- SonarQube detecta problemas críticos
- Snyk encuentra vulnerabilidades severas

Esto se logra mediante las dependencias entre jobs:

```yaml
needs: security
```

Si el job `security` falla, `deploy` no se ejecuta.

---

# 6. Orquestación de Contenedores (IE5)

## Docker Compose

Archivo:

```yaml
version: '3.9'

services:
  app:
    build: .
    ports:
      - "5000:5000"
```

## Levantar el Entorno

```bash
docker compose up -d
```

---

# 7. Orquestación con Kubernetes (Opcional)

Ejemplo de Deployment:

```yaml
apiVersion: apps/v1
kind: Deployment

metadata:
  name: microservicio-demo

spec:
  replicas: 2

  selector:
    matchLabels:
      app: microservicio-demo

  template:
    metadata:
      labels:
        app: microservicio-demo

    spec:
      containers:
        - name: app
          image: microservicio-demo:latest

          ports:
            - containerPort: 5000
```

## Despliegue

```bash
kubectl apply -f deployment.yaml
```

---

# 8. Trazabilidad y Calidad del Software (IE4)

## Trazabilidad

La trazabilidad se garantiza mediante:

- Versionamiento con Git
- Historial de commits
- Pull Requests
- Ejecución automática de pipelines
- Logs de GitHub Actions
- Escaneo continuo de seguridad

Cada cambio queda asociado a:

- Autor
- Fecha
- Resultado de pruebas
- Resultado de análisis de seguridad
- Estado del despliegue

---

## Calidad del Software

La calidad se asegura utilizando:

| Herramienta | Función |
|---|---|
| PyTest | Validación funcional |
| SonarQube | Calidad de código |
| Snyk | Seguridad |
| Docker | Consistencia del entorno |
| GitHub Actions | Automatización CI/CD |
| Dependabot | Actualización de dependencias |

---

# 9. Flujo General del Pipeline

```text
Desarrollador
      ↓
Push a GitHub
      ↓
GitHub Actions
      ↓
Pruebas Unitarias
      ↓
Build Docker
      ↓
Análisis de Seguridad
      ↓
Despliegue Automático
```

---

# 10. Beneficios de la Implementación

- Automatización completa del ciclo DevOps
- Integración continua
- Entrega continua
- Mayor seguridad
- Reducción de errores manuales
- Mejor mantenimiento
- Escalabilidad mediante contenedores
- Monitoreo y trazabilidad del software

---

# Tecnologías Utilizadas

- Docker
- GitHub Actions
- Python
- PyTest
- SonarQube
- Snyk
- Dependabot
- Docker Compose
- Kubernetes

---

# Autor

Proyecto de ejemplo para implementación de prácticas DevSecOps y CI/CD.
