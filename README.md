# 🐱 Whiskr Client (iOS & Android)

![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-purple?logo=kotlin)
![Compose Multiplatform](https://img.shields.io/badge/Compose-Multiplatform-blue?logo=jetpackcompose)
![iOS](https://img.shields.io/badge/iOS-Supported-black?logo=apple)
![Android](https://img.shields.io/badge/Android-Supported-green?logo=android)

**Whiskr** is a cross-platform social network for pets built with **Compose Multiplatform**. It demonstrates a modern architecture, handling complex user flows, real-time communication, AI integration, and payments on both iOS and Android from a single codebase.

**Backend Repository:** [Whiskr Backend Server](https://github.com/NikKharkov/WhiskrBackend)

---

## Screenshots 📱

### Onboarding & Auth
|          Login Screen           |           User Registration           |               Verification Flow               |
|:-------------------------------:|:-------------------------------------:|:---------------------------------------------:|
| ![Login](screenshots/login.jpg) | ![User Reg](screenshots/user_reg.jpg) | ![Verification](screenshots/verification.jpg) |

### Core Experience
|           Home Feed           |          Explore & Search           |                 Create Post                 |
|:-----------------------------:|:-----------------------------------:|:-------------------------------------------:|
| ![Home](screenshots/home.jpg) | ![Explore](screenshots/explore.jpg) | ![Create Post](screenshots/create_post.jpg) |

### Profile & Social
|            User Profile             |          Pet Registration           |                  Notifications                  |
|:-----------------------------------:|:-----------------------------------:|:-----------------------------------------------:|
| ![Profile](screenshots/profile.jpg) | ![Pet Reg](screenshots/pet_reg.jpg) | ![Notifications](screenshots/notifications.jpg) |

### AI & Features
|                AI Studio                |        Real-time Chat         |         Comments & Replies          |
|:---------------------------------------:|:-----------------------------:|:-----------------------------------:|
| ![AI Studio](screenshots/ai_studio.jpg) | ![Chat](screenshots/chat.jpg) | ![Replies](screenshots/replies.jpg) |

### Monetization
|     Premium Shop (Stripe)     |
|:-----------------------------:|
| ![Shop](screenshots/shop.gif) |

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
Open the project in **Android Studio** and run the `app` configuration.

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