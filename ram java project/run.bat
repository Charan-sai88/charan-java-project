@echo off
title Product and Billing System
set /p DB_PASSWORD="Enter MySQL root password (press Enter if empty): "
set DB_USERNAME=root
echo Starting Product and Billing System on http://localhost:8080 ...
java -jar "%~dp0target\product-billing-0.0.1-SNAPSHOT.jar" --server.port=8080
pause
