#!/bin/bash

# Флаг -e остановит скрипт ровно на той команде, которая упадет с ошибкой
set -e

echo "🚀 Начинаем локальную сборку iOS..."

# 1. Настройка окружения
# Находим Java 17 на твоем Маке и железно прописываем ее в терминал
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
echo "☕️ JAVA_HOME установлен в: $JAVA_HOME"

# Переходим в корень проекта на всякий случай
PROJECT_ROOT=$(pwd)

# 2. Подготовка Gradle
echo "🧹 Очистка старых билдов Gradle..."
./gradlew clean --no-daemon

echo "🔨 Генерация Dummy Framework для CocoaPods..."
chmod +x gradlew
./gradlew :app:generateDummyFramework --no-daemon

# 3. CocoaPods
echo "📦 Установка зависимостей CocoaPods..."
cd iosApp
# Удаляем старый кэш подов, чтобы исключить глюки
rm -rf Pods
pod install
cd ..

# 4. Сборка Xcode
echo "🍏 Запуск Xcode сборки..."
cd iosApp

# Запускаем сборку workspace (так как используем поды) под симулятор
xcodebuild clean build \
  -workspace iosApp.xcworkspace \
  -scheme iosApp \
  -destination 'generic/platform=iOS Simulator' \
  CODE_SIGN_IDENTITY="" \
  CODE_SIGNING_REQUIRED=NO \
  CODE_SIGNING_ALLOWED=NO \
  COMPILER_INDEX_STORE_ENABLE=NO

echo "✅ Билд прошел успешно!"