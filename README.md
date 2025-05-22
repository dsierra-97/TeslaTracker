# TeslaTracker 🚗⚡

Una aplicación Android para registrar avistamientos de Teslas en distintos lugares, desarrollada en Java con Room como solución de persistencia local.
Empezó como un juego entre mi pareja y yo cuando aparecieron los primeros coches Tesla. Rápidamente pasamos del "Ey, mira un tesla" al "ese ya lo hemos visto" o
"ese me suena" y de ahí a apuntar parcialmente las matriculas de los teslas que veíamos. Finalmente se me ocurrió hacer una app para registrar todo esto y de paso
aprender Android. La app sigue en desarrollo (comencé en diciembre de 2024) ya que programo en mi tiempo libre.

## 📱 Funcionalidades

- Registrar avistamientos de vehículos Tesla y sus atributos: letras de la matricula, color, pais, etc.
- Asociar observadores al avistamiento.
- Contador de número de veces visto por matrícula.
- Filtrado y búsqueda por atributos.
- Vista de detalle con opción de edición.
- Persistencia local con Room y LiveData.

## 🛠️ Tecnologías usadas

- **Java** (Android SDK)
- **Room** (persistencia de datos)
- **LiveData** y **ViewModel** (arquitectura MVVM)
- **Navigation Component** para navegación entre fragmentos
- **RecyclerView** para mostrar listas
- **Gradle** (Java DSL)

## 🧱 Arquitectura

```text
MVVM (Model - View - ViewModel)
│
├── Room (DAO, Entity, Database)
├── ViewModel (lógica de negocio y acceso a Room)
└── UI (Fragments + RecyclerView)
```

## 📸 Capturas

*(Puedes añadir capturas de pantalla aquí si lo deseas)*

## 🚀 Cómo ejecutar el proyecto

1. Clona el repositorio
2. Ábrelo en Android Studio.
3. Ejecuta la aplicación en un emulador o dispositivo físico.
   Requiere Java 21 y Android Studio actualizado.

## 👤 Autor

Desarrollado por Diego Sierra Portilla, programador backend con experiencia en Java, Spring, JPA/Hibernate y PostgreSQL, actualmente explorando desarrollo Android nativo.

## 📄 Licencia

Este proyecto está bajo la licencia APACHE 2.0. Consulta el archivo LICENSE para más información.

## 📢 Descargo de responsabilidad

Esta aplicación no está afiliada, patrocinada ni respaldada por Tesla, Inc. "Tesla" es una marca registrada de Tesla, Inc. Todos los nombres, logotipos y marcas pertenecen a sus respectivos propietarios y se utilizan únicamente con fines de identificación.
Esta aplicación está concebida como un proyecto de aprendizaje, su uso es exclusivamente personal y sin ánimo de lucro.

