@echo off
cd /d "%~dp0"
chcp 65001 >nul
where npm >nul 2>nul
if errorlevel 1 (
  echo 未检测到 npm，请先安装 Node.js 18 或更高版本。
  pause
  exit /b 1
)
if not exist node_modules (
  echo 首次运行，正在安装前端依赖...
  call npm install
  if errorlevel 1 (
    echo 依赖安装失败，请检查网络。
    pause
    exit /b 1
  )
)
echo 前端启动后请访问 http://127.0.0.1:5173
call npm run dev
pause
