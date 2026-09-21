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
│   │   │       ├── LoginPage.java            # Page Object de la pantalla de inicio de sesión
│   │   │       └── DashboardPage.java        # Page Object del panel principal
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
│       │       └── DashboardTest.java        # Casos de prueba de Dashboard
```

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
mvn test -Dtest=LoginTest
mvn test -Dtest=DashboardTest
```

---

## 📊 Reportes y Registros (Logs)

- **Logs de Ejecución:** Se muestran en la consola con colores y se guardan automáticamente en:
  `logs/test-execution.log`
- **Capturas de Pantalla:** En caso de que una prueba falle, se guarda una captura en:
  `screenshots/<nombre_test>_<timestamp>.png`
- **Reportes HTML de TestNG:**
  `target/surefire-reports/index.html` o `target/surefire-reports/emailable-report.html`
