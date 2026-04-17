@echo off
echo Starting Vanguard Tech Store...

REM Try to start Python backend, fallback to Node if it fails
echo [1/2] Attempting to start backend...
start /b cmd /c "python backend/main.py || node backend/server.js"

echo [2/2] Starting Frontend (HTTP Server)...
start /b cmd /c "npx -y http-server frontend -p 3000"

echo.
echo Application is starting! 
echo Backend: http://localhost:8000
echo Frontend: http://localhost:3000
echo.
echo Press any key to stop the servers...
pause > nul

taskkill /f /im node.exe /t > nul 2>&1
taskkill /f /im python.exe /t > nul 2>&1
echo Servers stopped.
