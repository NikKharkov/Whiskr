# 🐱 Whiskr Client (iOS & Android)

![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-purple?logo=kotlin)
![Compose Multiplatform](https://img.shields.io/badge/Compose-Multiplatform-blue?logo=jetpackcompose)
![iOS](https://img.shields.io/badge/iOS-Supported-black?logo=apple)
![Android](https://img.shields.io/badge/Android-Supported-green?logo=android)

**Whiskr** is a cross-platform social network for pets built with **Compose Multiplatform**. It demonstrates a modern architecture, handling complex user flows, real-time communication, AI integration, and payments on both iOS and Android from a single codebase.

**Backend Repository:** [Whiskr Backend Server](https://github.com/NikKharkov/WhiskrBackend)

---

## 🎨 Gallery & Features

### 🔐 Auth & Onboarding
<table>
  <tr>
    <td align="center">
      <img src="screenshots/login.jpg" width="240" alt="Login Screen" /><br>
      <b>Login</b>
    </td>
    <td align="center">
      <img src="screenshots/user_reg.jpg" width="240" alt="User Registration" /><br>
      <b>Registration</b>
    </td>
    <td align="center">
      <img src="screenshots/verification.jpg" width="240" alt="Verification" /><br>
      <b>Verification</b>
    </td>
  </tr>
</table>

### 🏠 Core Experience
<table>
  <tr>
    <td align="center">
      <img src="screenshots/home.jpg" width="240" alt="Home Feed" /><br>
      <b>Feed</b>
    </td>
    <td align="center">
      <img src="screenshots/explore.jpg" width="240" alt="Explore" /><br>
      <b>Explore</b>
    </td>
    <td align="center">
      <img src="screenshots/create_post.jpg" width="240" alt="Create Post" /><br>
      <b>Create Post</b>
    </td>
  </tr>
</table>

### 👤 Profile & Social
<table>
  <tr>
    <td align="center">
      <img src="screenshots/profile.jpg" width="240" alt="User Profile" /><br>
      <b>Profile</b>
    </td>
    <td align="center">
      <img src="screenshots/pet_reg.jpg" width="240" alt="Pet Registration" /><br>
      <b>Add Pet</b>
    </td>
    <td align="center">
      <img src="screenshots/notifications.jpg" width="240" alt="Notifications" /><br>
      <b>Activity</b>
    </td>
  </tr>
</table>

### 🤖 AI & Communication
<table>
  <tr>
    <td align="center">
      <img src="screenshots/ai_studio.jpg" width="240" alt="AI Studio" /><br>
      <b>AI Generation</b>
    </td>
    <td align="center">
      <img src="screenshots/chat.jpg" width="240" alt="Chat" /><br>
      <b>Real-time Chat</b>
    </td>
    <td align="center">
      <img src="screenshots/replies.jpg" width="240" alt="Comments" /><br>
      <b>Discussions</b>
    </td>
  </tr>
</table>

### 🛍️ Monetization (Stripe)
<table>
  <tr>
    <td align="center">
      <img src="screenshots/shop.gif" width="300" alt="Shop Demo" /><br>
      <b>Premium Shop Flow</b>
    </td>
  </tr>
</table>
---

## 🛠 Tech Stack

The project leverages the most advanced libraries in the Kotlin Multiplatform ecosystem:

* **UI Framework:** Compose Multiplatform
* **Navigation:** Decompose
* **Architecture:** Feature-Based Modularization + MVI
* **Dependency Injection:** Kotlin Inject
* **Networking:** Ktor + Ktorfit
* **Local Storage:** Room KMP + KVault (Secure storage)
* **Media Loading:** Coil 3 + Chaintech Mediaplayer
* **Integration:**
    * **Firebase:** Auth & Messaging (KMP Auth)
    * **Stripe:** Payment processing
    * **WebSockets:** Real-time chat features
    * **KMPNotifier:** Push notifications with deeplink logic

## 📂 Project Structure

* `core/`: Shared components, networking, storage, and UI kit.
* `features/`: Independent feature modules (Auth, Chat, Feed, AI Studio, etc.).
* `flows/`: High-level navigation flows integrating multiple features.
* `composeApp/`: Main entry point assembling the application.

## 🚀 How to Run

### Configuration
The project requires a constants file with API keys (Base URL, Stripe Key, etc.).
Ensure `Constants.kt` is properly configured in the `commonMain` source set before building.

### Android
Open the project in **Android Studio (Ladybug or newer)** and run the `app` configuration.

### iOS
1.  Navigate to the iOS directory:
    ```bash
    cd iosApp
    ```
2.  Install Pods (required for Firebase/Stripe):
    ```bash
    pod install
    ```
3.  Open `iosApp.xcworkspace` in Xcode and run.

---