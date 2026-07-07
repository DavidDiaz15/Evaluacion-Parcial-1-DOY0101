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

## Ejemplo de Pipeline DevSecOps para Microservicio

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
# 📚 Desarrollo Evaluación 3

**Autores:** David Díaz, Matías Peirano

> **Nota:** Todas las evidencias solicitadas (capturas de pantalla, configuraciones, despliegues, dashboards, ejecución del pipeline y validaciones) se encuentran en el documento PDF de evidencias entregado junto a este proyecto.

---

# Requerimiento 1 - Configuración de Herramientas de Monitoreo (IE1)

Se implementó un sistema de observabilidad para la API desarrollada en **Spring Boot (Java 21)** utilizando Prometheus y Micrometer para la recolección de métricas en tiempo real.

## Implementación

### 1. Instrumentación de la API

Se agregaron las dependencias necesarias en el archivo `pom.xml`:

* Spring Boot Actuator
* Micrometer Prometheus Registry

Esto permitió exponer métricas internas de la aplicación.

### 2. Configuración de variables de entorno

Se habilitaron los endpoints de Actuator mediante las siguientes variables:

```properties
MANAGEMENT_ENDPOINTS_WEB_EXPOSURE_INCLUDE=*
MANAGEMENT_ENDPOINT_PROMETHEUS_ENABLED=true
```

### 3. Configuración de Prometheus

Se desplegó un contenedor de Prometheus configurado para realizar scraping cada 15 segundos al endpoint:

```
/actuator/prometheus
```

Con ello se obtienen métricas de:

* CPU
* Memoria
* Threads
* JVM
* Requests HTTP
* Estado de la aplicación

---

# Requerimiento 2 - Despliegue Orquestado en Kubernetes (IE2)

La aplicación fue desplegada sobre un clúster Kubernetes utilizando **K3s** ejecutándose en una instancia **AWS EC2 Ubuntu Server 22.04 LTS**.

## Construcción de la imagen Docker

```bash
docker login
docker build -t <usuario>/libreria-api:latest ./libreriaApp
docker push <usuario>/libreria-api:latest
```

---

## Configuración de AWS

Se creó una instancia EC2 tipo:

```
t3.medium
```

Se habilitaron los siguientes puertos en el Security Group:

| Puerto | Uso                 |
| ------ | ------------------- |
| 22     | SSH                 |
| 8080   | API                 |
| 30080  | NodePort Kubernetes |

---

## Instalación de K3s

```bash
curl -sfL https://get.k3s.io | sh -

sudo chmod 644 /etc/rancher/k3s/k3s.yaml

export KUBECONFIG=/etc/rancher/k3s/k3s.yaml
```

---

## Despliegue de los manifiestos

Se utilizaron dos manifiestos:

* `mysql.yaml`
* `api.yaml`

Aplicación de recursos:

```bash
kubectl apply -f mysql.yaml
kubectl apply -f api.yaml
```

---

## Verificación

Se comprobó el correcto funcionamiento mediante:

```bash
kubectl get pods -w

kubectl get deployments

kubectl get services
```

Finalmente se verificó el acceso desde el navegador:

```
http://IP_PUBLICA_AWS:30080/actuator
```

---

# Requerimiento 3 - Dashboard de Métricas (IE3)

Se implementó Grafana como plataforma de visualización conectada directamente a Prometheus.

## Configuración

* Despliegue del contenedor Grafana.
* Configuración de Prometheus como Data Source.

```
http://prometheus:9090
```

Se importó el dashboard:

```
Spring Boot 3.x System Monitor

Dashboard ID: 11378
```

---

## Métricas monitoreadas

El dashboard permite visualizar:

* Tiempo de actividad (Uptime)
* Uso de CPU
* Uso de Memoria Heap
* Uso de Memoria Non-Heap
* Tráfico de Entrada/Salida
* Estado general de la JVM
* Disponibilidad del servicio

---

# Requerimiento 4 - Implementación de Políticas de Cumplimiento (IE5)

