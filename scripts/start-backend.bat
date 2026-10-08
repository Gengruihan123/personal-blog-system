@echo off
chcp 65001 >nul
cd /d "%~dp0..\backend"
where mvn >nul 2>nul
if errorlevel 1 (
  echo 未检测到 Maven，请先安装 JDK 17 和 Maven，并添加到 PATH。
  pause
  exit /b 1
)
call mvn spring-boot:run
pause
