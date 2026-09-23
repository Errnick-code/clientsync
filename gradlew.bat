@echo off
cd /d "%~dp0"

echo ============================================
echo   Обновление Gradle wrapper до версии 9.6.1
echo   (прямая правка properties, без gradlew.bat)
echo ============================================
echo.

if not exist "gradle\wrapper\gradle-wrapper.properties" (
    echo [ОШИБКА] Не найден gradle\wrapper\gradle-wrapper.properties
    echo Убедись, что этот файл лежит в корне проекта.
    pause
    exit /b 1
)

echo Старое содержимое:
type "gradle\wrapper\gradle-wrapper.properties"
echo.

(
echo distributionBase=GRADLE_USER_HOME
echo distributionPath=wrapper/dists
echo distributionUrl=https\://services.gradle.org/distributions/gradle-9.6.1-bin.zip
echo networkTimeout=10000
echo validateDistributionUrl=true
echo zipStoreBase=GRADLE_USER_HOME
echo zipStorePath=wrapper/dists
) > "gradle\wrapper\gradle-wrapper.properties"

echo Новое содержимое:
type "gradle\wrapper\gradle-wrapper.properties"
echo.

echo ============================================
echo   Готово. При следующем запуске gradlew.bat
echo   Gradle сам скачает версию 9.6.1.
echo ============================================
pause