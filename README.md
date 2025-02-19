# FlashCards - Android Jetpack Compose

FlashCards es una aplicación de entrevistas técnicas interactiva que utiliza **Jetpack Compose**, **MVI con Clean Architecture**, y **Google Gemini** para generar y corregir preguntas de entrevistas técnicas.

## 📌 Características

- **Selección de dificultad y temas**: El usuario puede elegir entre niveles de dificultad (`Jr`, `Ssr`, `Senior`) y temas (`Jetpack Compose`, `Coroutines`, `Networking`, `Performance`, `Flows`).
- **Generación de preguntas**: Utiliza la API de **Google Gemini** para generar preguntas basadas en la dificultad y el tema elegido.
- **Respuesta por voz**: Implementación de **Speech-to-Text** con `SpeechRecognizer`.
- **Corrección automática**: La respuesta del usuario se envía a **Gemini** para recibir feedback. Gemini puede devolver links relevantes si lo considera necesario, que son clickeables.
- **UI intuitiva**: Implementado en **Jetpack Compose** con **Hilt para DI** y **Retrofit para llamadas a API**.

## 📁 Estructura del Proyecto

```plaintext
📦 FlashCards
 ┣ 📂 app
 ┃ ┣ 📂 data  // Fuente de datos (API, Repositorios, etc.)
 ┃ ┣ 📂 domain // Casos de uso y lógica de negocio
 ┃ ┣ 📂 ui     // Presentación y pantallas
 ┃ ┣ 📂 utils  // Helpers y clases de utilidad
```

## 🚀 Instalación y Configuración

### 1️⃣ Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/FlashCards.git
cd FlashCards
```

### 2️⃣ Agregar API Key de Google Gemini

Crear un archivo `secrets.properties` en la raíz del proyecto y agregar:

```properties
GEMINI_API_KEY=tu_api_key
```

### 3️⃣ Ejecutar el Proyecto

Desde Android Studio, selecciona un emulador/dispositivo y ejecuta 🚀.

## 🛠️ Tecnologías Utilizadas

- **Jetpack Compose** - UI declarativa
- **MVI con Clean Architecture** - Arquitectura escalable
- **Hilt** - Inyección de dependencias
- **Retrofit** - Consumo de API
- **Google Gemini API** - Generación y evaluación de preguntas
- **SpeechRecognizer** - Conversión de voz a texto

## 📜 Licencia

Este proyecto está bajo la licencia **MIT**.

## 👨‍💻 Autor

**charr0max** - [GitHub](https://github.com/tu-usuario)

---

### ⭐ ¡Contribuye!

Si quieres mejorar la app, siéntete libre de hacer un fork y enviar un PR. 🚀

**ScreenShots**

<div align="center">
    <img src="https://github.com/user-attachments/assets/50907783-bdf6-437d-a64a-9f0066a9f199" width="30%" />
    <img src="https://github.com/user-attachments/assets/a8e5a04b-e748-4d23-a668-d3e39aabe24e" width="30%" />
    <img src="https://github.com/user-attachments/assets/6518e215-9e9f-4603-bfcf-82eaa028a2eb" width="30%" />
</div>