Se implementaron controles de calidad y seguridad sobre el repositorio utilizando GitHub y SonarQube.

## Branch Protection

Se protegió la rama principal (`main`) configurando:

* Prohibición de commits directos.
* Uso obligatorio de Pull Requests.
* Revisión antes de fusionar cambios.

---

## Análisis Estático (SAST)

Se integró SonarQube para analizar automáticamente el código fuente en cada ejecución del pipeline.

Las validaciones incluyen:

* Vulnerabilidades de seguridad.
* Security Hotspots.
* Bugs.
* Code Smells.
* Deuda técnica.
* Calidad del código.

---

# Requerimiento 5 - Integración CI/CD y Decisiones Técnicas (IE4)

Todo el flujo de integración continua fue automatizado mediante GitHub Actions.

## Flujo de trabajo

```text
Desarrollador

↓

Push a rama secundaria

↓

Pull Request

↓

GitHub Actions

↓

Análisis SonarQube

↓

Quality Gate

↓

Construcción Imagen Docker

↓

Despliegue Kubernetes
```

## Toma de decisiones

El pipeline depende completamente del resultado del **Quality Gate** generado por SonarQube.

Si el análisis cumple los estándares definidos:

* Se construye la imagen Docker.
* Se continúa con el despliegue.
* Se actualiza el entorno Kubernetes.

En caso contrario, el proceso se detiene automáticamente.

---

# Requerimiento 6 - Detención del Pipeline ante Fallas Críticas (IE6)

Se implementó un mecanismo de bloqueo automático del pipeline cuando SonarQube detecta vulnerabilidades críticas o incumplimiento del Quality Gate.

## Funcionamiento

1. Se ejecuta GitHub Actions.
2. SonarQube analiza el código.
3. Se evalúa el Quality Gate.
4. Si existen errores críticos:

   * El pipeline cambia a estado **Failed**.
   * No se construye la imagen Docker.
   * No se realiza el despliegue.
   * Se evita que código inseguro llegue al entorno de producción.

Este mecanismo garantiza que únicamente versiones que cumplen con los estándares de calidad y seguridad puedan ser desplegadas.

---

# Tecnologías Utilizadas

| Tecnología           | Uso                                    |
| -------------------- | -------------------------------------- |
| Java 21              | Backend                                |
| Spring Boot          | Framework principal                    |
| Spring Boot Actuator | Métricas de la aplicación              |
| Micrometer           | Instrumentación                        |
| Prometheus           | Recolección de métricas                |
| Grafana              | Visualización                          |
| Docker               | Contenedores                           |
| Docker Hub           | Repositorio de imágenes                |
| MySQL 8.0            | Base de datos                          |
| AWS EC2              | Infraestructura en la nube             |
| Kubernetes (K3s)     | Orquestación                           |
| GitHub               | Control de versiones                   |
| GitHub Actions       | CI/CD                                  |
| SonarQube            | Análisis estático y control de calidad |

---

# Evidencias

Las evidencias correspondientes a cada requerimiento (capturas de pantalla, configuraciones, despliegues, dashboards, ejecución del pipeline y validaciones de SonarQube) se encuentran documentadas en el PDF de evidencias entregado junto a este proyecto.

---

# Evaluación Final Transversal (EFT)

Esta sección documenta las implementaciones avanzadas realizadas sobre la infraestructura base para cumplir con los requerimientos de la Evaluación Final Transversal del proyecto, enfocadas en la seguridad, observabilidad y control de despliegues.

## 1. Práctica de Despliegue Seguro y Continuo

Para garantizar que el código que llega a producción sea estable y auditable, se modificó el pipeline de GitHub Actions agregando nuevas capas de validación:

* **Pruebas de Aceptación Automatizadas:** Se implementó un *job* intermedio (`acceptance_test`) que utiliza Docker Compose para levantar la aplicación y la base de datos en un entorno simulado (*staging*). Este paso verifica que los servicios se comunican correctamente y responden a peticiones antes de aplicar los manifiestos en Kubernetes.
* **Políticas de Aprobación de Despliegue:** Se configuró el entorno `production` mediante los *Environments* de GitHub para requerir revisión humana. El pipeline pausa su ejecución después de las pruebas y exige que un revisor autorizado apruebe manualmente el proceso antes de realizar el despliegue final en la instancia EC2.

