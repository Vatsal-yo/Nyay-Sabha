# Nyay Sabha



## Stack
- Frontend: React + Vite
- Backend: Core Java 17+ with `HttpServer` and built-in `HttpClient`
- AI: Groq API through Java backend


## Run backend (PowerShell)
```powershell
cd backend
$env:GROQ_API_KEY="YOUR_GROQ_API_KEY"
$env:GROQ_MODEL="openai/gpt-oss-120b"
javac --add-modules jdk.httpserver -d out (Get-ChildItem src/nyaysabha/*.java).FullName
java --add-modules jdk.httpserver -cp out nyaysabha.Main
```
The API runs at `http://localhost:8080`. Without a key, the server uses demo mode.

## Run frontend in a second terminal
```powershell
cd frontend
npm install
npm run dev
```
Open `http://localhost:5173`.





