# Nyay Sabha — Core Java backend

Requirements: JDK 17+; no Spring Boot, no third-party Java libraries.

## Configure Groq (PowerShell)
```powershell
$env:GROQ_API_KEY="YOUR_GROQ_API_KEY"
$env:GROQ_MODEL="openai/gpt-oss-120b"
```
Never put the key in React, source files, screenshots, or Git.

## Compile and run
```powershell
cd backend
javac --add-modules jdk.httpserver -d out (Get-ChildItem src/nyaysabha/*.java).FullName
java --add-modules jdk.httpserver -cp out nyaysabha.Main
```
Backend URL: `http://localhost:8080`.

## API
- `GET /api/health`
- `GET /api/categories`
- `GET /api/cases?category=CRIMINAL`
- `POST /api/trials/start`
- `POST /api/trials/argument`
- `POST /api/trials/objection`
- `POST /api/trials/evaluate-round`
- `POST /api/trials/rest`
- `POST /api/trials/finish`

POST bodies use `application/x-www-form-urlencoded`.

## Deferred legal references
No verified statutory text is supplied to the judge yet. The judge is instructed not to invent legal citations. Statute library (BNS, BSA, BNSS and relevant laws) and JDBC/MySQL are planned for December.

## Limitations
- Active trials are stored in memory.
- Groq requires internet, a valid API key, model availability and account quota.
- Demo mode is a placeholder, not AI or legal advice.
- AI judgments are educational simulations only.
