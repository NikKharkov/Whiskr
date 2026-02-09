# 🐱 Whiskr Client (iOS & Android)

![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-purple?logo=kotlin)
![Compose Multiplatform](https://img.shields.io/badge/Compose-Multiplatform-blue?logo=jetpackcompose)
![iOS](https://img.shields.io/badge/iOS-Supported-black?logo=apple)
![Android](https://img.shields.io/badge/Android-Supported-green?logo=android)

**Whiskr** is a cross-platform social network for pets built with **Compose Multiplatform**. It demonstrates a modern architecture ,handling complex user flows, real-time communication, AI integration, and payments on both iOS and Android from a single codebase.

**Backend Repository:** [Whiskr Backend Server](https://github.com/NikKharkov/WhiskrBackend)

---

## 🎨 Gallery & Features

<table style="width:100%; text-align: center;">
  <tr>
    <th colspan="3">Onboarding & Authentication</th>
  </tr>
  <tr>
    <td width="33%">
        <b>Login & Auth</b><br>
        <img src="screenshots/login.jpg" width="100%">
    </td>
    <td width="33%">
        <b>User Registration</b><br>
        <img src="screenshots/user_reg.jpg" width="100%">
    </td>
    <td width="33%">
        <b>Verification Flow</b><br>
        <img src="screenshots/verification.jpg" width="100%">
    </td>
  </tr>
    <tr>
    <th colspan="3">Core Experience</th>
  </tr>
  <tr>
    <td width="33%">
        <b>Home Feed</b><br>
        <img src="screenshots/home.jpg" width="100%">
    </td>
    <td width="33%">
        <b>Explore & Search</b><br>
        <img src="screenshots/explore.jpg" width="100%">
    </td>
    <td width="33%">
        <b>Post Creation</b><br>
        <img src="screenshots/create_post.jpg" width="100%">
    </td>
  </tr>
   <tr>
    <th colspan="3">Social & Profile</th>
  </tr>
  <tr>
    <td width="33%">
        <b>User Profile</b><br>
        <img src="screenshots/profile.jpg" width="100%">
    </td>
    <td width="33%">
        <b>Pet Registration</b><br>
        <img src="screenshots/pet_reg.jpg" width="100%">
    </td>
    <td width="33%">
        <b>Notifications</b><br>
        <img src="screenshots/notifications.jpg" width="100%">
    </td>
  </tr>
  <tr>
    <th colspan="3">Advanced Features</th>
  </tr>
  <tr>
    <td width="33%">
        <b>AI Studio</b><br>
        <i>Generate content with AI</i><br>
        <img src="screenshots/ai_studio.jpg" width="100%">
    </td>
    <td width="33%">
        <b>Real-time Chat</b><br>
        <i>WebSocket messaging</i><br>
        <img src="screenshots/chat.jpg" width="100%">
    </td>
     <td width="33%">
        <b>Comments & Replies</b><br>
        <img src="screenshots/replies.jpg" width="100%">
    </td>
  </tr>
    <tr>
    <th colspan="3">Monetization</th>
  </tr>
  <tr>
      <td colspan="3" align="center">
        <b>Premium Shop & Stripe Payments</b><br>
        <img src="screenshots/shop.gif" width="300">
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