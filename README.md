# Proyecto de Automatización OrangeHRM (Java + Selenium + TestNG + Log4j2)

Framework de automatización de pruebas web para la plataforma **OrangeHRM** ([https://opensource-demo.orangehrmlive.com](https://opensource-demo.orangehrmlive.com)) desarrollado bajo el patrón **Page Object Model (POM)**.

---

## 🛠️ Tecnologías y Librerías

- **Lenguaje:** Java 17+
- **Gestor de Construcción:** Maven 3.9+
- **Automatización de Navegador:** Selenium WebDriver 4.28+ (con Selenium Manager automático)
- **Framework de Pruebas:** TestNG 7.10+
- **Sistema de Logging:** Apache Log4j2 + SLF4J
- **Reportes:** Surefire Reports y capturas automáticas de pantalla en fallos

---

## 📁 Estructura del Proyecto

```text
d:/Proyecto modulo3/
├── pom.xml                                   # Configuración de dependencias Maven
├── testng.xml                                # Suite de pruebas TestNG
├── .gitignore                                # Exclusiones para control de versiones
├── README.md                                 # Guía del proyecto
├── src/
│   ├── main/
│   │   ├── java/com/orangehrm/
│   │   │   ├── driver/
│   │   │   │   └── DriverFactory.java        # ThreadLocal WebDriver para ejecución concurrente
│   │   │   ├── utils/
│   │   │   │   └── ConfigReader.java         # Lector centralizado de propiedades y CLI overrides
│   │   │   └── pages/
│   │   │       ├── BasePage.java             # Métodos comunes con esperas explícitas (WebDriverWait)
│   │   │       ├── LoginPage.java            # Page Object: pantalla de inicio de sesión
│   │   │       ├── DashboardPage.java        # Page Object: panel principal y navegación
│   │   │       └── PimPage.java              # Page Object: módulo PIM (Add Employee y Employee List)
│   │   └── resources/
│   │       ├── config.properties             # Configuración base (URL, navegador, credenciales)
│   │       └── log4j2.xml                    # Configuración de logs con colores y archivo
│   └── test/
│       ├── java/com/orangehrm/
│       │   ├── listeners/
│       │   │   └── TestListener.java         # Listener TestNG para logs y screenshots
│       │   └── tests/
│       │       ├── BaseTest.java             # Ganchos de configuración y cierre de sesión
│       │       ├── LoginTest.java            # Casos de prueba de Login
│       │       ├── DashboardTest.java        # Casos de prueba de Dashboard
│       │       └── PimTest.java              # Casos de prueba del módulo PIM
```

---

## 📋 Matriz de Casos de Prueba Estandarizada

Todos los casos de prueba del proyecto siguen la nomenclatura formal: `CP-<MODULO>-<TIPO>-<NUMERO>`:

### 🔑 Módulo Login
| ID | Descripción | Estado |
| :--- | :--- | :--- |
| **CP-LOGIN-POS-01** | *Verify successful login with valid credentials redirects to Dashboard* | ✅ Automatizado |
| **CP-LOGIN-NEG-01** | *Verify invalid credentials display an error alert* | ✅ Automatizado |
| **CP-LOGIN-POS-02** | *Verify user can log out successfully back to login screen* | ✅ Automatizado |

### 📊 Módulo Dashboard
| ID | Descripción | Estado |
| :--- | :--- | :--- |
| **CP-DASH-POS-01** | *Verify key components are displayed on the Dashboard after login* | ✅ Automatizado |

### 👥 Módulo PIM (Personnel Information Management)
El módulo PIM permite la gestión de empleados y usuarios. Se ha diseñado una matriz separando los casos positivos automatizados de los casos negativos que forman la base/backlog:

### 🟢 Casos de Prueba Positivos (Automatizados)

| ID | Nombre del Caso | Pasos Principales | Resultado Esperado | Estado |
| :--- | :--- | :--- | :--- | :--- |
| **CP-PIM-POS-01** | **Creación básica y verificación en lista** | 1. Ir a PIM -> Add Employee.<br>2. Ingresar First Name, Last Name y Employee Id único.<br>3. Guardar.<br>4. Buscar en Employee List por ID. | El empleado aparece en la lista con su ID y nombre correspondiente. | ✅ Automatizado |
| **CP-PIM-POS-02** | **Creación con nombre completo** | 1. Ir a Add Employee.<br>2. Llenar First Name, Middle Name y Last Name.<br>3. Guardar.<br>4. Buscar en Employee List. | El empleado se lista correctamente con sus nombres y apellidos. | ✅ Automatizado |

### 🔴 Casos de Prueba Negativos (Lista Base / Backlog)

Casos documentados para futuras etapas de automatización:

1. **CP-PIM-NEG-01:** Campo *First Name* requerido (validar mensaje *"Required"* al dejarlo vacío).
2. **CP-PIM-NEG-02:** Campo *Last Name* requerido (validar mensaje *"Required"* al dejarlo vacío).
3. **CP-PIM-NEG-03:** *Employee Id* duplicado (validar mensaje *"Employee Id already exists"* al usar un ID existente).
4. **CP-PIM-NEG-04:** Formato o tamaño de foto inválido (cargar archivo .txt o que exceda 1MB en la foto de perfil).

---

## ⚙️ Configuración (`src/main/resources/config.properties`)

```properties
base.url=https://opensource-demo.orangehrmlive.com
browser=chrome
headless=false
explicit.wait=15

default.username=Admin
default.password=admin123
```

---

## 🚀 Cómo Ejecutar las Pruebas

### 1. Compilar el proyecto
```bash
mvn clean test-compile
```

### 2. Ejecutar todas las pruebas con el navegador por defecto (Chrome)
```bash
mvn test
```

### 3. Ejecutar en modo Headless (sin abrir ventana gráfica)
```bash
mvn test -Dheadless=true
```

### 4. Ejecutar en otro navegador (Firefox o Edge)
```bash
mvn test -Dbrowser=edge
mvn test -Dbrowser=firefox
```

### 5. Ejecutar una clase de prueba específica
```bash
# Pruebas de Login
mvn test -Dtest=LoginTest

# Pruebas de Dashboard
mvn test -Dtest=DashboardTest

# Pruebas del módulo PIM (Agregar usuario/empleado y verificar en lista)
mvn test -Dtest=PimTest
```

---

## 📊 Reportes y Registros (Logs)

- **Logs de Ejecución:** Se muestran en la consola con colores y se guardan automáticamente en:
  `logs/test-execution.log`
- **Capturas de Pantalla:** En caso de que una prueba falle, se guarda una captura en:
  `screenshots/<nombre_test>_<timestamp>.png`
- **Reportes HTML de TestNG:**
  `target/surefire-reports/index.html` o `target/surefire-reports/emailable-report.html`

## 🤖 Pipeline-GitHub Actions

En este repositorio, la suite de pruebas de automatización ya se encuentra integrada con **GitHub Actions**, un servicio de Integración Continua (CI) y Despliegue Continuo (CD) que se ejecuta automáticamente en la infraestructura de GitHub. Esta automatización está configurada para activarse mediante flujos de trabajo (workflows) que responden a eventos específicos del repositorio, como **push** (envío de cambios), **pull request** (solicitud de fusión de código) o **ejecuciones manuales**.

### ⚙️ Configuración del Flujo de Trabajo

La configuración del pipeline se define en el archivo **`.github/workflows/maven.yml`** y está diseñada para ejecutarse en un entorno virtual de **Ubuntu** cada vez que se detecten cambios relevantes en la rama principal (`main`). Los componentes clave de esta automatización incluyen:

### 🧩 Componentes de la Automatización

- **Activadores (Triggers):**
  - `push:` El pipeline se inicia automáticamente cuando se envían cambios (push) a la rama `main` o cuando se crea una solicitud de extracción (pull request) a esta misma rama.
  - `workflow_dispatch:` Permite la ejecución manual del flujo de trabajo directamente desde la interfaz de GitHub, facilitando pruebas bajo demanda sin necesidad de enviar código.

- **Entorno de Ejecución:**
  - `runs-on: ubuntu-latest:` El trabajo se ejecuta en la última versión del sistema operativo Ubuntu proporcionado por GitHub Actions, asegurando un entorno actualizado y consistente.

- **Configuración del JDK:**
  - Se configura automáticamente la versión 17 de Java (utilizando OpenJDK **Temurin**), que es la requerida por el proyecto para compilar y ejecutar el código.
  - Se habilita el caché de Maven (`cache: 'maven'`) para optimizar los tiempos de ejecución, almacenando los artefactos de descarga y evitando la necesidad de volver a descargar dependencias en ejecuciones futuras.

- **Ejecución de Pruebas:**
  - `run: mvn test:` El comando central que ejecuta la suite completa de pruebas de TestNG. Durante esta fase:
    - Se compila el código fuente (`mvn clean test-compile`).
    - Se ejecutan los casos de prueba automatizados.
    - Los resultados se recopilan y se generan los reportes correspondientes.

- **Recolección de Artefactos:**
  - Al finalizar la ejecución, independientemente de si las pruebas fueron exitosas o fallidas (`if: always()`), se recolectan y almacenan los siguientes artefactos:
    - **Logs de ejecución:** Incluyen el detalle de cada paso, los resultados de las pruebas y cualquier mensaje de error o advertencia (`logs/`).
    - **Capturas de pantalla:** Automáticamente se guardan imágenes de los casos fallidos, con un nombre descriptivo y un sello de tiempo (`screenshots/`).
    - **Reportes de TestNG:** Los informes HTML y XML que detallan el rendimiento y los resultados de la suite de pruebas (`target/surefire-reports/`).

### ✅ Beneficios del Pipeline Actual

Gracias a esta integración, el proyecto obtiene las siguientes ventajas:

1. **Automatización Inmediata:** Las pruebas se ejecutan automáticamente cada vez que hay cambios en la rama `main`, proporcionando una retroalimentación instantánea sobre la calidad del código.
2. **Entorno Controlado:** Todas las ejecuciones se realizan en el mismo entorno (`ubuntu-latest` con JDK 17), eliminando las variaciones que podrían surgir en entornos de desarrollo locales.
3. **Consistencia:** Se garantiza que las pruebas se ejecuten de la misma manera en cada ejecución, ya que las dependencias y configuraciones están estandarizadas.
4. **Seguridad:** El uso de GitHub Secrets para credenciales sensibles mejora significativamente la seguridad del proyecto.
5. **Visibilidad Total:** La recolección automática de logs y capturas de pantalla facilita la depuración y el análisis de cualquier fallo, permitiendo una rápida identificación de los problemas.
6. **Escalabilidad:** El pipeline puede ser escalado para incluir etapas adicionales como pruebas de rendimiento, análisis estático de código o despliegues automáticos.

En resumen, la automatización con GitHub Actions ya está completamente operativa, asegurando que cada cambio en la rama principal sea validado automáticamente mediante la suite de pruebas de automatización.