## 2. Orquestación y Monitoreo Avanzado en la Nube

Adicional a la implementación de Prometheus y Grafana, se integraron tecnologías específicas para el control de red y métricas de infraestructura:

* **Istio (Service Mesh):** Se inyectó la malla de servicios de Istio en el clúster de Kubernetes (K3s). Esto permite manejar la red de los microservicios de manera más eficiente, controlando el tráfico interno entre la API y la base de datos, y proporcionando métricas detalladas de la comunicación a nivel de *pods*.
* **AWS CloudWatch:** Se instaló y configuró el agente de Amazon CloudWatch directamente en el sistema operativo del servidor (Amazon Linux). Esta herramienta permite analizar el consumo de recursos (CPU, Memoria, Disco) de la instancia EC2 a nivel de infraestructura, complementando las métricas de aplicación obtenidas con Actuator.

## 3. Declaración de Uso de Inteligencia Artificial

En cumplimiento con las normativas del curso, declaramos el uso de Inteligencia Artificial (IA) generativa como herramienta de apoyo durante el desarrollo de este proyecto.

* **Uso de la IA:** Se utilizó IA (Gemini) como asistente técnico para la estructuración de comandos en Bash, la sintaxis de los archivos YAML de Kubernetes y la configuración y resolución de errores (*troubleshooting*) dentro del pipeline de GitHub Actions.
* **Validación Humana:** Todos los comandos, scripts y configuraciones sugeridos por la IA fueron analizados, probados y validados por el equipo en los entornos de prueba para asegurar su funcionamiento y coherencia con la arquitectura propuesta.

---

## 4. Reflexiones Individuales y Conclusiones

### Reflexión de David Díaz
*El desarrollo de este proyecto me ha permitido aprender de verdad cómo todo funciona y de que trata el ramo de DevOps. A lo largo del proyecto hemos ido construyendo la infraestructura, paso a paso, empezando por trabajar con una API REST con Spring Boot (Que fue realizada en otro ramo, pero nops sirve para trabajar aqui) y GitFlow, y llegando a la automatización del ciclo de integración y despliegue continuo con GitHub Actions, Docker y Kubernetes.

Fue un reto conseguir confeccionar el pipeline correctamente, de modo que cada etapa dependiera de la anterior y solo se desplegara código que cumpliera con los estándares de calidad exigidos en las rúbricas de las evaluaciones. También supuso un gran desafío la configuración del servidor en AWS, la instalación de K3s, el despliegue de los manifiestos de Kubernetes y la integración de herramientas de monitoreo como Prometheus y Grafana. El hecho de tener controles de calidad con SonarQube y la posibilidad de bloquear la evolución mediante el Quality Gate me ha hecho ver la importancia de incorporar siempre la seguridad en todo el ciclo de desarrollo.

En el proyecto yo mismo contribuí activamente a la implementación y configuración de la infraestructura, y participé en la creación del pipeline de integración continua, la contenerización de la aplicación, el despliegue en Kubernetes, y la configuración de las herramientas de análisis de calidad. Además colaboré en la documentación técnica y la validación de cada uno de los requisitos solicitados, procurando que el proyecto se mantuviera lo más ordenado posible y con buena práctica en desarrollo colaborativo.

Como aprendizaje personal, de esta experiencia he sacado que me ha reforzado mis conocimientos sobre automatización, sobre despliegue de aplicaciones, sobre administración de servicios en la nube, y sobre monitoreo de sistemas. También he visto confirmada la importancia del trabajo en equipo, la planificación mediante control de versiones, y la implementación de procesos que nos permitan desarrollar software de la manera más segura posible, estable, y fácil de mantener.*

### Reflexión de Matías Peirano
