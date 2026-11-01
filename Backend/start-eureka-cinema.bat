@echo off
echo ===================================================
echo [1/2] Demarrage d'Eureka Server (Port 8761)...
echo ===================================================
start "Eureka Server" cmd /k "cd /d %~dp0eureka && .\mvnw.cmd spring-boot:run"

echo.
echo Attente du demarrage d'Eureka (8 secondes)...
timeout /t 8 /nobreak >nul

echo.
echo ===================================================
echo [2/2] Demarrage de Cinema-Reservation (Port 8085)...
echo ===================================================
start "Cinema-Reservation" cmd /k "cd /d %~dp0microservices\cinema-reservation && .\mvnw.cmd spring-boot:run"

echo.
echo ===================================================
echo Eureka et Cinema-Reservation ont ete lances !
echo - Eureka Dashboard : http://localhost:8761
echo - Cinema-Reservation : http://localhost:8085
echo ===================================================
